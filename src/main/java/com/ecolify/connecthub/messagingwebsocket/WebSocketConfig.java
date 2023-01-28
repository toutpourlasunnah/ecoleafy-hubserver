package com.ecolify.connecthub.messagingwebsocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ModuleSocketHandler(), "/ws/module").setAllowedOrigins("*");

        registry.addHandler(new WebAppSocketHandler(), "/ws/app").setAllowedOrigins("*");
        registry.addHandler(new WebAppSocketHandler(), "/ws/app").setAllowedOrigins("*").withSockJS();;
        //registry.addHandler(new SomeOtherSocketHandler(), "/ws/someroute2");
    }


}