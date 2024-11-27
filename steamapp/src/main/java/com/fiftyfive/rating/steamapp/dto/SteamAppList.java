package com.fiftyfive.rating.steamapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SteamAppList {
    AppList applist;

    @Getter @Setter @NoArgsConstructor
    public static class AppList {
        List<SteamApp> apps;
    }

    @Getter @Setter @NoArgsConstructor
    public static class SteamApp {
        Long appid;
        String name;
    }
}
