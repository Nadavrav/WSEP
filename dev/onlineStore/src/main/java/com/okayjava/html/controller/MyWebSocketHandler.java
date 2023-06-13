//package com.okayjava.html.controller;
//
//import com.okayjava.html.CommunicateToServer.Server;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class MyWebSocketHandler extends TextWebSocketHandler {
//    private Server server;
//    private Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String userId = extractUserId(session);
//        userSessions.put(userId, session);
//    }
//
//    private String extractUserId(WebSocketSession session) {
//        return (String) session.getAttributes().get("username");
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//
//        // Call a function from myService to process the message payload
//        String processedPayload = server.processMessage(payload);
//
//        String responsePayload = "Received message: " + payload;
//        session.sendMessage(new TextMessage(responsePayload));
//    }
//
//    private void sendMessage(String userId, TextMessage message) {
//        WebSocketSession userSession = userSessions.get(userId);
//        if (userSession != null && userSession.isOpen()) {
//            try {
//                userSession.sendMessage(message);
//            } catch (IOException e) {
//                // Handle the exception accordingly
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        String userId = extractUserId(session);
//        userSessions.remove(userId);
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        // Handle transport-level errors here
//    }
//}
