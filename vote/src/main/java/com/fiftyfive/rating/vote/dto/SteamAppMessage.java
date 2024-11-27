package com.fiftyfive.rating.vote.dto;

import lombok.Data;

@Data
public class SteamAppMessage {
    private Long appId;
    private Boolean liked;
    private Boolean disliked;
}
