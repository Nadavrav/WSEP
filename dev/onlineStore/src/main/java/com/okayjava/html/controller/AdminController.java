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
        Response<List<ServiceUser>> response = server.getRegisteredUsersInfo();
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error");
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("usersInfo", response.getValue());
            System.out.println("usersInfo size: " + response.getValue().size());
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
