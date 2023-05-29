package com.okayjava.html.controller;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import DomainLayer.Response;


@Controller
public class BagController {
    private Server server = Server.getInstance();

    @GetMapping("/Bag")
    public String Bag(Model model) {
        Response response = server.getProductsInMyCart();
        if (response.isError()){
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        }
        else model.addAttribute("myCart", response.getValue());
        return "Bag";
    }

    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam("index") int index, Model model) {

//        Response response = server.removeProductFromCart(productID, storeID);
//        if(response.isError()){
//            model.addAttribute("isError", true);
//            model.addAttribute("message", response.getMessage());
//        return "error";
//        }

        return "Bag";
    }
}
