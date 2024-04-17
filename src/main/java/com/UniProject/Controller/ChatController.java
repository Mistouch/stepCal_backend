package com.UniProject.Controller;

import com.UniProject.DTO.ChatMessageDto;
import com.UniProject.DTO.DtoImpl;
import com.UniProject.DTO.UserDto;
import com.UniProject.Services.ChatService;
import com.UniProject.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final Map<String, WebSocketSession> sessions;

    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;
    @Autowired
    DtoImpl dto;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/send-message")
    public void sendMessageToAll(@RequestBody ChatMessageDto message,HttpServletRequest request) throws JsonProcessingException {
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request Headers: " + Collections.list(request.getHeaderNames()));


        String email= (String) request.getAttribute("email");
        UserDto user=userService.getUser(email);
        message.setSender(user.getFirst_name());

        String jsonMessage=objectMapper.writeValueAsString(message);
        System.out.println(jsonMessage);
        chatService.saveMessage(message);
        //System.out.println(user.getFirst_name());

        TextMessage textMessage = new TextMessage(jsonMessage);
        System.out.println(textMessage.getPayload());
        for (WebSocketSession session : sessions.values()) {
            try {
                session.sendMessage(textMessage);
            } catch (Exception e) {
                // Handle exception if sending message fails for a session
                e.printStackTrace();
            }
        }
    }
    @GetMapping("/get-messages")
    public ResponseEntity<List<ChatMessageDto>>showAllMessage(){
        List<ChatMessageDto>messages=chatService.getAllMessages();

        return ResponseEntity.ok(messages);
    }
}
