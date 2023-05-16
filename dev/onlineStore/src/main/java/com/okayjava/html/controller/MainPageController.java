package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Facade.*;
import DomainLayer.Stores.Products.Product;
import DomainLayer.Stores.Products.StoreProduct;
import DomainLayer.Stores.Store;
import DomainLayer.Users.Role;
import DomainLayer.Users.Role.*;
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

import static DomainLayer.Users.Role.StoreFounder;
import static DomainLayer.Users.Role.StoreOwner;

@Controller
public class MainPageController {
	private Service server = new Service();
	public Map<Integer, ServiceStoreProduct> productList = new HashMap<>();
	public boolean logged = false, isAuthorized = false;
	private int visitorID;

//	public Role role;
	public boolean role = true; //delete

	@GetMapping("/")
    public String mainPage(Model model) throws Exception {
		visitorID = server.EnterNewSiteVisitor().getValue();
		model.addAttribute("visitorID", visitorID);
		System.out.println("BeforeLoadData");
		server.loadData();
//		model.addAttribute("role", role);
		System.out.println("LoadData");
		return "MainPage";
    }

	@GetMapping("/MainPage")
	public String reMainPage(Model model){
		System.out.println("reMainPage");
		model.addAttribute("logged", logged);
		model.addAttribute("isAuthorized", isAuthorized);
		model.addAttribute("role", role);
		return "MainPage";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String signIn(@RequestParam("username") String username,
							   @RequestParam("password") String password, Model model) {

		Response response = server.login(username, password);
		response.getValue();
		model.addAttribute("visitorID", response.getValue());
//		role = server.getRole(visitorID);
		model.addAttribute("role", role);
		System.out.println(response.getValue()); // == success
		if (!response.isError()) {
			logged = true;
			System.out.println("logged is getting true");
			model.addAttribute("logged", true);
			return ("MainPage");
		}
		else {
			logged = false;
			model.addAttribute("logged",false);
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return"error";
		}
	}


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestParam("register-name") String username,
								 @RequestParam("register-password") String password, Model model) {
//		visitorID = server.EnterNewSiteVisitor().getValue();
//		model.addAttribute("visitorID", visitorID);
		Response response = server.Register(username, password);
		System.out.println(response.getValue());
		if (!response.isError()) {
			return ("MainPage");
		} else {
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return "error";
		}
	}

	@GetMapping ( "/logout")
	public String logout(Model model) {
		server.logout();
		logged = false;
		System.out.println("getting out b yeeeeeeeeeeeeeeeeeee");
		model.addAttribute("logged", false);
		return "MainPage";
	}

	@RequestMapping(value = "/complaints", method = RequestMethod.POST)
	public String complaints(@RequestParam("complaints") String message, Model model) {
		//check the function in service
//		if (model.getAttribute("role").equals(StoreFounder)){
//			//will be able to see the complaints
//		}
		return "MainPage";
	}

	@RequestMapping(value = "/open-store", method = RequestMethod.POST)
	public String openStore(@RequestParam("store-name") String storeName, Model model) {
		System.out.println("first");
		Response response = server.OpenStore(storeName);
		System.out.println("119");
		// Check if open store is successful
		if (!response.isError()) {
//			role = StoreOwner;
//			owner = true;
			Integer storeID = (Integer) response.getValue();
//			server.getRolesData(storeID).getValue();
			model.addAttribute("role", role);
			model.addAttribute("logged", logged);
			model.addAttribute("storeID", storeID);
			return "MainPage";
		}
		else {
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return "error";
		}
	}

	@RequestMapping(value = "/show-result", method = RequestMethod.POST)
	public String userSearch(@RequestParam("filter-keyword") String keyword,
							 @RequestParam("filter-product-name") String productName,
							 @RequestParam("filter-store-name") String storeName,
							 @RequestParam("filter-category") String category,
							 @RequestParam("filter-description") String description,
							 @RequestParam(value = "min-price", defaultValue = "0") String minPriceStr,
							 @RequestParam(value = "max-price", defaultValue = "0") String maxPriceStr,
							 @RequestParam(value = "product_rate", defaultValue = "0") String productRateStr,
							 @RequestParam(value = "store_rate", defaultValue = "0") String storeRateStr,
							 Model model) {
//		System.out.println("147");
//		System.out.println(productName);
		int minPrice = parseOrDefault(minPriceStr, 0);
		int maxPrice = parseOrDefault(maxPriceStr, 0);
		int productRate = parseOrDefault(productRateStr, 0);
		int storeRate = parseOrDefault(storeRateStr, 0);

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




//		HashMap<Integer,List<ServiceStoreProduct>> storesIDsAndProducts = new HashMap<>();

		Response<List<ServiceStore>> response = server.FilterProductSearch(productFilter, storeFilter);//response.getValue returning 0.
		System.out.println("175");
		System.out.println(response.getValue().size() + "this is the size of the list from the filterProductSearch");
		System.out.println(storeFilter.size() + "this is the storeFilter size");
		System.out.println(productFilter.size() + "this is the productFilter size");
		System.out.println(productFilter.get(1) + "this is the productName");
		System.out.println(storeFilter.get(0).toString() + "this is the storeName");

		if (response.isError()){
			model.addAttribute("isError", true);
			model.addAttribute("message", response.getMessage());
			return "error";
		}

		List<ServiceStore> serviceStoreProducts = response.getValue();//this list is empty, its from filterProductSearch

		for(ServiceStore s : serviceStoreProducts){//not reaching this line because serviceStoreProducts is Empty.
			for(ServiceStoreProduct serviceStoreProduct : s.getProductList()){
				productList.put(s.getStoreId(), serviceStoreProduct); //all products after search

//				List<ServiceStoreProduct> servList = new LinkedList<>();
//				servList.add(serviceStoreProduct);
//				storesIDsAndProducts.put(s.getStoreId(),servList);
			}
		}
//		model.addAttribute("storesIDsAndProducts",storesIDsAndProducts);
		model.addAttribute("productList", productList); //only for search result
		return "SearchResults";
	}

	private int parseOrDefault(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@GetMapping("/error")
	public String error(Model model){
		model.addAttribute("message", "Page Not Found");
		return "error";
	}
}
