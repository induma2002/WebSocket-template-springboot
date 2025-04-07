package edu.induma.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.induma.model.User;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class BinWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received from /app: " + payload);

        try {
            boolean value = Boolean.parseBoolean(payload);
            System.out.println("Parsed boolean value: " + value);

            // You can add logic here to handle true/false
            if (value) {
                session.sendMessage(new TextMessage("Received: true"));
            } else {
                session.sendMessage(new TextMessage("Received: false"));
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("Invalid boolean value received."));
        }
    }

    public void sendMessageToAll(User message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            synchronized (sessions) {
                for (WebSocketSession session : sessions) {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(json));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
