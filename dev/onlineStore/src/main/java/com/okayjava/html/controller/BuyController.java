package com.okayjava.html.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class BuyController {
    @GetMapping("/Buy")
    public String searchResult(Model model) {
        model.getAttribute("totalAmount");
        return "Buy";
    }
}
