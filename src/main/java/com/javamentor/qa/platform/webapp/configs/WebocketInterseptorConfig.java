package com.javamentor.qa.platform.webapp.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebocketInterseptorConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String header = accessor.getFirstNativeHeader(AUTHORIZATION);
                    if (header.startsWith("Bearer ")) {
                        String jwtToken = header.substring("Bearer ".length());
                        DecodedJWT jwt = JWT.require(Algorithm.HMAC256("PrinceNanadaime".getBytes())).build().verify(jwtToken);

                        Authentication authentication = new UsernamePasswordAuthenticationToken(jwt.getId(), jwt.getSubject());

                        if (!authentication.isAuthenticated()) {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                        accessor.setUser(authentication);
                    }
                    System.out.println("Юзер: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

                }
                return message;
            }
        });
    }
}
