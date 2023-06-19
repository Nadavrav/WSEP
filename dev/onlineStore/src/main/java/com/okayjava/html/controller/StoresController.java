package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StoresController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Stores")
    public String getStoreAndProductsNames(Model model) {
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<?> response = server.getStoresName(request); //linkedlist stores
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("stores", response.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();

            // Filter appointment requests based on the logged-in user
            Map<Integer, List<String>> filteredAppointmentRequests = new HashMap<>();
            for (Map.Entry<Integer, List<String>> entry : appointmentRequests.entrySet()) {
                List<String> owners = entry.getValue();
                if (owners.contains(server.getUsername(request))) {
                    filteredAppointmentRequests.put(entry.getKey(), owners);
                }
            }
            model.addAttribute("appointmentRequests", filteredAppointmentRequests);
            System.out.println("Appointment Requests: " + filteredAppointmentRequests);
        }
        alert.reset();
        return "Stores";
    }

    @RequestMapping(value = "/add-store-comment" , method = RequestMethod.POST)
    public String storeCommentAndRate(@RequestParam("store-comment") String comment,
                                      @RequestParam("store-rating") int rating,
                                      @RequestParam("storeID") int storeID,
                                      Model model){

//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<?> response = server.addStoreRateAndComment(request,storeID, rating, comment);
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("error");
        } else {
            alert.setSuccess(true);
            alert.setMessage("Your Comment & Rating is added to the store.");
            model.addAttribute("alert", alert.copy());
            System.out.println("adding comment: " + comment + " with rating: " + rating + " to storeid: " + storeID);
        }
        model.addAttribute("stores", server.getStoresName(request).getValue());
        alert.reset();
        return "Stores";
    }
}
