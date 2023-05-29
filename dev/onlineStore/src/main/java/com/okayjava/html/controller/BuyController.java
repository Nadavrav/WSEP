package com.okayjava.html.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class BuyController {
    @GetMapping("/Buy")
    public String searchResult(Model model) {
        return "Buy";
    }

    @PostMapping("/Buy")
    public String showPage(Model model) {
        return "Buy";
    }
}
