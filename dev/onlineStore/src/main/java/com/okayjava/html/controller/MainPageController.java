package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Facade.*;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import DomainLayer.Users.RegisteredUser;
import ServiceLayer.*;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.*;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.NameStoreFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.RatingStoreFilter;
import ServiceLayer.ServiceObjects.Fiters.StoreFilters.StoreFilter;
import ServiceLayer.ServiceObjects.ServiceProducts.ServiceStoreProduct;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class MainPageController {
	private Service server = new Service();
	public Map<Integer, ServiceStoreProduct> productList = new HashMap<Integer, ServiceStoreProduct>();

	@GetMapping("/")
    public String mainPage(Model model) throws Exception {
		System.out.println("31");
		server.loadData();
		model.addAttribute("logged", model.getAttribute("logged"));
		model.addAttribute("isAuthorized", model.getAttribute("isAuthorized"));
        return "/MainPage";
    }

	@GetMapping("/MainPage")
	public String reMainPage(Model model){
		System.out.println("38");
		model.addAttribute("logged", true);
		model.addAttribute("isAuthorized", model.getAttribute("isAuthorized"));
		return "MainPage";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String signIn(@RequestParam("username") String username,
							   @RequestParam("password") String password, Model model) {

		Response response = server.login(username, password);
		model.addAttribute("visitorID", response.getValue());

		if (!response.isError()) {
			model.addAttribute("logged", true);
			return ("MainPage");
		}
		else {
			model.addAttribute("logged",false);
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return"error";
		}
	}


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestParam("register-name") String username,
								 @RequestParam("register-password") String password, Model model) {

		Response response = server.Register(username, password);
		if (!response.isError()) {
			model.addAttribute("logged", true);
			return ("MainPage");
		} else {
//			model.addAttribute("logged", false);
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return "error";
		}
	}

	@GetMapping ( "/logout")
	public String logout(Model model) {
		server.logout();
		model.addAttribute("logged", false);
		return "MainPage";
//		Response response = server.logout();
//		// Check if logout is successful
//		System.out.println("logging out2");
//		if (!response.isError()) {
//			// Redirect to a success page
//			System.out.println("logging out");
//			model.addAttribute("logged", false);
//			return ("/");
//		} else {
//			System.out.println("logging out3");
////			model.addAttribute("logged", true);
//			model.addAttribute("isError", true);
//			model.addAttribute("message", response.getMessage());
//			return "error";
//		}
	}

	@RequestMapping(value = "/complaints", method = RequestMethod.POST)
	public String complaints(@RequestParam("complaints") String message, Model model) {
		//check the function in service
		return "MainPage";
	}

	@RequestMapping(value = "/open-store", method = RequestMethod.POST)
	public String openStore(@RequestParam("store-name") String storeName, Model model) {

		Response response = server.OpenStore(storeName);
		// Check if open store is successful
		if (!response.isError()) {
			System.out.println("notError");
			// Redirect to a success page
			model.addAttribute("owner", true);
			return "MainPage";
		}
		else {
			System.out.println("Error");
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return "error";
		}
	}

	@PostMapping("/show-result")
	public String userSearch(@RequestParam("filter-keyword") String keyword,
							 @RequestParam("filter-product-name") String productName,
							 @RequestParam("filter-store-name") String storeName,
							 @RequestParam("filter-category") String category,
							 @RequestParam("filter-description") String description,
							 @RequestParam("min-price") int minPrice,
							 @RequestParam("max-price") int maxPrice,
							 @RequestParam("product_rate") int productRate,
							 @RequestParam("store_rate") int storeRate, Model model) {

		//filter - product
		List<ProductFilter> productFilter = new LinkedList<>();
		productFilter.add(new CategoryProductFilter(category));
		productFilter.add(new NameProductFilter(productName));
		productFilter.add(new RatingProductFilter(productRate));
		productFilter.add(new MinPriceProductFilter(minPrice));
		productFilter.add(new MaxPriceProductFilter(maxPrice));
		productFilter.add(new DescriptionProductFilter(description));
		productFilter.add(new KeywordProductFilter(keyword));
		// filter - store
		List<StoreFilter> storeFilter = new LinkedList<>();
		storeFilter.add(new NameStoreFilter(storeName));
		storeFilter.add(new RatingStoreFilter(storeRate));

		Response<List<ServiceStore>> response = server.FilterProductSearch(productFilter, storeFilter);

		if (response.isError()){
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return "error";
		}

//
//		int length = response.getValue().size();
//		for(int i = 0 ; i < length ; i++){
//			Object[] object = new Object[5];
//			object[0] = response.getValue().get(i); //store name
//			object[1] = //product name
//			object[2] = //price
//			object[3] = //category
//			object[4] = //description
//			object[5] = //quantity
//			productList.add(object);
//		}

		List<ServiceStore> serviceStoreProducts = response.getValue();

		for(ServiceStore s : serviceStoreProducts){
			for(ServiceStoreProduct serviceStoreProduct : s.getProductList()){
				productList.put(s.getStoreId(), serviceStoreProduct); //all products after search
			}
		}
		model.addAttribute("productList", productList); //only for search result
		return "SearchResults";
	}

	@GetMapping("/error")
	public String error(Model model){
		model.addAttribute("message", "Page Not Found");
		return "error";
	}
}
