package DomainLayer.WebSocket;
import DomainLayer.Users.RegisteredUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public WebSocketHandler webSocketHandler() {
        WebSocketHandler webSocketHandler = new WebSocketHandler();
        // Register the observer
        webSocketHandler.registerObserver(realTimeMessageObserver());
        return webSocketHandler;
    }

    @Bean
    public RegisteredUser realTimeMessageObserver() {
        return new RealTimeMessageObserver();
    }
}
