package com.okayjava.html.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.management.Notification;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private HttpServletRequest request;
    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public Notification sendNotification(Notification notification) {
        // Handle the notification logic
        return notification;
    }
}
