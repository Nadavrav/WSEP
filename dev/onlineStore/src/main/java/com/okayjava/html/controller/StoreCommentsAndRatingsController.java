package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Stores.Store;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
public class StoreCommentsAndRatingsController {
    private Service server = new Service();

    @GetMapping("/StoreCommentsAndRatings")
    public String getStoreNames(Model model) {
        Response response = server.getStoresName(); //from service
        LinkedList<Store> stores = (LinkedList<Store>) response.getValue();

        model.addAttribute("stores", stores);
        return "StoreCommentsAndRatings";
    }

}
