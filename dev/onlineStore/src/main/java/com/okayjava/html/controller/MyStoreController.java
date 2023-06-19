package com.okayjava.html.controller;

import DomainLayer.Response;
import DomainLayer.Stores.Conditions.BasicConditions.FilterConditions.NameCondition;
import DomainLayer.Stores.Discounts.AdditiveDiscount;
import DomainLayer.Stores.Discounts.BasicDiscount;
import ServiceLayer.ServiceObjects.Fiters.ProductFilters.MinPriceProductFilter;
import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.*;
import ServiceLayer.ServiceObjects.ServiceDiscounts.*;
import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Controller
public class MyStoreController {
    @Autowired
    private HttpServletRequest request;

    Alert alert = Alert.getInstance();
    private Server server = Server.getInstance();
    private int storeID;

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
        this.storeID = storeId;
        alert.reset();
        return "MyStore";
    }

    @PostMapping("/discount_option")
    public String processDiscountOption(@RequestParam("discountId") List<Integer> discountIds,
                                        @RequestParam("description") String description,
                                        @RequestParam("amount") Integer amount,
                                        @RequestParam("condition") String condition) {

        Integer id1 = discountIds.get(0); //first selected discount
        Integer id2 = discountIds.get(1); // second selected discount

        ServiceMultiDiscount serviceMultiDiscount;
        ServiceBasicDiscount serviceBasicDiscount;

        if (discountIds.size() == 2){
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
        } else if (discountIds.size() > 2) {
            if (condition.equals("MinBetween")) {
                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.MinBetweenDiscount, description, discountIds);
                server.addDiscount(request, serviceMultiDiscount, storeID);
            } else if (condition.equals("Additive")) {
                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.AdditiveDiscount, description, discountIds);
                server.addDiscount(request, serviceMultiDiscount, storeID);
            } else if (condition.equals("MaxBetween")) {
                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.MaxBetweenDiscounts, description, discountIds);
                server.addDiscount(request, serviceMultiDiscount, storeID);
            }
        }
        return "redirect:/MyStore/";
    }

    @PostMapping("/add_discount")
    public String addNewDiscount(@RequestParam("conditionSelect") String condition,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "endDate", required = false) String endDate,
                                 @RequestParam(value = "date", required = false) String date,
                                 @RequestParam(value = "startHour", required = false) String startHour,
                                 @RequestParam(value = "endHour", required = false) String endHour,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "maxBagPrice", required = false) Double maxBagPrice,
                                 @RequestParam(value = "minBagPrice", required = false) Double minBagPrice,
                                 @RequestParam(value = "maxTotalProductAmount", required = false) Double maxTotalProductAmount,
                                 @RequestParam(value = "minTotalProductAmount", required = false) Integer minTotalProductAmount,
                                 @RequestParam("description") String description,
                                 @RequestParam("amount") Integer amount) {
        System.out.println("1");
        switch (condition) {
            case "BetweenDatesCondition":
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
                break;
            case "DateCondition":
                if (date != null) {
                    // Handle DateCondition
                }
                break;
            case "LocalHourRangeCondition":
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
                break;
            case "CategoryCondition":
                if (category != null) {
                    CategoryConditionRecord categoryConditionRecord = new CategoryConditionRecord(category);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, categoryConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
                break;
            case "NameCondition":
                if (name != null) {
                    NameConditionRecord nameConditionRecord = new NameConditionRecord(name);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, nameConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
                break;
            case "MaxBagPriceCondition":
                if (maxBagPrice != null) {
                    MaxBagPriceConditionRecord maxBagPriceConditionRecord = new MaxBagPriceConditionRecord(maxBagPrice);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, maxBagPriceConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
                break;
            case "MinBagPriceCondition":
                if (minBagPrice != null) {
                    MinBagPriceConditionRecord minBagPriceConditionRecord = new MinBagPriceConditionRecord(minBagPrice);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, minBagPriceConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
                break;
            case "MaxTotalProductAmountCondition":
                if (maxTotalProductAmount != null) {
                    MaxTotalProductAmountConditionRecord maxTotalProductAmountConditionRecord =
                            new MaxTotalProductAmountConditionRecord(maxTotalProductAmount);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, maxTotalProductAmountConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
                break;
            case "MinTotalProductAmountCondition":
                if (minTotalProductAmount != null) {
                    MinTotalProductAmountConditionRecord minTotalProductAmountConditionRecord =
                            new MinTotalProductAmountConditionRecord(minTotalProductAmount);
                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, minTotalProductAmountConditionRecord);
                    server.addDiscount(request, serviceBasicDiscount, storeID);
                }
                break;
            default:
                break;
        }

        return "redirect:/MyStore/";
    }

    @RequestMapping(value="/dailyStoreIncome", method = RequestMethod.POST)
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
        alert.reset();
        return "MyStore";
    }
}
