package com.okayjava.html.controller;
import DomainLayer.Response;
import DomainLayer.Stores.Store;
import DomainLayer.Users.Permission;
import ServiceLayer.ServiceObjects.ServiceStore;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ManagerController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/Manager")
    public String menu(Model model) {
        alert.reset();
        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        Response<Collection<ServiceStore>> response = server.getMyStores(request);
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("myStoresList", response.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "Manager";
    }

    @RequestMapping(value = "/fetch_permissions", method = {RequestMethod.GET, RequestMethod.POST})
    public String fetchPermissions(@RequestParam("storeId") int storeId,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<LinkedList<Permission>> response = server.getPermissions(request, storeId);
        if (response.isError()){
            System.out.println("error: " + response.getMessage());
            alert.setFail(true);
            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            redirectAttributes.addFlashAttribute("myPermissions", response.getValue());
            System.out.println(response.getValue());
        }

        //re-manager
        Response<Collection<ServiceStore>> responseMyStores = server.getMyStores(request);
        if (responseMyStores.isError()){
            alert.setFail(true);
            alert.setMessage(responseMyStores.getMessage());
//            model.addAttribute("alert", alert.copy());
        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("myStoresList", responseMyStores.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }

        Response<List<String>> responsePurchaseHistory = server.GetStoreHistoryPurchase(request, storeId);
        if (!responsePurchaseHistory.isError()) {
            System.out.println("Purchase History should be displayed! " + responsePurchaseHistory.getValue());
            alert.setSuccess(true);
            alert.setMessage(responsePurchaseHistory.getMessage());
            model.addAttribute("alert", alert.copy());
            redirectAttributes.addFlashAttribute("purchaseHistory", responsePurchaseHistory.getValue());
        }

        Response<?> responseEmployeeInfo = server.getRolesData(request,storeId);
        if (!responseEmployeeInfo.isError()) {
            alert.setSuccess(true);
            alert.setMessage("Employee Info for StoreId: " + storeId);
            model.addAttribute("alert", alert.copy());
            redirectAttributes.addFlashAttribute("employeeInfo", responseEmployeeInfo.getValue()); //String
            System.out.println("success - " + responseEmployeeInfo.getValue());
        }

        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/openStore", method = RequestMethod.POST)
    public String openStore(@RequestParam("store-name") String storeName,
                            Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<Integer> response = server.OpenStore(request,storeName);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error");
        } else {
            alert.setSuccess(true);
            alert.setMessage(storeName + " Store is Opened - ID - " + response.getValue());
            model.addAttribute("alert", alert.copy());
            System.out.println("Store Opened with ID: " + response.getValue());
        }
//        model.addAttribute("alert", alert.copy());
        //re-manager
        Response<Collection<ServiceStore>> responseMyStores = server.getMyStores(request);
        if (responseMyStores.isError()){
            alert.setFail(true);
            alert.setMessage(responseMyStores.getMessage());
//            model.addAttribute("alert", alert.copy());
        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
            model.addAttribute("myStoresList", responseMyStores.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }
        alert.reset();
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

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<Integer> response = server.AddProduct(request,storeID, productName, price, category, quantity, description);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error in adding a product!!");
        } else {
            alert.setSuccess(true);
            alert.setMessage(productName + " was added successfully to storeId: " + storeID);
            model.addAttribute("alert", alert.copy());
            System.out.println(productName + " was added successfully to storeId: " + storeID);
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/remove-product", method = RequestMethod.POST)
    public String removeProduct(@RequestParam("storeID_remove") int storeID,
                                @RequestParam("productID-remove") int productID,
                                Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<?> response = server.RemoveProduct(request,productID, storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage("product with id: " + productID + " was removed successfully from storeId: " + storeID);
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

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        if (productName != null) {
            Response<?> response = server.UpdateProductName(request,productID, storeID, productName);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage("Product Name Updated to " + productName + " Successfully!");
                model.addAttribute("alert", alert.copy());
            }
        }
        if (price > 0) {
            Response<?> response = server.UpdateProductPrice(request,productID, storeID, price);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage("Product Price Updated to " + price + " Successfully!");
                model.addAttribute("alert", alert.copy());
            }
        }
        if (quantity > 0) {
            Response<?> response = server.UpdateProductQuantity(request,productID, storeID, quantity);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage("Product Quantity Updated to " + quantity + " Successfully!");
                model.addAttribute("alert", alert.copy());
            }
        }
        if (category != null) {
            Response<?> response = server.UpdateProductCategory(request,productID, storeID, category);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", response.getMessage());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage("Product Quantity Updated to " + category + " Successfully!");
                model.addAttribute("alert", alert.copy());
            }
        }
        if (description != null) {
            Response<?> response = server.UpdateProductDescription(request,productID, storeID, description);
            if (response.isError()) {
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                alert.reset();
                return "redirect:/Manager";
            }
            else{
                alert.setSuccess(true);
                alert.setMessage("Product Description Updated to " + description + " Successfully!");
                model.addAttribute("alert", alert.copy());
            }
        }
        alert.setSuccess(true);
        alert.setMessage("Product Updated Successfully");
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "redirect:/Manager";
    }

//    @RequestMapping(value = "/purchase-history", method = RequestMethod.GET)
//    public String purchaseHistory(@RequestParam("storeID-purchase") int storeID, Model model) {
//        model.addAttribute("alert", alert.copy());
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//
//        Response<List<String>> response = server.GetStoreHistoryPurchase(request, storeID);
//        if (response.isError()) {
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            System.out.println("Purchase History should be displayed! " + response.getValue());
//
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("purchaseHistory", response.getValue());
//        }
//        alert.reset();
//        return "Manager";
//    }


    @RequestMapping(value = "/close-store", method = RequestMethod.POST)
    public String closeStore(@RequestParam("storeID-close") int storeID,
                             Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<?> response = server.CloseStore(request,storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else{
            alert.setSuccess(true);
            alert.setMessage("Store With ID: " + storeID + "Is Closed!");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/Manager";
    }

//    @RequestMapping(value = "/employee-info", method = RequestMethod.POST)
//    public String employeeInfo(@RequestParam("storeID-employee") int storeID,
//                               Model model) {
//
//        model.addAttribute("alert", alert.copy());
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//
//        Response<?> response = server.getRolesData(request,storeID);
//        if (response.isError()) {
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else{
//            alert.setSuccess(true);
//            alert.setMessage("Employee Info for StoreId: " + storeID);
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("employeeInfo", response.getValue()); //String
//            System.out.println("success - " + response.getValue());
//        }
//        alert.reset();
//        return "redirect:/Manager";
//    }

    @RequestMapping(value = "/remove-employee", method = RequestMethod.POST)
    public String removeEmployee(@RequestParam("storeID-remove-emp") int storeID,
                                 @RequestParam("username-remove-emp") String userName,
                                 Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<?> response = server.removeEmployee(request,storeID, userName);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(userName + " Was Removed!");
            model.addAttribute("alert", alert.copy());
            System.out.println(userName + " was removed successfully from storeId: " + storeID);
        }
        alert.reset();
        return "redirect:/Manager";
    }

    @RequestMapping(value = "/appoint-store-owner", method = RequestMethod.POST)
    public String appointStoreOwner(@RequestParam("storeID-add-owner") int storeID,
                                    @RequestParam("owner-name-add") String newOwnerName,
                                    Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<?> response = server.appointNewStoreOwner(request, newOwnerName, storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {alert.setSuccess(true);
                alert.setMessage("Appointment requests have been sent to existing owners for storeID: " + storeID);
                model.addAttribute("alert", alert.copy());
            }
        alert.reset();
        return "redirect:/Manager";
    }


    @RequestMapping(value = "/appoint-store-manager", method = RequestMethod.POST)
    public String appointStoreManager(@RequestParam("storeID-add-manager") int StoreID,
                                      @RequestParam("manager-name-add") String managerName,
                                      Model model) {

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<?> response = server.appointNewStoreManager(request,managerName, StoreID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else{
            alert.setSuccess(true);
            alert.setMessage(managerName + " is a Manager for storeID: " + StoreID);
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

        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());

        Response<?> response = server.changeStoreManagerPermission(request,managerName, storeID, permissions);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else{
            alert.setSuccess(true);
            alert.setMessage("Permissions has been changed successfully!");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/Manager";
    }
}
