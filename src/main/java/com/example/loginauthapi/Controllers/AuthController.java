package com.example.loginauthapi.Controllers;

import com.example.loginauthapi.Dtos.LoginBodyDto;
import com.example.loginauthapi.Dtos.RegisterRequestDto;
import com.example.loginauthapi.Dtos.ResponseDto;
import com.example.loginauthapi.Infra.Security.TokenService;
import com.example.loginauthapi.Repository.UserRepository;
import com.example.loginauthapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginBodyDto body) throws Exception {
        User user = this.userRepository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generatedToken(user);
            return ResponseEntity.ok(new ResponseDto(user.getName(), token));

        }
        return ResponseEntity.badRequest().build();

    }
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDto body) throws Exception{
        Optional<User> user = this.userRepository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.userRepository.save(newUser);

            String token = this.tokenService.generatedToken(newUser);
            return ResponseEntity.ok(new ResponseDto(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}