package com.fiftyfive.rating.user.component;

import com.fiftyfive.rating.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String requestTokenHeader = request.getHeader("Authorization");

        try {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("bearer ")) {
                String jwtToken = requestTokenHeader.substring(7);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtTokenProvider.validToken(jwtToken)) {
                        UserDetails userDetails = userService.loadUserByUsername(jwtTokenProvider.getUserId(jwtToken));

                        Authentication authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }                }
            }
        } catch (Exception e) {
            throw new SecurityException("Invalid JWT token");
        }

        chain.doFilter(request, response);
    }
}
