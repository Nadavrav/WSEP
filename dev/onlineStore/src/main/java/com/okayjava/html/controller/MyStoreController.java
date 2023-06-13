package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceDiscount;
import ServiceLayer.ServiceObjects.ServicePolicy;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Controller
public class MyStoreController {
    Alert alert = Alert.getInstance();
//    private Server server = Server.getInstance();
    Server server = new Server();
    @GetMapping("/MyStore/{storeId}")
    public String myStoreMenu(Model model,
                              @PathVariable("storeId") int storeId,
                              @RequestParam("storeName") String storeName,
                              @RequestParam(value = "selectedDiscountIds", required = false) List<Integer> selectedDiscountIds,
                              @RequestParam(value = "selectedPolicyIds", required = false) List<Integer> selectedPolicyIds) {

        model.addAttribute("logged", server.isLogged());
        model.addAttribute("Admin", server.isAdmin().getValue());
        model.addAttribute("storeName", storeName);
        Response<Collection<ServiceDiscount>> responseDiscount = server.getStoreDiscountInfo(storeId);
        if (responseDiscount.isError()){
            alert.setFail(true);
            alert.setMessage(responseDiscount.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responseDiscount.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("discountInfo", responseDiscount.getValue());
        }

        Response<HashSet<ServicePolicy>> responsePolicy = server.getStorePolicy(storeId);
        if (responsePolicy.isError()){
            alert.setFail(true);
            alert.setMessage(responsePolicy.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responsePolicy.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("policyInfo", responsePolicy.getValue());
        }

//        if (selectedDiscountIds != null && !selectedDiscountIds.isEmpty()) {
//            server.addDiscount(selectedDiscountIds);
//        }

        alert.reset();
        return "MyStore";
    }

    @RequestMapping(value = "/discount_option", method = RequestMethod.POST)
    public String handleSelectedOptions(HttpServletRequest request) {
        String[] selectedOptions = request.getParameterValues("discountOption");

        if (selectedOptions != null) {
            for (String option : selectedOptions) {
                switch (option) {
                    case "option1":
//                        server.
                        break;
                    case "option2":
//                        server.
                        break;
                    case "option3":
//                        server.
                        break;
                    case "option4":
//                        server.
                        break;
                    case "option5":
//                        server.
                        break;
                    default:
                        break;
                }
            }
        }
        return "MyStore";
    }


}
