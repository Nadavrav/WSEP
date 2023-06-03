package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StoresController {
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Stores")
    public String getStoreAndProductsNames(Model model) {
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

        Response<?> response = server.addStoreRateAndComment(storeID, rating, comment);
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("error");
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("adding comment: " + comment + " with rating: " + rating + " to storeid: " + storeID);
        }
        alert.reset();
        return "Stores";
    }

    @RequestMapping(value = "/add-store-discount" , method = RequestMethod.POST)
    public String storeDiscount(@RequestParam("store-discount") String discount,
                                @RequestParam("storeID") int storeID,
                                Model model){

//        Response<?> response = server.addDiscount(storeID, discount);
//        if (response.isError()){
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//            System.out.println("error");
//        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
////            model.addAttribute("alert", alert.copy());
//            System.out.println("adding discount: " + discount + " to storeid: " + storeID);
//        }
//        alert.reset();
        return "Stores";
    }
}
