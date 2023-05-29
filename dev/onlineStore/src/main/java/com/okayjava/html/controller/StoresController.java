package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StoresController {
    private Server server = Server.getInstance();

    @GetMapping("/Stores")
    public String getStoreAndProductsNames(Model model) {
        Response<?> response = server.getStoresName(); //linkedlist stores
        model.addAttribute("stores", response.getValue());
        return "Stores";
    }

    @RequestMapping(value = "/add-store-comment" , method = RequestMethod.POST)
    public String storeCommentAndRate(@RequestParam("store-comment") String comment,
                                      @RequestParam("store-rating") int rating,
                                      @RequestParam("") int storeID,
                                      Model model){

        Response<?> response = server.addStoreRateAndComment(storeID, rating, comment);
        if (response.isError()){
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        }
        return "Stores";
    }

    @RequestMapping(value = "/show-store-comment" , method = RequestMethod.POST)
    public String showStoreInfo(@RequestParam("") int storeID, Model model){
        Response<?> response = server.GetInformation(storeID);
        if (response.isError()){
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        }
        model.addAttribute("storeInfo", response.getValue()); //storeInfo is a String
        return "Stores";
    }
}
