package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class Messages {
    @Autowired
    private HttpServletRequest request;

    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Messages")
    public String messages(Model model) throws Exception {
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<LinkedList<String>> response = server.getNewMessages(request);
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("messages", response.getValue()); //LinkedList<String>
            model.addAttribute("alert", alert.copy());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }
        alert.reset();
        return "Messages";
    }
}
