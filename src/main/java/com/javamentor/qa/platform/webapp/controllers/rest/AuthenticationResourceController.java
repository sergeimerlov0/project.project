package com.javamentor.qa.platform.webapp.controllers.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.controllers.dto.AuthenticationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResourceController(UserService userService, AuthenticationManager authenticationManager,
                                            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/getall")
    public List<User> users() {
        return userService.getAll();
    }

    @PostMapping("/auth/token")
    public String token(@RequestBody AuthenticationRequest request) throws NullPointerException,
            AuthenticationException {
        String email = request.getEmail();
        String password = request.getPassword();
        log.info("Email is " + email);

        Optional<User> userOptional = userService.getByEmail(email);
        User user = userOptional.orElseGet(userOptional::orElseThrow);

        if (!passwordEncoder.matches(password, user.getPassword()) && !passwordEncoder.matches(
                user.getPassword(), password)) throw new AuthenticationException("Wrong password!");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                email, user.getPassword(), user.getAuthorities());

        if (!token.isAuthenticated()) authenticationManager.authenticate(token);
        return JWT.create().withSubject(user.getEmail()).sign(Algorithm.HMAC256("PrinceNanadaime".getBytes()));
    }
}
