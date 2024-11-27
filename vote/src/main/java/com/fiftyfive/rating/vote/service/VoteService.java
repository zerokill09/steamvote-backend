package com.fiftyfive.rating.vote.service;

import com.fiftyfive.rating.vote.domain.VoteEntity;
import com.fiftyfive.rating.vote.domain.VoteRepository;
import com.fiftyfive.rating.vote.dto.SteamAppMessage;
import com.fiftyfive.rating.vote.dto.VoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    public SteamAppMessage writeLike(Long userId, VoteDto voteDto) {
        Optional<VoteEntity> likeEntityOptional = voteRepository.findByUserIdAndAppId(userId, voteDto.getAppId());

        VoteEntity voteEntity;
        SteamAppMessage steamAppMessage = new SteamAppMessage();
        steamAppMessage.setAppId(voteDto.getAppId());

        if (likeEntityOptional.isPresent()) {
            voteEntity = likeEntityOptional.get();

             if(voteEntity.getLiked() == null) {
                 voteEntity.setLiked(voteDto.isLiked());
                 if(voteDto.isLiked()) {
                     steamAppMessage.setLiked(true);
                 } else {
                     steamAppMessage.setDisliked(true);
                 }
             } else if (voteDto.isLiked() != voteEntity.getLiked()) {
                voteEntity.setLiked(voteDto.isLiked());
                if(voteDto.isLiked()) {
                    steamAppMessage.setLiked(true);
                    steamAppMessage.setDisliked(false);
                } else {
                    steamAppMessage.setDisliked(true);
                    steamAppMessage.setLiked(false);
                }
            } else {
                voteEntity.setLiked(null);
                if(voteDto.isLiked()) {
                    steamAppMessage.setLiked(false);
                } else {
                    steamAppMessage.setDisliked(false);
                }
            }
        } else {
            voteEntity = new VoteEntity(userId, voteDto.getAppId(), voteDto.isLiked());
            if(voteDto.isLiked()) {
                steamAppMessage.setLiked(true);
            } else {
                steamAppMessage.setDisliked(true);
            }
        }
        voteRepository.save(voteEntity);

        return steamAppMessage;
    }
}
