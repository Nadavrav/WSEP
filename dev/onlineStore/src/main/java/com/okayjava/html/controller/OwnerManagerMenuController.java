package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Users.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ServiceLayer.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static DomainLayer.Users.Permission.*;

@Controller
public class OwnerManagerMenuController {
    private Service server = new Service();

    @GetMapping("/OwnerManagerMenu")
    public String OwnerManagerMenu() {
        return "OwnerManagerMenu";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("storeID_add") int storeID,
                             @RequestParam("product-name") String productName,
                             @RequestParam("product-price") double productPrice,
                             @RequestParam("product-category") String category,
                             @RequestParam("product-quantity") int quantity,
                             @RequestParam("product-desc") String desc,
                             Model model) {

        if (model.getAttribute("isAuthorized").equals(true)) {
            //functionality to add a product
            Response response = server.AddProduct(storeID, productName, productPrice, category, quantity, desc);
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

    @PostMapping("/remove-product")
    public String removeProduct(@RequestParam("storeID_remove") int storeID,
                                @RequestParam("product-id") int productID,
                                Model model) {

        if (model.getAttribute("isAuthorized").equals(true)) {
            //functionality to remove a product
            Response response = server.removeProductFromCart(productID, storeID);
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

    @PostMapping("/update-product")
    public String updateProduct(@RequestParam("storeID_update") int storeID,
                                @RequestParam("productID_update") int productID,
                                @RequestParam("productname") String productName,
                                @RequestParam("price") double price,
                                @RequestParam("quantity") int quantity,
                                @RequestParam("category") String category,
                                @RequestParam("description") String description,
                                Model model) {

        if (model.getAttribute("idAdmin").equals(true)) {
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

    @PostMapping("/add-manager")
    public String addManager(@RequestParam("storeID_manager") int storeID,
                             @RequestParam("managerName_add") String managerName,
                             Model model) {

        if (model.getAttribute("isAuthorized").equals(true)) {
            //functionality to remove a product
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

    @PostMapping("/change-permission")
    public String changeManagerPermission(@RequestParam("manager-name") String managerName,
                             @RequestParam("store-id") int storeID,
                             @RequestParam("permission") Permission permission,
                                          Model model) {
        LinkedList<Permission> linkPermission = new LinkedList<>();
        linkPermission.add(permission);
        if (model.getAttribute("isAuthorized").equals(true)) {
            if ((permission== CanAppointStoreOwner)){
                model.getAttribute("isAuthorizedAppointStoreOwner").equals(true);
            }
            if ((permission== CanAppointStoreManager)){
                model.getAttribute("isAuthorizedCanAppointStoreManager").equals(true);
            }
            if ((permission== CanSeeCommentsAndRating)){
                model.getAttribute("isAuthorizedCanSeeCommentsAndRating").equals(true);
            }
            if ((permission==CanSeePurchaseHistory)){
                model.getAttribute("isAuthorizedCanSeePurchaseHistory").equals(true);
            }
            //functionality to remove a product
            Response response = server.changeStoreManagerPermission(managerName, storeID, linkPermission);
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

    @PostMapping("close-store")
    public String closeStore(@RequestParam("storeID-close") int storeID, Model model){
        Response response = server.CloseStore(storeID);
        if (response.isError()){
            model.addAttribute("isError", true);
            model.addAttribute("message", response.getMessage());
            return "error";
        }
        model.addAttribute("message", "");
        return "OwnerManagerMenu";
    }

    @PostMapping("/employee-info")
    public String employeeInfo(){
        return "OwnerManagerMenu";
    }

}
