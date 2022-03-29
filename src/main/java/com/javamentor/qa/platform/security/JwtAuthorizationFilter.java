package com.javamentor.qa.platform.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException, NullPointerException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) filterChain.doFilter(request, response);
        else if (authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring("Bearer ".length());
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256("PrinceNanadaime".getBytes())).build().verify(jwtToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(jwt.getId(), jwt.getSubject());
            if (!authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new LinkedHashMap<>();
            error.put("timestamp", String.valueOf(new Date().getTime()));
            error.put("status", "Forbidden");
            error.put("message", "Forbidden");
            error.put("path", request.getServletPath());
            error.put("Generate token to get access: ", "/api/auth/token");
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}