package com.fiftyfive.rating.steamapp.service;

import com.fiftyfive.rating.steamapp.domain.SteamAppEntity;
import com.fiftyfive.rating.steamapp.domain.SteamAppRepository;
import com.fiftyfive.rating.steamapp.dto.SteamAppDetails;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final SteamAppRepository steamAppRepository;

    public List<SteamAppDetails> getAppList() throws InterruptedException {
        String url = "https://steamspy.com/api.php?request=top100in2weeks";
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();

        WebClient webClient = WebClient.builder().baseUrl(url).exchangeStrategies(exchangeStrategies).build();
        String appListStr = webClient.get().retrieve().bodyToMono(String.class).block();

        JsonObject object = JsonParser.parseString(appListStr).getAsJsonObject();

        List<SteamAppDetails> result = new ArrayList<>();

        for(Map.Entry<String, JsonElement> entry : object.entrySet()) {
            JsonObject steamApp = entry.getValue().getAsJsonObject();
            Long appId = steamApp.get("appid").getAsLong();
            String detailUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId;

            webClient = WebClient.builder().baseUrl(detailUrl).exchangeStrategies(exchangeStrategies).build();
            String jsonString = webClient.get().retrieve().bodyToMono(String.class).block();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonObject appData = jsonObject.getAsJsonObject(appId.toString());
            if (appData != null && appData.get("success").getAsBoolean()) {
                SteamAppDetails appDetails = new Gson().fromJson(appData.getAsJsonObject("data"), SteamAppDetails.class);

                if(appDetails.getType().equals("game")) {
                    result.add(appDetails);
                    try {
                        steamAppRepository.save(new SteamAppEntity(appId, appDetails.getName(), appDetails.getHeader_image()));
                    } catch (Exception e) {
                        System.out.println(appId + " error");
                    }
                }
            }

            TimeUnit.MILLISECONDS.sleep(1000L);
        }

        return result;
    }
}
