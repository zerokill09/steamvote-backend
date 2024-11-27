package com.fiftyfive.rating.user.service;

import com.fiftyfive.rating.user.component.JwtTokenProvider;
import com.fiftyfive.rating.user.domain.UserEntity;
import com.fiftyfive.rating.user.domain.UserRepository;
import com.fiftyfive.rating.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String userId, String password) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return issueTokens(userEntity, authentication);
    }

    public String issueTokens(UserEntity user, Authentication authentication) {
        return "bearer " + jwtTokenProvider.generateAccessToken(user, authentication);
    }

    public UserDto createUser(UserDto userDto) {
        Optional<UserEntity> existUser = userRepository.findByUserId(userDto.getUserId());

        if(existUser.isPresent()) {
            throw new RuntimeException("이미 존재하는 Id 입니다.");
        }

        userDto.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        UserEntity userEntity = new UserEntity(userDto.getUserId(), userDto.getEncryptedPwd());
        userRepository.save(userEntity);

        return new UserDto(userEntity.getUserId());
    }
}
