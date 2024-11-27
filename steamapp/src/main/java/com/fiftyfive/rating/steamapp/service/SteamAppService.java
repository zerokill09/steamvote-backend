package com.fiftyfive.rating.steamapp.service;

import com.fiftyfive.rating.steamapp.domain.SteamAppEntity;
import com.fiftyfive.rating.steamapp.domain.SteamAppRepository;
import com.fiftyfive.rating.steamapp.dto.AppIdsMessage;
import com.fiftyfive.rating.steamapp.dto.SteamAppDto;
import com.fiftyfive.rating.steamapp.dto.ListDto;
import com.fiftyfive.rating.steamapp.dto.ListReqDto;
import com.fiftyfive.rating.steamapp.mapper.SteamAppMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class SteamAppService {
    private final KafkaProducer kafkaProducer;
    private final SteamAppRepository steamAppRepository;
    private final SteamAppMapper steamAppMapper;

    public ListDto<SteamAppDto> getCatalogs(Long userId, ListReqDto listReqDto) throws ExecutionException, InterruptedException, TimeoutException {
        Pageable pageable = PageRequest.of(listReqDto.getPage(), listReqDto.getSize());
        Page<SteamAppEntity> catalogEntities = steamAppRepository.findAll(pageable);
        ListDto<SteamAppDto> catalogDtoPage = steamAppMapper.toDtoPage(catalogEntities);

        if(userId != null) {
            List<Long> appIds = catalogEntities.getContent().stream().map(SteamAppEntity::getAppId).toList();
            AppIdsMessage appIdsMessage = new AppIdsMessage(userId, appIds);
            String result = kafkaProducer.syncSendAppIds("req_apps_liked", "res_apps_liked", "data", appIdsMessage);
            JsonArray likes = JsonParser.parseString(result).getAsJsonArray();

            for(JsonElement like : likes) {
                JsonObject obj = like.getAsJsonObject();
                Long appId = obj.get("appId").getAsLong();
                Boolean liked = obj.get("liked").getAsBoolean();

                for(SteamAppDto steamAppDto : catalogDtoPage.getContent()) {
                    if(steamAppDto.getAppId().equals(appId)) {
                        steamAppDto.setIsLiked(liked);
                        break;
                    }
                }
            }
        }

        return catalogDtoPage;
    }
}
