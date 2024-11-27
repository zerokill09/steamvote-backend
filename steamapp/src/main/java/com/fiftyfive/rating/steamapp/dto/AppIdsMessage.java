package com.fiftyfive.rating.steamapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AppIdsMessage {
    private Long userId;
    private List<Long> appIds;
}
