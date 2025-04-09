package edu.induma.handler;

import edu.induma.service.impl.VideoStreamService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppWebSocketHandler extends TextWebSocketHandler {
    private final BinWebSocketHandler binWebSocketHandler;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected to websocket session " + session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("Disconnected from websocket session " + session.getId());
        sessions.remove(session);
    }

    public void broadcast(String base64Image) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(base64Image));
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JSONObject json = new JSONObject(payload);

        if (json.getString("type").equals("image")) {
            String chunk = json.getString("chunk");
            boolean isLast = json.getBoolean("last");

            // Reassemble chunks
            if (!isLast) {
                // Append chunk to buffer
                String existingBuffer = (String) session.getAttributes().get("imageBuffer");
                if (existingBuffer == null) {
                    session.getAttributes().put("imageBuffer", chunk);
                } else {
                    session.getAttributes().put("imageBuffer", existingBuffer + chunk);
                }
            } else {
                // Final chunk, process full image
                String existingBuffer = (String) session.getAttributes().get("imageBuffer");
                if (existingBuffer != null) {
                    String fullImage = existingBuffer + chunk;
                    session.getAttributes().remove("imageBuffer"); // Clean up
                    binWebSocketHandler.sendMessageToAll(fullImage);
                    // Process full image here
          System.out.println(fullImage);

                    // Example: Save image to file asynchronously to avoid blocking
                    new Thread(() -> {
                        byte[] decodedBytes = java.util.Base64.getDecoder().decode(fullImage);
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream("output.jpg")) {
                            fos.write(decodedBytes);
                        } catch (IOException e) {
                            System.err.println("Error saving image: " + e.getMessage());
                        }
                    }).start();
                } else {
                    // Handle case where no buffer exists (e.g., first chunk was lost)
                    System.out.println("Error: No image buffer found for final chunk.");
                }
            }
        }
    }

    public void sendMessageToAll(String message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (Exception e) {
                        e.printStackTrace(); // you can log this better in production
                    }
                }
            }
        }
    }
}
