package com.fiftyfive.rating.steamapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiftyfive.rating.steamapp.domain.SteamAppEntity;
import com.fiftyfive.rating.steamapp.domain.SteamAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SteamAppRepository steamAppRepository;

    @KafkaListener(topics = "like_topic")
    public void updateLikeCount(String kafkaMessage) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<HashMap<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Optional<SteamAppEntity> catalogEntityOptional = steamAppRepository.findByAppId(Long.parseLong(map.get("appId").toString()));
        if (catalogEntityOptional.isPresent()) {
            SteamAppEntity steamAppEntity = catalogEntityOptional.get();
            Boolean liked = (Boolean) map.get("liked");
            Boolean disliked = (Boolean) map.get("disliked");

            if(liked != null) {
                if(liked) {
                    steamAppEntity.setLiked(steamAppEntity.getLiked() + 1);
                } else {
                    steamAppEntity.setLiked(steamAppEntity.getLiked() - 1);
                }
            }

            if(disliked != null) {
                if(disliked) {
                    steamAppEntity.setDisliked(steamAppEntity.getDisliked() + 1);
                } else {
                    steamAppEntity.setDisliked(steamAppEntity.getDisliked() - 1);
                }
            }

            steamAppRepository.save(steamAppEntity);
        }
    }
}
