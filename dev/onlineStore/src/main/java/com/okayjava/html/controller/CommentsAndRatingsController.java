package com.okayjava.html.controller;

import DomainLayer.Response;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentsAndRatingsController {
    private Server server = Server.getInstance();
    public int storeID, productID;

    @GetMapping("/CommentsAndRatings")
    public String commentsAndRatingsPage(@RequestParam("storeId") int storeId, @RequestParam("productId") int productId, Model model) {

        System.out.println("storeID: " + storeId + "productID: " + productId );

        Response<?> response = server.getProductRatingList(storeId, productId);
        if(response.isError()){
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
        }
        model.addAttribute("ProductRatingList", response.getValue()); //HashMap<String, String>

        Response<?> response1 = server.GetStoreRate(storeId);
        if(response1.isError()){
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
        }
        model.addAttribute("StoreRate", response1.getValue()); //Double

        // Add the storeId and productId to the model
        this.storeID = storeId;
        this.productID = productId;
        model.addAttribute("storeId", storeId);
        model.addAttribute("productId", productId);
        return "CommentsAndRatings";
    }

    @PostMapping("/add-product-comment")
    public String addProductRateAndComment(@RequestParam("rating") int rate,
                                           @RequestParam("comment") String comment,
                                           Model model) {

        Response<?> response = server.addProductRateAndComment(this.productID, this.storeID, rate, comment);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        }
        else model.addAttribute("message", response.getMessage());
        return "CommentsAndRatings";
    }
}
