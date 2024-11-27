package com.fiftyfive.rating.vote.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiftyfive.rating.vote.dto.SteamAppMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, SteamAppMessage steamAppMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
           jsonString = objectMapper.writeValueAsString(steamAppMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, topic, jsonString);
    }
}
