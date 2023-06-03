package com.okayjava.html.controller;
import DomainLayer.Response;
import DomainLayer.Users.Permission;
import ServiceLayer.ServiceObjects.ServiceStore;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class ManagerController {

    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Manager")
    public String menu(Model model) {
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "Manager";
    }

//    @RequestMapping(value = "/my-stores", method = RequestMethod.POST)
//    public String showMyStores(Model model) throws Exception {
//        System.out.println("here!");
//        Response<List<ServiceStore>> response = server.getStoresByUserName();
//        if (response.isError()) {
//            System.out.println("Error");
//            model.addAttribute("isError", true);
//            model.addAttribute("errorMessage", response.getMessage());
//            return "error";
//        } else
//            model.addAttribute("myStoresList", response.getValue()); //List<ServiceStore>
//        return "redirect:/Manager";
//    }

    @RequestMapping(value = "/openStore", method = RequestMethod.POST)
    public String openStore(@RequestParam("store-name") String storeName,
                            Model model) {

        Response<Integer> response = server.OpenStore(storeName);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error");
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Store Opened with ID: " + response.getValue());
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.POST)
    public String permissions(Model model) {
        return "Manager";
    }

    @RequestMapping(value = "/add-product", method = RequestMethod.POST)
    public String addProduct(@RequestParam("storeID-add") int storeID,
                             @RequestParam("product-name") String productName,
                             @RequestParam("product-price") double price,
                             @RequestParam("product-category") String category,
                             @RequestParam("product-quantity") int quantity,
                             @RequestParam("product-desc") String description,
                             Model model) {

        Response<Integer> response = server.AddProduct(storeID, productName, price, category, quantity, description);

        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error in adding a product!!");
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println(productName + " was added successfully to storeId: " + storeID);
        }
        alert.reset();
        return "Manager";
    }

    @RequestMapping(value = "/remove-product", method = RequestMethod.POST)
    public String removeProduct(@RequestParam("storeID_remove") int storeID,
                                @RequestParam("productID-remove") int productID,
                                Model model) {

        Response<?> response = server.RemoveProduct(productID, storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("product with id: " + productID + " was removed successfully from storeId: " + storeID);
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/update-product", method = RequestMethod.POST)
    public String updateProduct(@RequestParam("storeID-update") int storeID,
                                @RequestParam("productID-update") int productID,
                                @RequestParam("productName-update") String productName,
                                @RequestParam("price-update") double price,
                                @RequestParam("quantity-update") int quantity,
                                @RequestParam("category-update") String category,
                                @RequestParam("description-update") String description,
                                Model model) {

        if (productName != null) {
            Response<?> response = server.UpdateProductName(productID, storeID, productName);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
            }
        }
        if (price > 0) {
            Response<?> response = server.UpdateProductPrice(productID, storeID, price);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
            }
        }
        if (quantity > 0) {
            Response<?> response = server.UpdateProductQuantity(productID, storeID, quantity);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
            }
        }
        if (category != null) {
            Response<?> response = server.UpdateProductCategory(productID, storeID, category);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", response.getMessage());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
            }
        }
        if (description != null) {
            Response<?> response = server.UpdateProductDescription(productID, storeID, description);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
            }
        }
        alert.setSuccess(true);
        alert.setMessage("Product Updated Successfully");
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/purchase-history", method = RequestMethod.POST)
    public String purchaseHistory(@RequestParam("storeID-purchase") int storeID,
                                  Model model) {

        Response<List<String>> response = server.GetStoreHistoryPurchase(storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            System.out.println("Purchase History should be displayed!");
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        model.addAttribute("purchaseHistory", response.getValue()); //List<String>
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/close-store", method = RequestMethod.POST)
    public String closeStore(@RequestParam("storeID-close") int storeID,
                             Model model) {

        Response<?> response = server.CloseStore(storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/employee-info", method = RequestMethod.POST)
    public String employeeInfo(@RequestParam("storeID-employee") int storeID,
                               Model model) {


        Response<?> response = server.getRolesData(storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else{
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("employeeInfo", response.getValue()); //String
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/appoint-store-owner", method = RequestMethod.POST)
    public String appointStoreOwner(@RequestParam("storeID-add-owner") int storeID,
                                    @RequestParam("owner-name-add") String ownerName,
                                    Model model) {

        Response<?> response = server.appointNewStoreOwner(ownerName, storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else{
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/appoint-store-manager", method = RequestMethod.POST)
    public String appointStoreManager(@RequestParam("storeID-add-manager") int StoreID,
                                      @RequestParam("manager-name-add") String managerName,
                                      Model model) {

        Response<?> response = server.appointNewStoreManager(managerName, StoreID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else{
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/change-permission", method = RequestMethod.POST)
    public String changePermission(@RequestParam("manager-name") String managerName,
                                   @RequestParam("store-id") int storeID,
                                   @RequestParam("permission") List<Permission> permissions,
                                   Model model) {

        Response<?> response = server.changeStoreManagerPermission(managerName, storeID, permissions);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else{
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/Manager";
    }
}
