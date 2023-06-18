package com.okayjava.html.controller;
import ServiceLayer.ServiceObjects.ServiceCart;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import DomainLayer.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class BagController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Bag")
    public String Bag(Model model) {
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<ServiceCart> response = server.getProductsInMyCart(request);
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            model.addAttribute("myCart", response.getValue().getBags()); //Set<ServiceBag>
            model.addAttribute("alert", alert.copy());
        }

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

            // Filter appointment requests based on the logged-in user
            Map<Integer, List<String>> filteredAppointmentRequests = new HashMap<>();
            for (Map.Entry<Integer, List<String>> entry : appointmentRequests.entrySet()) {
                List<String> owners = entry.getValue();
                if (owners.contains(server.getUsername())) {
                    filteredAppointmentRequests.put(entry.getKey(), owners);
                }
            }
            model.addAttribute("appointmentRequests", filteredAppointmentRequests);
            System.out.println("Appointment Requests: " + filteredAppointmentRequests);
        }
        alert.reset();
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
        alert.reset();
        return "redirect:/Bag";
    }

    @PostMapping("/updateAmount")
    @ResponseBody
    public ResponseEntity<String> updateAmount(@RequestParam("productId") int productId,
                                               @RequestParam("storeId") int storeId,
                                               @RequestParam("amount") int amount,
                                               Model model) {

        Response<?> response = server.changeCartProductQuantity(request, productId, storeId, amount);
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
            Response<Double> totalPriceResponse = server.getTotalPrice(request);
            if (!totalPriceResponse.isError()) {
                model.addAttribute("totalAmount", totalPriceResponse.getValue());
            }

            return ResponseEntity.ok("Amount updated successfully");
        }
    }
}
