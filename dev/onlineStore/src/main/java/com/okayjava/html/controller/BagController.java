package com.okayjava.html.controller;

import ServiceLayer.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import DomainLayer.Response;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class BagController {
    private Service server = new Service();

    @GetMapping("/Bag")
    public String Bag() {
        return "Bag";
    }

//    @PostMapping("/remove-product")
//    public void removeProduct(Model model){
//        Response response = server.removeProductFromCart(productID, storeID);
//        if(response.isError()){
//            model.addAttribute("message", response.isError());
//        }
//    }
}
