package com.javamentor.qa.platform.webapp.controllers.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.controllers.dto.AuthenticationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthenticationResourceController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResourceController(UserService userService, PasswordEncoder encoder,
                                            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.userService = userService;
    }

    /*public boolean verifyPassword(String requestPassword, String userPassword) {
        if (!encoder.matches(requestPassword, userPassword) && !encoder.matches(userPassword, requestPassword)) {
            log.error("Wrong Password!");
            return false;
        } else return true;
    }*/

    @GetMapping("/getall")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, String>> currentUser(@RequestBody AuthenticationRequest request) {
        Map<String, String> data = new LinkedHashMap<>();
        Optional<User> optionalUser = userService.getByEmail(request.getEmail());
        User user = optionalUser.orElseGet(optionalUser::orElseThrow);
        data.put("email", user.getEmail());
        data.put("password", user.getPassword());
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/auth/token")
    public String token(@RequestBody AuthenticationRequest request) throws NullPointerException {
        String email = request.getEmail();
        log.info("Email is " + email);

        User user = userService.getByEmail(email).get();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                email, user.getPassword(), user.getAuthorities()
        );
        authenticationManager.authenticate(token);
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 hours = 1 day
                .withClaim("link", user.getLinkSite())
                .sign(Algorithm.HMAC256("PrinceNanadaime".getBytes()));
    }
}
