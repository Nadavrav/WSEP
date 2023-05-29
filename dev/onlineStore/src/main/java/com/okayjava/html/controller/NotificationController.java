package com.okayjava.html.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.management.Notification;

@Controller
public class NotificationController {

    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public Notification sendNotification(Notification notification) {
        // Handle the notification logic
        return notification;
    }
}
