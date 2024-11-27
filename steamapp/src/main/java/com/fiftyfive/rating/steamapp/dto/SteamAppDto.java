package com.fiftyfive.rating.steamapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SteamAppDto {
    private Long appId;
    private String name;
    private String thumbnail;
    private Integer liked;
    private Integer disliked;
    private Boolean isLiked;
}
