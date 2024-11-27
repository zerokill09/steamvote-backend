package com.fiftyfive.rating.vote.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiftyfive.rating.vote.domain.VoteEntity;
import com.fiftyfive.rating.vote.domain.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final VoteRepository voteRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "req_apps_liked", containerFactory = "replyKafkaListenerContainerFactory")
    @SendTo
    public String listen(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<HashMap<Object, Object>>() {});
            Long userId = Long.parseLong(map.get("userId").toString());
            List<Long> appIds =  mapper.readValue(map.get("appIds").toString(),   new TypeReference<List<Long>>() {});
            List<VoteEntity> likeEntities = voteRepository.findByUserIdAndAppIdInAndLikedIsNotNull(userId, appIds);

            return objectMapper.writeValueAsString(likeEntities);
        } catch (JsonProcessingException e) {
            return "error";
        }
    }
}
