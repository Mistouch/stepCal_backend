package com.UniProject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class CustomWebSocketEventHandler extends AbstractWebSocketHandler {
    private final Map<String,WebSocketSession> sessions;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        /*var principle=session.getPrincipal();
        if(principle==null || principle.getName()==null){
            session.close(CloseStatus.SERVER_ERROR.withReason("closed"));
            return;
        }*/
        System.out.println("connect");
        sessions.put(session.getId(),session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        /*var principle=session.getPrincipal();
        assert principle != null;*/
        System.out.println("closed");
        sessions.remove(session.getId());
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Handle incoming text message
        //Broadcast message to all user
        System.out.println("message");
        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }

    }
}
