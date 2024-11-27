package com.fiftyfive.rating.vote.dto;

import lombok.Data;

@Data
public class VoteDto {
    private Long appId;
    private boolean liked;

    public VoteDto(Long appId, boolean liked) {
        this.appId = appId;
        this.liked = liked;
    }
}
