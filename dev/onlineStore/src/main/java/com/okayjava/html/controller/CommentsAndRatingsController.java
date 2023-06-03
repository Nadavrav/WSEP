package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentsAndRatingsController {
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();
    public int storeID, productID;

    @GetMapping("/CommentsAndRatings")
    public String commentsAndRatingsPage(@RequestParam("storeId") int storeId, @RequestParam("productId") int productId, Model model) {
        System.out.println(storeId);
        System.out.println(productId);
        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<?> response = server.getProductRatingList(storeId, productId);
        if(response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
//            return "redirect:/CommentsAndRatings";
        }
        model.addAttribute("ProductRatingList", response.getValue()); //HashMap<String, String>

        Response<?> response1 = server.GetStoreRate(storeId);
        if(response1.isError()){
            alert.setFail(true);
            alert.setMessage(response1.getMessage());
            model.addAttribute("alert", alert.copy());
//            return "redirect:/CommentsAndRatings";
        }
        model.addAttribute("StoreRate", response1.getValue()); //Double

        // Add the storeId and productId to the model
        this.storeID = storeId;
        this.productID = productId;
        model.addAttribute("storeId", storeId);
        model.addAttribute("productId", productId);
        model.addAttribute("alert", alert.copy());
        return "CommentsAndRatings";
    }

    @PostMapping("/add-product-comment")
    public String addProductRateAndComment(@RequestParam("rating") int rate,
                                           @RequestParam("comment") String comment,
                                           Model model) {

        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<?> response = server.addProductRateAndComment(productID, storeID, rate, comment);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
        }
        else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
        }
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "CommentsAndRatings";
    }
}
