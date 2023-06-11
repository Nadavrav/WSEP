package com.okayjava.html.controller;
import ServiceLayer.ServiceObjects.ServiceBag;
import ServiceLayer.ServiceObjects.ServiceCart;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import DomainLayer.Response;
import org.thymeleaf.model.IModel;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Set;


@Controller
public class BagController {
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Bag")
    public String Bag(HttpServletRequest request, Model model) {
        model.addAttribute("logged", server.isLogged());
        model.addAttribute("Admin", server.isAdmin().getValue());
//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<ServiceCart> response = server.getProductsInMyCart();
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("myCart", response.getValue().getBags()); //Set<ServiceBag>
            model.addAttribute("alert", alert.copy());
        }

        Response<Double> responseT = server.getTotalPrice();
        if (responseT.isError()){
            alert.setFail(true);
            alert.setMessage(responseT.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("totalAmount", responseT.getValue());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "Bag";
    }

    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam("storeId") int storeId,
                                 @RequestParam("productId") int productId,
                                 Model model) {

        Response<?> response = server.removeProductFromCart(productId, storeId);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
        } else {
            System.out.println("productId: " + productId + " was removed!");
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
        }
        alert.reset();
        return "redirect:/Bag"; // Redirect to the Bag page to display the updated cart
    }


    @PostMapping("/updateAmount")
    @ResponseBody
    public ResponseEntity<String> updateAmount(@RequestParam("productId") int productId,
                                               @RequestParam("storeId") int storeId,
                                               @RequestParam("amount") int amount,
                                               Model model) {

        Response<?> response = server.changeCartProductQuantity(productId, storeId, amount);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            alert.reset();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        } else {
            System.out.println("updating..");
            alert.setSuccess(true);
            alert.setMessage("Amount changed to: " + amount);
            model.addAttribute("alert", alert.copy());
            alert.reset();

            // Update total price
            Response<Double> totalPriceResponse = server.getTotalPrice();
            if (!totalPriceResponse.isError()) {
                model.addAttribute("totalAmount", totalPriceResponse.getValue());
            }

            return ResponseEntity.ok("Amount updated successfully");
        }
    }

}
