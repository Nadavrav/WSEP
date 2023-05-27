package DomainLayer.WebSocket;

import java.util.ArrayList;
import java.util.List;

public class MessageSubject {
    private List<MessageObserver> observers = new ArrayList<>();

    public void registerObserver(MessageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (MessageObserver observer : observers) {
            observer.updateMessage(message);
        }
    }
}

