package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Users.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ServiceLayer.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static DomainLayer.Users.Permission.*;

@Controller
public class OwnerManagerMenuController {
    private Service server = new Service();
    private HashMap<String, List<Permission>> gotPermission = new HashMap<>();
    LinkedList<Permission> linkPermission = new LinkedList<>();
    @GetMapping("/OwnerManagerMenu")
    public String OwnerManagerMenu() {
        return "OwnerManagerMenu";
    }

    @RequestMapping(value = "/add-product", method = RequestMethod.POST)
    public String addProduct(@RequestParam("storeID-add") int storeID,
                             @RequestParam("product-name") String productName,
                             @RequestParam("product-price") double productPrice,
                             @RequestParam("product-category") String category,
                             @RequestParam("product-quantity") int quantity,
                             @RequestParam("product-desc") String desc,
                             Model model) {

//        if (model.getAttribute("owner").equals(true)) {
            //functionality to add a product
            Response response = server.AddProduct(storeID, productName, productPrice, category, quantity, desc);
            if(response.isError()){
                model.addAttribute("isError", true);
                model.addAttribute("message", response.getMessage());
                return "error";
            }

        else {
                model.addAttribute("message", "You're Not Authorized!");
                return "OwnerManagerMenu";
//                return "error";
            }
    }

    @RequestMapping(value = "/remove-product", method = RequestMethod.POST)
    public String removeProduct(@RequestParam("storeID_remove") int storeID,
                                @RequestParam("product-id") int productID,
                                Model model) {

        if (model.getAttribute("owner").equals(true)) {
            //functionality to remove a product
            Response response = server.RemoveProduct(productID, storeID);
            if(response.isError()){
                model.addAttribute("isError", true);
                model.addAttribute("message", response.getMessage());
                return "error";
            }
            return "OwnerManagerMenu";
        } else {
            model.addAttribute("message", "You're Not Authorized!");
            return "error";
        }
    }
    @RequestMapping(value = "/update-product", method = RequestMethod.POST)
    public String updateProduct(@RequestParam("storeID_update") int storeID,
                                @RequestParam("productID_update") int productID,
                                @RequestParam("productname") String productName,
                                @RequestParam("price") double price,
                                @RequestParam("quantity") int quantity,
                                @RequestParam("category") String category,
                                @RequestParam("description") String description,
                                Model model) {

        if (model.getAttribute("owner").equals(true)) {
            //functionality to update a product
            if(productName != null){
                Response response = server.UpdateProductName(productID, storeID, productName);
                if(response.isError()){
                    model.addAttribute("isError", true);
                    model.addAttribute("message", response.getMessage());
                    return "error";
                }
            }
            if(price > 0){
                Response response = server.UpdateProductPrice(productID, storeID, price);
                if(response.isError()){
                    model.addAttribute("isError", true);
                    model.addAttribute("message", response.getMessage());
                    return "error";
                }
            }
            if(quantity > 0){
                Response response = server.UpdateProductQuantity(productID, storeID, quantity);
                if(response.isError()){
                    model.addAttribute("isError", true);
                    model.addAttribute("message", response.getMessage());
                    return "error";
                }
            }
            if(category != null){
                Response response = server.UpdateProductCategory(productID, storeID, category);
                if(response.isError()){
                    model.addAttribute("isError", true);
                    model.addAttribute("message", response.getMessage());
                    return "error";
                }
            }
            if(description != null){
                Response response = server.UpdateProductDescription(productID, storeID, description);
                if(response.isError()){
                    model.addAttribute("isError", true);
                    model.addAttribute("message", response.getMessage());
                    return "error";
                }
            }
            return "OwnerManagerMenu";
        } else {
            model.addAttribute("message", "You're Not Authorized!");
            return "error";
        }
    }

    @RequestMapping(value = "/add-manager", method = RequestMethod.POST)
    public String addManager(@RequestParam("storeID_manager") int storeID,
                             @RequestParam("managerName_add") String managerName,
                             Model model) {

        if (model.getAttribute("owner").equals(true)) {
            Response response = server.appointNewStoreManager(managerName, storeID);
            if(response.isError()){
                model.addAttribute("isError", true);
                model.addAttribute("message", response.getMessage());
                return "error";
            }
            return "OwnerManagerMenu";
        } else {
            model.addAttribute("message", "You're Not Authorized!");
            return "error";
        }
    }

    @RequestMapping(value = "/change-permission", method = RequestMethod.POST)
    public String changeManagerPermission(@RequestParam("manager-name") String managerName,
                             @RequestParam("store-id") int storeID,
                             @RequestParam("permission") Permission permission,
                                          Model model) {

        if (model.getAttribute("owner").equals(true)) {
            linkPermission.add(permission);
            Response response = server.changeStoreManagerPermission(managerName, storeID, linkPermission);
            if(response.isError()){
                model.addAttribute("isError", true);
                model.addAttribute("message", response.getMessage());
                return "error";
            }

            gotPermission.put(managerName, linkPermission);
            model.addAttribute("gotPermission", gotPermission);
            model.addAttribute("name", managerName);
            return "OwnerManagerMenu";
        } else {
            model.addAttribute("message", "You're Not Authorized!");
            return "error";
        }
    }

    @RequestMapping(value = "/close-store", method = RequestMethod.POST)
    public String closeStore(@RequestParam("storeID-close") int storeID, Model model){
        if (model.getAttribute("owner").equals(true)){
            Response response = server.CloseStore(storeID);
            if (response.isError()){
                model.addAttribute("isError", true);
                model.addAttribute("message", response.getMessage());
                return "error";
            }
            return "OwnerManagerMenu";
        }else {
            model.addAttribute("message", "You're Not Authorized!");
            return "error";
        }
    }

    @RequestMapping(value = "/purchase-history", method = RequestMethod.POST)
    public String showPurchaseHistory(@RequestParam("storeID-purchase") int storeID, Model model){
        if (model.getAttribute("owner").equals(true)){
            Response response = server.GetStoreHistoryPurchase(storeID);
            if (response.isError()){
                model.addAttribute("isError", true);
                model.addAttribute("message", response.getMessage());
                return "error";
            }
            return "OwnerManagerMenu";
        }else {
            model.addAttribute("message", "You're Not Authorized!");
            return "error";
        }
    }

    @PostMapping("/employee-info")
    public String employeeInfo(){
        return "OwnerManagerMenu";
    }

}