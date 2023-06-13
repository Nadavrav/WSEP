package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BuyController {
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();
//    Server server = new Server();
    @GetMapping("/Buy")
    public String purchase(Model model) {
        model.addAttribute("logged", server.isLogged());
        model.addAttribute("Admin", server.isAdmin().getValue());
        model.addAttribute("alert", alert.copy());
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
        return "Buy";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    public String purchase(@RequestParam("cardNumber") int cardNumber,
//                           @RequestParam("cardholderName") String cardholderName,
//                           @RequestParam("expirationMonth") int expirationMonth,
//                           @RequestParam("expirationYear") int expirationYear,
                           @RequestParam("address") String address,
//                           @RequestParam("cvv") String cvv,
                           Model model) {


//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<List<String>> response = server.PurchaseCart(cardNumber, address);
        if (response.isError()){
            System.out.println("error in buying: " + response.getMessage());
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            alert.setSuccess(true);
            alert.setMessage("Thank You ;)");
            model.addAttribute("buy", response.getValue()); //List<String>
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "Buy";
    }
}
