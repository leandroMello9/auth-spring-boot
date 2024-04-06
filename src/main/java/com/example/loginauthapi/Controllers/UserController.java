package com.example.loginauthapi.Controllers;


import com.example.loginauthapi.Infra.Security.TokenService;
import com.example.loginauthapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("sucesso");

    }
}
