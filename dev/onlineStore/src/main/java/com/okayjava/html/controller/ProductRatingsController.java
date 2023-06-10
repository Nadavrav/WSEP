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

@Controller
public class ProductRatingsController {
    @Autowired
    private HttpServletRequest request;
    private Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();
    private int storeID;
    private int productID;

    @GetMapping("/ProductRatings")
    public String productsPage(Model model) {
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "CommentsAndRatings";
    }

    @GetMapping("/CommentsAndRatings")
    public String commentsAndRatings(@RequestParam("storeId") int storeId,
                                     @RequestParam("productId") int productId,
                                     Model model) {

        model.addAttribute("alert", alert.copy());
        alert.reset();

        Response<?> response = server.getProductRatingList(request,storeId, productId);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            model.addAttribute("ProductRatingList", response.getValue());
        }

        Response<?> response1 = server.GetStoreRate(request,storeId);
        if (response1.isError()) {
            alert.setFail(true);
            alert.setMessage(response1.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            model.addAttribute("StoreRate", response1.getValue());
        }

        this.storeID = storeId;
        this.productID = productId;
        model.addAttribute("storeId", storeId);
        model.addAttribute("productId", productId);
        model.addAttribute("alert", alert.copy());
        return "ProductRatings";
    }

    @PostMapping("/add-product-comment")
    public String addProductRateAndComment(@RequestParam("rating") int rate,
                                           @RequestParam("comment") String comment,
                                           Model model) {

        model.addAttribute("alert", alert.copy());
        alert.reset();

        Response<?> response = server.addProductRateAndComment(request,productID, storeID, rate, comment);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
        } else {
            alert.setSuccess(true);
            alert.setMessage("Comment & Rate Is Added!");

            // Update the ratings list
            Response<?> ratingsResponse = server.getProductRatingList(request,storeID, productID);
            if (!ratingsResponse.isError()) {
                model.addAttribute("ProductRatingList", ratingsResponse.getValue());
            }
        }

        model.addAttribute("alert", alert.copy());
        model.addAttribute("StoreRate", server.GetStoreRate(request,storeID).getValue());
        model.addAttribute("storeId", storeID);
        model.addAttribute("productId", productID);
        alert.reset();
        return "ProductRatings";
    }
}
