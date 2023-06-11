package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedList;

@Controller
public class Messages {

    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Messages")
    public String messages(Model model) throws Exception {
        model.addAttribute("logged", server.isLogged());
        model.addAttribute("Admin", server.isAdmin().getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<LinkedList<String>> response = server.getNewMessages();
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("messages", response.getValue()); //LinkedList<String>
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "Messages";
    }
}
