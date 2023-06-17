package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.*;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.*;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.NameStoreFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.RatingStoreFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceStore;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SearchResultsController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();

    @GetMapping("/SearchResults")
    public String searchResult(Model model) {
        model.addAttribute("logged", server.isLogged());
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "SearchResults";
    }

    @RequestMapping(value = "/show-result", method = RequestMethod.POST)
    public String userSearch(@RequestParam(value = "filter-keyword" , defaultValue = "") String keywordStr,
                             @RequestParam(value = "filter-product-name", defaultValue = "") String productNameStr,
                             @RequestParam(value = "filter-store-name", defaultValue = "") String storeNameStr,
                             @RequestParam(value = "filter-category", defaultValue = "") String categoryStr,
                             @RequestParam(value = "filter-description", defaultValue = "") String descriptionStr,
                             @RequestParam(value = "min-price", defaultValue = "-1") String minPriceStr,
                             @RequestParam(value = "max-price", defaultValue = "-1") String maxPriceStr,
                             @RequestParam(value = "product_rate", defaultValue = "-1") String productRateStr,
                             @RequestParam(value = "store_rate", defaultValue = "-1") String storeRateStr,
                             Model model) {

        String keyword = parseOrDefaultS(keywordStr, "");
        String productName = parseOrDefaultS(productNameStr, "");
        String storeName = parseOrDefaultS(storeNameStr, "");
        String category = parseOrDefaultS(categoryStr, "");
        String description = parseOrDefaultS(descriptionStr, "");
        int minPrice = parseOrDefault(minPriceStr, -1);
        int maxPrice = parseOrDefault(maxPriceStr, -1);
        int productRate = parseOrDefault(productRateStr, -1);
        int storeRate = parseOrDefault(storeRateStr, -1);

        //filter - product
        List<ProductFilter> productFilter = new ArrayList<>();
        productFilter.add(new CategoryProductFilter(category));
        productFilter.add(new NameProductFilter(productName));
        productFilter.add(new RatingProductFilter(productRate));
        productFilter.add(new MinPriceProductFilter(minPrice));
        productFilter.add(new MaxPriceProductFilter(maxPrice));
        productFilter.add(new DescriptionProductFilter(description));
        productFilter.add(new KeywordProductFilter(keyword));

        // filter - store
        List<StoreFilter> storeFilter = new ArrayList<>();
        storeFilter.add(new NameStoreFilter(storeName));
        storeFilter.add(new RatingStoreFilter(storeRate));
        Response<List<ServiceStore>> response = server.FilterProductSearch(request,productFilter, storeFilter);

        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage("Filtered Products: ");
            model.addAttribute("alert", alert.copy());
        }

        model.addAttribute("searchList", response.getValue());
        alert.reset();
        return "SearchResults";
    }

    private int parseOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String parseOrDefaultS(String value, String defaultValue) {
        try {
            if (value == null) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @RequestMapping(value = "/add-to-cart", method = {RequestMethod.GET, RequestMethod.POST})
    public String addToCart(@RequestParam("storeId") int storeId,
                            @RequestParam("productId") int productId,
                            @RequestParam("quantity") int quantity,
                            Model model) {

        Response<?> response = server.addProductToCart(request,productId, storeId, quantity);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        else {
            alert.setSuccess(true);
            alert.setMessage("Product added to cart.");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "SearchResults";
    }
}
