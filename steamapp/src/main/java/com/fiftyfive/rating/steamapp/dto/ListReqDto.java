package com.fiftyfive.rating.steamapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListReqDto {
    private int page;
    private int size;

    public int getPage() {
        return this.page - 1;
    }
}
