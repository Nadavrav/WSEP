package com.okayjava.html.controller;

import ServiceLayer.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import DomainLayer.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class BagController {
    private Service server = new Service();

    @GetMapping("/Bag")
    public String Bag() {
        return "Bag";
    }

    @PostMapping("/Bag")
    public String myBag() {
        return "Bag";
    }

//    @PostMapping("/remove-product")
//    public void removeProduct(@RequestParam("") Model model){
//        Response response = server.removeProductFromCart(productID, storeID);
//        if(response.isError()){
//            model.addAttribute("message", response.isError());
//        }
//    }

    @PostMapping("/remove-product-from-cart")
    public String removeProductFromCart(@RequestParam("productId") int productID,
                                        Model model) {
//        Response response = server.removeProductFromCart(productID, storeID);
//        if(response.isError()){
//            model.addAttribute("isError", true);
//            model.addAttribute("message", response.getMessage());
//        }
        List<Object[]> cartList = (List<Object[]>) model.getAttribute("cartList");
        for(Object[] obj : cartList){
            if(obj[3].equals(productID)){
                cartList.remove(obj);
            }
        }
        model.addAttribute("cartList", cartList);
        return "/Bag";
    }
}
