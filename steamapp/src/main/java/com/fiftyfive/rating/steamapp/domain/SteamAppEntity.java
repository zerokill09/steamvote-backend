package com.fiftyfive.rating.steamapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name="steamapps")
@NoArgsConstructor
public class SteamAppEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long appId;
    private String name;
    private String thumbnail;
    @Setter
    private Integer liked;
    @Setter
    private Integer disliked;

    public SteamAppEntity(Long appId, String name, String thumbnail) {
        this.appId = appId;
        this.name = name;
        this.thumbnail = thumbnail;
    }
}
