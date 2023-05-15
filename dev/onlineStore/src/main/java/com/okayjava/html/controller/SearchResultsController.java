package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Stores.Products.Product;
import ServiceLayer.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class SearchResultsController {
    private Service server = new Service();
    double totalAmount = 0.0;
    List<Product> products = new ArrayList<>();
//    List<String> cartList = new ArrayList<>();
    List<Object[]> cartList = new ArrayList<>(); //productName & price & quantity

    @GetMapping("/SearchResults")
    public String searchResult(Model model) {

        model.addAttribute("products", products);
        return "SearchResults";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") int productId,
                          Model model){

        List<Object[]> productList = (List<Object[]>) model.getAttribute("productList");

        Object[] selectedProduct = productList.get(productId);

//        Response response = server.addProductToCart();
//        if(response.isError()){
//            model.addAttribute("isError", true);
//            model.addAttribute("message", response.getMessage());
//        }
        Object[] obj = new Object[3];
        obj[0] = selectedProduct[1]; //product name
        obj[1] = selectedProduct[2]; //price
        obj[2] = selectedProduct[5]; //quantity
        cartList.add(obj);

        for (Object[] product : cartList) {
            totalAmount += Double.parseDouble(product[1].toString());
        }
        model.addAttribute("cartList", cartList);
        model.addAttribute("totalAmount", totalAmount);
        return "SearchResults";
    }

    @PostMapping("/buy-now")
    public String buyNow(@RequestParam("productId") int productId, Model model){
        List<Object[]> productList = (List<Object[]>) model.getAttribute("productList");
        Object[] selectedProduct = productList.get(productId);
//        Response response = server.PurchaseCart();//for buying a product selected in the table
        return "Buy";
    }
}
