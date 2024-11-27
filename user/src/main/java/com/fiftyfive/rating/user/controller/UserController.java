package com.fiftyfive.rating.user.controller;

import com.fiftyfive.rating.user.dto.UserDto;
import com.fiftyfive.rating.user.service.AuthService;
import com.fiftyfive.rating.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return authService.createUser(userDto);
    }

    @PostMapping("/sign-in")
    public String login(@RequestBody UserDto userDto) {
        return authService.login(userDto.getUserId(), userDto.getPwd());
    }
}
