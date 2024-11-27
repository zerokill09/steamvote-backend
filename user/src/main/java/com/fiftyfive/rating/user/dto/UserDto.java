package com.fiftyfive.rating.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String pwd;
    private String encryptedPwd;

    public UserDto(String userId) {
        this.userId = userId;
    }
}
