package edu.induma.configuration;

import edu.induma.handler.AppWebSocketHandler;
import edu.induma.handler.BinWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {


    final private BinWebSocketHandler binWebSocketHandler;
    final private AppWebSocketHandler appWebSocketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(binWebSocketHandler, "/bin").setAllowedOrigins("*");
        registry.addHandler(appWebSocketHandler, "/app").setAllowedOrigins("*");
    }
}
