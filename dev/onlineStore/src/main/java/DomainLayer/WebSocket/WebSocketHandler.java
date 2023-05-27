package DomainLayer.WebSocket;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();
    private MessageSubject messageSubject = new MessageSubject();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload =(String) message.getPayload();
        messageSubject.notifyObservers(payload);

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void registerObserver(MessageObserver observer) {
        messageSubject.registerObserver(observer);
    }

    public void removeObserver(MessageObserver observer) {
        messageSubject.removeObserver(observer);
    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//    }
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        messageSubject.notifyObservers(payload);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//    }
}
