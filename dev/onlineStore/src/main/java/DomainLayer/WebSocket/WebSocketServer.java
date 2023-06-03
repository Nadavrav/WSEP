package DomainLayer.WebSocket;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message from client: " + message);
        String response = "Server received: " + message;
        try {
            session.getBasicRemote().sendText(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Client closed connection: " + session.getId());
    }
}
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
