package com.javamentor.qa.platform.webapp.controllers.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final UserService userService;

    public AuthenticationResourceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, String>> currentUser(@AuthenticationPrincipal User user) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("email", user.getEmail());
        data.put("password", user.getPassword());
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/auth/token")
    public ResponseEntity<String> token(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 hours = 1 day
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC256("PrinceNanadaime".getBytes())));
    }
}
