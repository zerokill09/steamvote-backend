package com.fiftyfive.rating.vote.controller;

import com.fiftyfive.rating.vote.dto.SteamAppMessage;
import com.fiftyfive.rating.vote.dto.VoteDto;
import com.fiftyfive.rating.vote.service.KafkaProducer;
import com.fiftyfive.rating.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    private final KafkaProducer kafkaProducer;

    @PutMapping("/like")
    public void writeLike(@RequestHeader("X-User-Id") Long userId, @RequestBody VoteDto voteDto) {
        SteamAppMessage steamAppMessage = voteService.writeLike(userId, voteDto);
        kafkaProducer.send("like_topic", steamAppMessage);
    }
}
