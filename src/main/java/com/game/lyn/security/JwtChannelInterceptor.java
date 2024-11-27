package com.game.lyn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) throws MessagingException {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    String username = jwt.getSubject();

                    // Lấy danh sách các quyền từ JWT nếu có
                    List<SimpleGrantedAuthority> authorities = jwt.getClaimAsStringList("roles").stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    accessor.setUser(authentication);
                } catch (Exception e) {
                    throw new MessagingException("Invalid JWT token", e);
                }
            } else {
                throw new MessagingException("Missing or invalid Authorization header");
            }
        }

        return message;
    }
}
