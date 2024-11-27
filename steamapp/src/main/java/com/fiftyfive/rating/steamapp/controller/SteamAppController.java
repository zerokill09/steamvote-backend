package com.fiftyfive.rating.steamapp.controller;

import com.fiftyfive.rating.steamapp.dto.SteamAppDto;
import com.fiftyfive.rating.steamapp.dto.ListDto;
import com.fiftyfive.rating.steamapp.dto.ListReqDto;
import com.fiftyfive.rating.steamapp.service.SteamAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/steam-app")
@RequiredArgsConstructor
public class SteamAppController {
    private final SteamAppService steamAppService;

    @GetMapping("/list")
    public ListDto<SteamAppDto> getCatalogs(@RequestHeader(value = "X-User-Id", required = false) Long userId, @ModelAttribute ListReqDto listReqDto) throws ExecutionException, InterruptedException, TimeoutException {
        return steamAppService.getCatalogs(userId, listReqDto);
    }
}
