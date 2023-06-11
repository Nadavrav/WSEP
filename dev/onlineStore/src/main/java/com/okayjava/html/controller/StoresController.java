package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.ServicePolicy;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;

@Controller
public class StoresController {
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Stores")
    public String getStoreAndProductsNames(Model model) {
        model.addAttribute("logged", server.isLogged());
        model.addAttribute("Admin", server.isAdmin().getValue());
        alert.reset();
        Response<?> response = server.getStoresName(); //linkedlist stores
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("stores", response.getValue());
        }
        alert.reset();
        return "Stores";
    }

    @RequestMapping(value = "/add-store-comment" , method = RequestMethod.POST)
    public String storeCommentAndRate(@RequestParam("store-comment") String comment,
                                      @RequestParam("store-rating") int rating,
                                      @RequestParam("storeID") int storeID,
                                      Model model){

        alert.reset();
        Response<?> response = server.addStoreRateAndComment(storeID, rating, comment);
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
        model.addAttribute("stores", server.getStoresName().getValue());
        alert.reset();
        return "Stores";
    }

}
