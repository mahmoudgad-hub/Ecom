package com.example.ecom.auth.filter;


import com.example.ecom.auth.CurrentUser;
import com.example.ecom.auth.CustomUserDetails;
import com.example.ecom.auth.config.JwtUtil;
import com.example.ecom.service.CustomUserDetailsService;
import com.example.ecom.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

            CurrentUser.setUserId(jwtUtil.extractUserId(jwt));
            username = jwtUtil.extractUsername(jwt);
            //  try {
                // ✅ استخرج اليوزر مرة واحدة

                if (jwtUtil.validateToken(jwt, username)) {
                    /*
                      if (tokenBlacklistService.isBlacklisted(jwt)) {
                        throw new RuntimeException("Token is revoked");
                    }
                } else {
                    throw new RuntimeException("Token is invalid");
                }*/
                    if (!tokenBlacklistService.isBlacklisted(jwt)) {
                    throw new RuntimeException("Token is invalid");
                        }

//            } catch (Exception e) {
//                throw new RuntimeException("Invalid Token", e);
           }


            //    username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername(username);
            System.out.println("Authorities: " + customUserDetails.getAuthorities());
            System.out.println("Authenticated user: " + customUserDetails.getUsername());
            System.out.println("UserId :" + customUserDetails.getUserId());

            if (jwtUtil.validateToken(jwt, customUserDetails.getUsername())) {
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }


        filterChain.doFilter(request, response);
    }
}

