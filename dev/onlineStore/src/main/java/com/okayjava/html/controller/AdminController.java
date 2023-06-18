package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();

    @GetMapping("Admin")
    public String adminPage(Model model) {
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();

        Response<List<ServiceUser>> response = server.getOnlineUsers(request);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("onlineUsersInfo", response.getValue());
        }

        Response<List<ServiceUser>> responseOff = server.getOfflineUsers(request);
        if (responseOff.isError()) {
            alert.setFail(true);
            alert.setMessage(responseOff.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responseOff.getMessage());
            model.addAttribute("offlineUsersInfo", responseOff.getValue());
        }

        return "Admin";
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("usernameToDelete") String username,
                             Model model) {

        Response<?> response = server.deleteUser(request,username);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(username + " Is Deleted!");
            model.addAttribute("alert", alert.copy());
        }

        return "Admin";
    }

    @RequestMapping(value = "/notification-history", method = RequestMethod.POST)
    public String showNotifications(Model model) {
        alert.reset();
        return "/Admin";
    }
}
