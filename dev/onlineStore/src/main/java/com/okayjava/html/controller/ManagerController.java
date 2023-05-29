package com.okayjava.html.controller;
import DomainLayer.Response;
import DomainLayer.Users.Permission;
import ServiceLayer.ServiceObjects.ServiceStore;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class ManagerController {
    private Server server = Server.getInstance();

    @GetMapping("/Manager")
    public String menu() {
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
            System.out.println("Error");
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else {
            model.addAttribute("message", response.getMessage());
            System.out.println("Store ID: " + response.getValue());
        }
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
            System.out.println("Manager Menu: \n error in adding a product!!");
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else {
            //add the product to allProduct list
            model.addAttribute("message", response.getMessage());
            System.out.println("Manager Menu: \n - product: " + productName + " was added successfully to store: " + storeID);
        }

        return "Manager";
    }

    @RequestMapping(value = "/remove-product", method = RequestMethod.POST)
    public String removeProduct(@RequestParam("storeID_remove") int storeID,
                                @RequestParam("productID-remove") int productID,
                                Model model) {

        Response<?> response = server.RemoveProduct(productID, storeID);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else {
            model.addAttribute("message", response.getMessage());
            System.out.println("Manager Menu: \n - product with id: " + productID + " was removed successfully from store: " + storeID);
        }

        return "Manager";
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
                System.out.println("fails here - 1");
                model.addAttribute("isError", true);
                model.addAttribute("errorMessage", response.getMessage());
                return "error";
            }
            else model.addAttribute("message", response.getMessage());
        }
        if (price > 0) {
            Response<?> response = server.UpdateProductPrice(productID, storeID, price);
            if (response.isError()) {
                System.out.println("fails here - 2");
                model.addAttribute("isError", true);
                model.addAttribute("errorMessage", response.getMessage());
                return "error";
            }
            else model.addAttribute("message", response.getMessage());
        }
        if (quantity > 0) {
            Response<?> response = server.UpdateProductQuantity(productID, storeID, quantity);
            if (response.isError()) {
                System.out.println("fails here - 3");
                model.addAttribute("isError", true);
                model.addAttribute("errorMessage", response.getMessage());
                return "error";
            }
            else model.addAttribute("message", response.getMessage());
        }
        if (category != null) {
            Response<?> response = server.UpdateProductCategory(productID, storeID, category);
            if (response.isError()) {
                System.out.println("fails here - 4");
                model.addAttribute("isError", true);
                model.addAttribute("errorMessage", response.getMessage());
                return "error";
            }
            else model.addAttribute("message", response.getMessage());
        }
        if (description != null) {
            Response<?> response = server.UpdateProductDescription(productID, storeID, description);
            if (response.isError()) {
                System.out.println("fails here - 5");
                model.addAttribute("isError", true);
                model.addAttribute("errorMessage", response.getMessage());
                return "error";
            }
            else model.addAttribute("message", response.getMessage());
        }

        return "Manager";
    }

    @RequestMapping(value = "/purchase-history", method = RequestMethod.POST)
    public String purchaseHistory(@RequestParam("storeID-purchase") int storeID,
                                  Model model) {

        Response<List<String>> response = server.GetStoreHistoryPurchase(storeID);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else {
            System.out.println("Purchase History should be displayed!");
            model.addAttribute("purchaseHistory", response.getValue()); //List<String>
        }
        return "Manager";
    }

    @RequestMapping(value = "/close-store", method = RequestMethod.POST)
    public String closeStore(@RequestParam("storeID-close") int storeID,
                             Model model) {

        Response<?> response = server.CloseStore(storeID);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        }
        else model.addAttribute("message", response.getMessage());
        return "Manager";
    }

    @RequestMapping(value = "/employee-info", method = RequestMethod.POST)
    public String employeeInfo(@RequestParam("storeID-employee") int storeID,
                               Model model) {


        Response<?> response = server.getRolesData(storeID);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else model.addAttribute("employeeInfo", response.getValue()); //String
        return "Manager";
    }

    @RequestMapping(value = "/appoint-store-owner", method = RequestMethod.POST)
    public String appointStoreOwner(@RequestParam("storeID-add-owner") int storeID,
                                    @RequestParam("owner-name-add") String ownerName,
                                    Model model) {

        Response<?> response = server.appointNewStoreOwner(ownerName, storeID);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else model.addAttribute("message", response.getValue());
        return "Manager";
    }

    @RequestMapping(value = "/appoint-store-manager", method = RequestMethod.POST)
    public String appointStoreManager(@RequestParam("storeID-add-manager") int StoreID,
                                      @RequestParam("manager-name-add") String managerName,
                                      Model model) {

        Response<?> response = server.appointNewStoreManager(managerName, StoreID);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else model.addAttribute("message", response.getValue());
        return "Manager";
    }

    @RequestMapping(value = "/change-permission", method = RequestMethod.POST)
    public String changePermission(@RequestParam("manager-name") String managerName,
                                   @RequestParam("store-id") int storeID,
                                   @RequestParam("permission") List<Permission> permissions,
                                   Model model) {

        Response<?> response = server.changeStoreManagerPermission(managerName, storeID, permissions);
        if (response.isError()) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", response.getMessage());
            return "error";
        } else model.addAttribute("message", response.getValue());
        return "Manager";
    }
}
