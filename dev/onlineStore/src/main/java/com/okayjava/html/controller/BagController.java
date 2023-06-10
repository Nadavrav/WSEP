package com.okayjava.html.controller;
import ServiceLayer.ServiceObjects.ServiceCart;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import DomainLayer.Response;

import javax.servlet.http.HttpServletRequest;


@Controller
public class BagController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Bag")
    public String Bag(Model model) {
        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<ServiceCart> response = server.getProductsInMyCart(request);
        System.out.println("size: " + response.getValue().getBags().size());
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("myCart", response.getValue().getBags()); //Set<ServiceBag>
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();

//        Response<ServiceCart> responseT = server.; for total amount
//        if (responseT.isError()){
//            alert.setFail(true);
//            alert.setMessage(responseT.getMessage());
//            model.addAttribute("alert", alert.copy());
//        }
//        else{
//            model.addAttribute("totalAmount", responseT.getValue());
//            model.addAttribute("alert", alert.copy());
//        }
        return "Bag";
    }

    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam("storeId") int storeId,
                                 @RequestParam("productId") int productId,
                                 Model model) {

        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<?> response = server.removeProductFromCart(request,productId, storeId);
        if(response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            System.out.println("productId: " + productId + " was removed!");
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
        }

        return "Bag";
    }

    @PostMapping("/updateAmount")
    @ResponseBody
    public ResponseEntity<String> updateAmount(@RequestParam("productId") int productId,
                                               @RequestParam("storeId") int storeId,
                                               @RequestParam("amount") int amount) {

        System.out.println("here!");
        server.changeCartProductQuantity(request,productId, storeId, amount);
        // Return a success response
        return ResponseEntity.ok("Amount updated successfully");
    }
}
