package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NameCondition;
import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.FilterOnlyIfCondition;
import DomainLayer.Stores.Conditions.ConditionTypes.Condition;
import DomainLayer.Stores.Conditions.ConditionTypes.FilterCondition;
import DomainLayer.Stores.Discounts.AdditiveDiscount;
import DomainLayer.Stores.Discounts.BasicDiscount;
import DomainLayer.Stores.Policies.Policy;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.MinPriceProductFilter;
import ServiceLayer.ServiceObjects.ServiceBid;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.*;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionTypes;
import ServiceLayer.ServiceObjects.ServiceDiscounts.*;
import ServiceLayer.ServiceObjects.ServicePolicies.ServiceBasicPolicy;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.CompositeMessageCondition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class MyStoreController {
    @Autowired
    private HttpServletRequest request;

    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();
    private int storeID;
    private String StoreName;

    @GetMapping("/MyStore/{storeId}")
    public String myStoreMenu(Model model,
                              @PathVariable("storeId") int storeId,
                              @RequestParam("storeName") String storeName) {

        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("storeName", storeName);
        Response<Collection<ServiceDiscountInfo>> responseDiscount = server.getStoreDiscountInfo(request, storeId);
        if (responseDiscount.isError()){
            alert.setFail(true);
            alert.setMessage(responseDiscount.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responseDiscount.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("discountInfo", responseDiscount.getValue());
            System.out.println("discounts: " + responseDiscount.getValue());
        }

        Response<HashSet<ServicePolicy>> responsePolicy = server.getStorePolicy(request, storeId);
        if (responsePolicy.isError()){
            alert.setFail(true);
            alert.setMessage(responsePolicy.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responsePolicy.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("policyInfo", responsePolicy.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }

        Response<Collection<ServiceBid>> responseBid = server.geStoreBids(request, storeId);
        if (!responseBid.isError()) {
            System.out.println(storeId);
            Collection<ServiceBid> bidRequests = responseBid.getValue();
            model.addAttribute("bidRequests", bidRequests);
            System.out.println("Bid Requests: " + bidRequests);
        }

        Response<List<String>> responsePurchaseHistory = server.GetStoreHistoryPurchase(request, storeId);
        if (!responsePurchaseHistory.isError()) {
            System.out.println("Purchase History should be displayed! " + responsePurchaseHistory.getValue());
            alert.setSuccess(true);
            alert.setMessage(responsePurchaseHistory.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("purchaseHistory", responsePurchaseHistory.getValue());
        }

        Response<?> responseEmployeeInfo = server.getRolesData(request, storeId);
        if (!responseEmployeeInfo.isError()) {
            alert.setSuccess(true);
            alert.setMessage("Employee Info for StoreId: " + storeId);
            model.addAttribute("alert", alert.copy());
            model.addAttribute("employeeInfo", responseEmployeeInfo.getValue()); //String
            System.out.println("success - " + responseEmployeeInfo.getValue());
        }

        this.storeID = storeId;
        this.StoreName = storeName;
        alert.reset();
        return "MyStore";
    }

    @GetMapping("/MyStore")
    public String myStoreMenu(Model model){

        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("storeName", StoreName); // Add the store name attribute
        model.addAttribute("discountInfo", server.getStoreDiscountInfo(request, storeID).getValue());
        model.addAttribute("policyInfo", server.getStorePolicy(request, storeID).getValue());
        model.addAttribute("appointmentRequests", server.getAppointmentRequests(request).getValue());
        model.addAttribute("purchaseHistory", server.GetStoreHistoryPurchase(request, storeID).getValue());
        return "MyStore";
    }

    @PostMapping("/add-discount")
    public String processDiscountOption(@RequestParam("discountId") Integer[] discountIds,
                                        @RequestParam("description") String description,
                                        @RequestParam("amount") Integer amount,
                                        @RequestParam("condition") String condition,
                                        Model model) {

        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("storeName", StoreName);
        Integer id1 = discountIds[0]; //first selected discount
        Integer id2 = discountIds[1]; // second selected discount

        ServiceMultiDiscount serviceMultiDiscount;
        ServiceBasicDiscount serviceBasicDiscount;

        if (discountIds.length == 2){
            if (condition.equals("AndCondition")) {
                AndConditionRecord andConditionRecord = new AndConditionRecord(id1, id2);
                serviceBasicDiscount = new ServiceBasicDiscount(description, amount, andConditionRecord);
                server.addDiscount(request, serviceBasicDiscount, storeID);
            } else if (condition.equals("OrCondition")) {
                OrConditionRecord orConditionRecord = new OrConditionRecord(id1, id2);
                serviceBasicDiscount = new ServiceBasicDiscount(description, amount, orConditionRecord);
                server.addDiscount(request, serviceBasicDiscount, storeID);
            } else if (condition.equals("XorCondition")) {
                XorConditionRecord xorConditionRecord = new XorConditionRecord(id1, id2);
                serviceBasicDiscount = new ServiceBasicDiscount(description, amount, xorConditionRecord);
                server.addDiscount(request, serviceBasicDiscount, storeID);
            }
        } else if (discountIds.length > 2) {
            if (condition.equals("MinBetween")) {
                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.MinBetweenDiscount, description, Arrays.asList(discountIds));
                server.addDiscount(request, serviceMultiDiscount, storeID);
            } else if (condition.equals("Additive")) {
                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.AdditiveDiscount, description, Arrays.asList(discountIds));
                server.addDiscount(request, serviceMultiDiscount, storeID);
            } else if (condition.equals("MaxBetween")) {
                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.MaxBetweenDiscounts, description, Arrays.asList(discountIds));
                server.addDiscount(request, serviceMultiDiscount, storeID);
            }
        }

//        Response<Collection<ServiceDiscountInfo>> responseDiscount = server.getStoreDiscountInfo(request, storeID);
//        if (responseDiscount.isError()) {
//            alert.setFail(true);
//            alert.setMessage(responseDiscount.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            alert.setSuccess(true);
//            alert.setMessage(responseDiscount.getMessage());
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("discountInfo", responseDiscount.getValue());
//        }

//        Response<HashSet<ServicePolicy>> responsePolicy = server.getStorePolicy(request, storeID);
//        if (responsePolicy.isError()) {
//            alert.setFail(true);
//            alert.setMessage(responsePolicy.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            alert.setSuccess(true);
//            alert.setMessage(responsePolicy.getMessage());
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("policyInfo", responsePolicy.getValue());
//        }
        alert.reset();
        return "MyStore";
//        return "redirect:/MyStore/" + storeID + "?storeName=" + StoreName;
    }

    @PostMapping("/add-basic-discount")
    public String addNewDiscount(@RequestParam("conditionSelect") String condition,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "endDate", required = false) String endDate,
                                 @RequestParam(value = "startHour", required = false) String startHour,
                                 @RequestParam(value = "endHour", required = false) String endHour,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "maxBagPrice", required = false) Double maxBagPrice,
                                 @RequestParam(value = "minBagPrice", required = false) Double minBagPrice,
                                 @RequestParam(value = "maxTotalProductAmount", required = false) Double maxTotalProductAmount,
                                 @RequestParam(value = "minTotalProductAmount", required = false) Integer minTotalProductAmount,
                                 @RequestParam("description") String description,
                                 @RequestParam("amount") Integer amount,
                                 Model model) {

        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("storeName", StoreName);
        switch (condition) {
            case "BetweenDatesCondition" -> {
                if (startDate != null && endDate != null) {
                    String[] startDateParts = startDate.split("-");
                    String[] endDateParts = endDate.split("-");

                    int fromDay = Integer.parseInt(startDateParts[2]);
                    int fromMonth = Integer.parseInt(startDateParts[1]);
                    int fromYear = Integer.parseInt(startDateParts[0]);

                    int untilDay = Integer.parseInt(endDateParts[2]);
                    int untilMonth = Integer.parseInt(endDateParts[1]);
                    int untilYear = Integer.parseInt(endDateParts[0]);

                    BetweenDatesConditionRecord betweenDatesConditionRecord =
                            new BetweenDatesConditionRecord(fromDay, fromMonth, fromYear, untilDay, untilMonth, untilYear);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, betweenDatesConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            case "LocalHourRangeCondition" -> {
                if (startHour != null && endHour != null) {
                    String[] startHourParts = startHour.split(":");
                    String[] endHourParts = endHour.split(":");

                    int lowerBoundaryHour = Integer.parseInt(startHourParts[0]);
                    int lowerBoundaryMin = Integer.parseInt(startHourParts[1]);

                    int upperBoundaryHour = Integer.parseInt(endHourParts[0]);
                    int upperBoundaryMin = Integer.parseInt(endHourParts[1]);

                    LocalHourRangeConditionRecord localHourRangeConditionRecord =
                            new LocalHourRangeConditionRecord(lowerBoundaryHour, lowerBoundaryMin, upperBoundaryHour, upperBoundaryMin);

                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, localHourRangeConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            case "CategoryCondition" -> {
                if (category != null) {
                    CategoryConditionRecord categoryConditionRecord = new CategoryConditionRecord(category);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, categoryConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            case "NameCondition" -> {
                if (name != null) {
                    System.out.println("in name condition: " + name);
                    NameConditionRecord nameConditionRecord = new NameConditionRecord(name);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, nameConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
                    System.out.println("after adding disocunt: " + server.addDiscount(request, serviceBasicDiscount, storeID).getValue());
                }
            }
            case "MaxBagPriceCondition" -> {
                if (maxBagPrice != null) {
                    MaxBagPriceConditionRecord maxBagPriceConditionRecord = new MaxBagPriceConditionRecord(maxBagPrice);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, maxBagPriceConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            case "MinBagPriceCondition" -> {
                if (minBagPrice != null) {
                    MinBagPriceConditionRecord minBagPriceConditionRecord = new MinBagPriceConditionRecord(minBagPrice);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, minBagPriceConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            case "MaxTotalProductAmountCondition" -> {
                if (maxTotalProductAmount != null) {
                    MaxTotalProductAmountConditionRecord maxTotalProductAmountConditionRecord =
                            new MaxTotalProductAmountConditionRecord(maxTotalProductAmount);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, maxTotalProductAmountConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            case "MinTotalProductAmountCondition" -> {
                if (minTotalProductAmount != null) {
                    MinTotalProductAmountConditionRecord minTotalProductAmountConditionRecord =
                            new MinTotalProductAmountConditionRecord(minTotalProductAmount);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, minTotalProductAmountConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
            }
            default -> {
            }
        }
//        alert.setSuccess(true);
//        alert.setMessage("Discount was added to store " + StoreName);
//        model.addAttribute("alert", alert.copy());
        alert.reset();
//        return "redirect:/MyStore/" + storeID + "?storeName=" + StoreName;
        return "MyStore";
    }

//    @PostMapping ("policy_option")
//    public String addNewPolicy(@RequestParam("policyConditionSelect") String condition,
//                               @RequestParam("policyDescription") String description,
//                               @RequestParam(value = "message", required = false) String message,
//                               Model model){
//
//
//    }

    @RequestMapping(value="/dailyStoreIncome", method = RequestMethod.GET)
    public String showDailyIncomeToStore(@RequestParam("date") String dateString,
                                         Model model) {

        String[] dateParts = dateString.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        System.out.println(month + "/"+ day +"/"+ year);
        Response<Integer> response = server.getDailyIncomeByStore(request, day, month, year, storeID);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("income for storeId " + storeID + " : " + response.getValue());
            model.addAttribute("dailyIncome", response.getValue());
        }

        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("storeName", StoreName); // Add the store name attribute

        Response<Collection<ServiceDiscountInfo>> responseDiscount = server.getStoreDiscountInfo(request, storeID);
        if (responseDiscount.isError()) {
            alert.setFail(true);
            alert.setMessage(responseDiscount.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responseDiscount.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("discountInfo", responseDiscount.getValue());
        }

        Response<HashSet<ServicePolicy>> responsePolicy = server.getStorePolicy(request, storeID);
        if (responsePolicy.isError()) {
            alert.setFail(true);
            alert.setMessage(responsePolicy.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responsePolicy.getMessage());
            model.addAttribute("alert", alert.copy());
            model.addAttribute("policyInfo", responsePolicy.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();

            // Filter appointment requests based on the logged-in user
            Map<Integer, List<String>> filteredAppointmentRequests = new HashMap<>();
            for (Map.Entry<Integer, List<String>> entry : appointmentRequests.entrySet()) {
                List<String> owners = entry.getValue();
                if (owners.contains(server.getUsername(request))) {
                    filteredAppointmentRequests.put(entry.getKey(), owners);
                }
            }
            model.addAttribute("appointmentRequests", filteredAppointmentRequests);
            System.out.println("Appointment Requests: " + filteredAppointmentRequests);
        }

        alert.reset();
        return "MyStore";
    }

    @PostMapping("/acceptBid")
    public String acceptBid(@RequestParam("productId") int productId,
                            @RequestParam("userName") String userName,
                            Model model) {

        Response<?> response = server.voteOnBid(request, productId, storeID, userName, true);
        if (response.isError()) {
            System.out.println("acceptBid success.");
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            System.out.println("acceptBid success.");
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "MyStore";
    }

    @PostMapping("/declineBid")
    public String declineBid(@RequestParam("productId") int productId,
                             @RequestParam("userName") String userName,
                             Model model) {
        Response<?> response = server.voteOnBid(request, productId, storeID, userName, false);
        if (response.isError()) {
            System.out.println("declineBid success.");
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            System.out.println("declineBid success.");
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "MyStore";
    }

    @PostMapping("/counterOfferBid")
    public String counterOfferBid(@RequestParam("productId") int productId,
                                  @RequestParam("userName") String userName,
                                  @RequestParam("newPrice") double newPrice,
                                  @RequestParam("message") String message,
                                  Model model) {

        Response<?> response = server.counterOfferBid(request, productId, storeID, userName, newPrice, message);
        if (response.isError()) {
            System.out.println("counterOfferBid failed." + response.getMessage());
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            System.out.println("counterOfferBid success.");
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "MyStore";
    }
}
