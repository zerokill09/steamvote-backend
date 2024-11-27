package com.fiftyfive.rating.steamapp.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListDto<T> {
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean first;
    private boolean last;
    private List<T> content = new ArrayList<>();
}
