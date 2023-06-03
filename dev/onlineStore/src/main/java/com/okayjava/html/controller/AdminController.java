package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();

    @GetMapping("Admin")
    public String adminPage(Model model) {
        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<List<ServiceUser>> response = server.getOnlineUsers();
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error");
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("onlineUsersInfo", response.getValue());
            System.out.println("onlineUsersInfo size: " + response.getValue().size());
        }

        alert.reset();

        Response<List<ServiceUser>> responseOff = server.getOfflineUsers();
        if (responseOff.isError()) {
            alert.setFail(true);
            alert.setMessage(responseOff.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error");
        } else {
            alert.setSuccess(true);
            alert.setMessage(responseOff.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("offlineUsersInfo", responseOff.getValue());
            System.out.println("usersInfo size: " + responseOff.getValue().size());
        }
        alert.reset();
        return "Admin";
    }

    @RequestMapping(value = "/notification-history", method = RequestMethod.POST)
    public String showNotifications(Model model) {
        alert.reset();
        return "/Admin";
    }
}
