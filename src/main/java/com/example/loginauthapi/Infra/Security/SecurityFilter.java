package com.example.loginauthapi.Infra.Security;

import com.example.loginauthapi.Repository.UserRepository;
import com.example.loginauthapi.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    //OncePerRequestFilter = Vai filtrar a cada request que chegar na api.

    @Autowired
        private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);
        if(login != null) {
            User user = this.userRepository.findByEmail(login)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            //SecurityContextHolder = Contexto de segurança do spring security
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }
        filterChain.doFilter(request, response);
    }
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
