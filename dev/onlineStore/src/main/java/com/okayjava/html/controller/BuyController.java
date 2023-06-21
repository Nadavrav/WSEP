package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BuyController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();
    @GetMapping("/Buy")
    public String purchase(Model model) throws Exception {
        model.addAttribute("alert", alert.copy());
        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("alert", alert.copy());
        Response<Double> responseT = server.getTotalPrice(request);
        if (responseT.isError()){
            alert.setFail(true);
            alert.setMessage(responseT.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("totalAmount", responseT.getValue());
            model.addAttribute("alert", alert.copy());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }
        alert.reset();
        return "Buy";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)//to update
    public String purchase(@RequestParam("cardNumber") String cardNumber,
                          @RequestParam("cardholderName") String cardholderName,
                           @RequestParam("expirationMonth") int expirationMonth,
                           @RequestParam("expirationYear") int expirationYear,
                           @RequestParam("address") String address,
                           @RequestParam("cvv") int cvv,
                           Model model) throws Exception {

//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<List<String>> response = server.PurchaseCart(request,cardholderName,cardNumber,expirationMonth+"/"+expirationYear,cvv,"206469017", address,"Nazareth","israel","1613101");
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
            model.addAttribute("purchaseSuccessful", true);
            System.out.println("successful buy");
        }
        alert.reset();
        return "Buy";
    }
}
