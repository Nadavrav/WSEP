//package com.okayjava.html.controller;
//
//import DomainLayer.Response;
//import DomainLayer.Stores.Conditions.ComplexConditions.CompositeConditions.FilterOnlyIfCondition;
//import ServiceLayer.ServiceObjects.ServiceBid;
//import ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords.*;
//import ServiceLayer.ServiceObjects.ServiceDiscounts.*;
//import ServiceLayer.ServiceObjects.ServicePolicies.ServiceBasicPolicy;
//import ServiceLayer.ServiceObjects.ServicePolicies.ServicePolicy;
//import com.okayjava.html.CommunicateToServer.Alert;
//import com.okayjava.html.CommunicateToServer.Server;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.CompositeMessageCondition;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
//@Controller
//public class MyStoreController {
//    @Autowired
//    private HttpServletRequest request;
//
//    Alert alert = Alert.getInstance();
//    private Server server = Server.getInstance();
//    private int storeID;
//    private String StoreName;
//    private Collection<ServiceDiscountInfo> discountInfo;
//    private HashSet<ServicePolicy> policyInfo;
//
//    @GetMapping("/MyStore/{storeId}")
//    public String myStoreMenu(Model model,
//                              @PathVariable("storeId") int storeId,
//                              @RequestParam("storeName") String storeName) {
//
//        this.storeID = storeId;
//        this.StoreName = storeName;
//
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("storeName", storeName);
//
//        // store discounts
//        Response<Collection<ServiceDiscountInfo>> responseDiscount = server.getStoreDiscountInfo(request, storeID);
//        if (responseDiscount.isError()){
//            alert.setFail(true);
//            alert.setMessage(responseDiscount.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            alert.setSuccess(true);
//            alert.setMessage(responseDiscount.getMessage());
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("discountInfo", responseDiscount.getValue());
//            discountInfo = responseDiscount.getValue();
//            System.out.println("discounts for storeId " + storeId + " :" + responseDiscount.getValue());
//        }
//
//        // store policies
//        Response<HashSet<ServicePolicy>> responsePolicy = server.getStorePolicy(request, storeId);
//        if (responsePolicy.isError()){
//            alert.setFail(true);
//            alert.setMessage(responsePolicy.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            alert.setSuccess(true);
//            alert.setMessage(responsePolicy.getMessage());
//            model.addAttribute("alert", alert.copy());
//            policyInfo = responsePolicy.getValue();
//            model.addAttribute("policyInfo", responsePolicy.getValue());
//            System.out.println("Policy infor for storeId " + storeId + " :" + responsePolicy.getValue());
//        }
//
//        // appointment request
//        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
//        if (!responseRequest.isError()) {
//            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
//            model.addAttribute("appointmentRequests", appointmentRequests);
//            System.out.println("Appointment Requests: " + appointmentRequests);
//        }
//
//        // Bid
//        Response<Collection<ServiceBid>> responseBid = server.geStoreBids(request, storeId);
//        if (!responseBid.isError()) {
//            System.out.println(storeId);
//            Collection<ServiceBid> bidRequests = responseBid.getValue();
//            model.addAttribute("bidRequests", bidRequests);
//            System.out.println("Bid Requests: " + bidRequests);
//        }
//
//        // purchase history
//        Response<List<String>> responsePurchaseHistory = server.GetStoreHistoryPurchase(request, storeId);
//        if (!responsePurchaseHistory.isError()) {
//            System.out.println("Purchase History should be displayed! " + responsePurchaseHistory.getValue());
//            alert.setSuccess(true);
//            alert.setMessage(responsePurchaseHistory.getMessage());
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("purchaseHistory", responsePurchaseHistory.getValue());
//        }
//
//        // employee info
//        Response<?> responseEmployeeInfo = server.getRolesData(request, storeId);
//        if (!responseEmployeeInfo.isError()) {
//            alert.setSuccess(true);
//            alert.setMessage("Employee Info for StoreId: " + storeId);
//            model.addAttribute("alert", alert.copy());
//            model.addAttribute("employeeInfo", responseEmployeeInfo.getValue()); //String
//        }
//
//        alert.reset();
//        return "MyStore";
//    }
//
//    @GetMapping("/policies-and-discounts")
//    public String getPoliciesAndDiscounts(Model model) {
//        model.addAttribute("policyInfo", policyInfo);
//        model.addAttribute("discountInfo", discountInfo);
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("storeName", StoreName);
//        return "MyStore";
//    }
//
//    @PostMapping("/add-discount")
//    public String processDiscountOption(@RequestParam("discountId") Integer[] discountIds,
//                                        @RequestParam("description") String description,
//                                        @RequestParam("amount") Integer amount,
//                                        @RequestParam("condition") String condition,
//                                        Model model) {
//
//        Integer id1 = discountIds[0]; //first selected discount
//        Integer id2 = discountIds[1]; // second selected discount
//
//        ServiceMultiDiscount serviceMultiDiscount;
//        ServiceBasicDiscount serviceBasicDiscount;
//
//        if (discountIds.length == 2){
//            if (condition.equals("AndCondition")) {
//                AndConditionRecord andConditionRecord = new AndConditionRecord(id1, id2);
//                serviceBasicDiscount = new ServiceBasicDiscount(description, amount, andConditionRecord);
//                server.addDiscount(request, serviceBasicDiscount, storeID);
//            } else if (condition.equals("OrCondition")) {
//                OrConditionRecord orConditionRecord = new OrConditionRecord(id1, id2);
//                serviceBasicDiscount = new ServiceBasicDiscount(description, amount, orConditionRecord);
//                server.addDiscount(request, serviceBasicDiscount, storeID);
//            } else if (condition.equals("XorCondition")) {
//                XorConditionRecord xorConditionRecord = new XorConditionRecord(id1, id2);
//                serviceBasicDiscount = new ServiceBasicDiscount(description, amount, xorConditionRecord);
//                server.addDiscount(request, serviceBasicDiscount, storeID);
//            }
//        } else if (discountIds.length > 2) {
//            if (condition.equals("MinBetween")) {
//                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.MinBetweenDiscount, description, Arrays.asList(discountIds));
//                server.addDiscount(request, serviceMultiDiscount, storeID);
//            } else if (condition.equals("Additive")) {
//                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.AdditiveDiscount, description, Arrays.asList(discountIds));
//                server.addDiscount(request, serviceMultiDiscount, storeID);
//            } else if (condition.equals("MaxBetween")) {
//                serviceMultiDiscount = new ServiceMultiDiscount(DiscountType.MaxBetweenDiscounts, description, Arrays.asList(discountIds));
//                server.addDiscount(request, serviceMultiDiscount, storeID);
//            }
//        }
//        discountInfo = server.getStoreDiscountInfo(request, storeID).getValue();
//        System.out.println("updated discountInfo: " + discountInfo);
//        return "redirect:/policies-and-discounts";
//    }
//
//    @PostMapping("/add-basic-discount")
//    public String addNewDiscount(@RequestParam("conditionSelect") String condition,
//                                 @RequestParam(value = "startDate", required = false) String startDate,
//                                 @RequestParam(value = "endDate", required = false) String endDate,
//                                 @RequestParam(value = "startHour", required = false) String startHour,
//                                 @RequestParam(value = "endHour", required = false) String endHour,
//                                 @RequestParam(value = "category", required = false) String category,
//                                 @RequestParam(value = "name", required = false) String name,
//                                 @RequestParam(value = "maxBagPrice", required = false) Double maxBagPrice,
//                                 @RequestParam(value = "minBagPrice", required = false) Double minBagPrice,
//                                 @RequestParam(value = "maxTotalProductAmount", required = false) Double maxTotalProductAmount,
//                                 @RequestParam(value = "minTotalProductAmount", required = false) Integer minTotalProductAmount,
//                                 @RequestParam("description") String description,
//                                 @RequestParam("amount") Integer amount,
//                                 Model model) {
//
//        switch (condition) {
//            case "BetweenDatesCondition" -> {
//                if (startDate != null && endDate != null) {
//                    String[] startDateParts = startDate.split("-");
//                    String[] endDateParts = endDate.split("-");
//
//                    int fromDay = Integer.parseInt(startDateParts[2]);
//                    int fromMonth = Integer.parseInt(startDateParts[1]);
//                    int fromYear = Integer.parseInt(startDateParts[0]);
//
//                    int untilDay = Integer.parseInt(endDateParts[2]);
//                    int untilMonth = Integer.parseInt(endDateParts[1]);
//                    int untilYear = Integer.parseInt(endDateParts[0]);
//
//                    BetweenDatesConditionRecord betweenDatesConditionRecord =
//                            new BetweenDatesConditionRecord(fromDay, fromMonth, fromYear, untilDay, untilMonth, untilYear);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, betweenDatesConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            case "LocalHourRangeCondition" -> {
//                if (startHour != null && endHour != null) {
//                    String[] startHourParts = startHour.split(":");
//                    String[] endHourParts = endHour.split(":");
//
//                    int lowerBoundaryHour = Integer.parseInt(startHourParts[0]);
//                    int lowerBoundaryMin = Integer.parseInt(startHourParts[1]);
//
//                    int upperBoundaryHour = Integer.parseInt(endHourParts[0]);
//                    int upperBoundaryMin = Integer.parseInt(endHourParts[1]);
//
//                    LocalHourRangeConditionRecord localHourRangeConditionRecord =
//                            new LocalHourRangeConditionRecord(lowerBoundaryHour, lowerBoundaryMin, upperBoundaryHour, upperBoundaryMin);
//
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, localHourRangeConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            case "CategoryCondition" -> {
//                if (category != null) {
//                    CategoryConditionRecord categoryConditionRecord = new CategoryConditionRecord(category);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, categoryConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            case "NameCondition" -> {
//                if (name != null) {
//                    System.out.println("in name condition: " + name);
//                    NameConditionRecord nameConditionRecord = new NameConditionRecord(name);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, nameConditionRecord);
////                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                    System.out.println("after adding disocunt: " + server.addDiscount(request, serviceBasicDiscount, storeID).getValue());
//                }
//            }
//            case "MaxBagPriceCondition" -> {
//                if (maxBagPrice != null) {
//                    MaxBagPriceConditionRecord maxBagPriceConditionRecord = new MaxBagPriceConditionRecord(maxBagPrice);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, maxBagPriceConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            case "MinBagPriceCondition" -> {
//                if (minBagPrice != null) {
//                    MinBagPriceConditionRecord minBagPriceConditionRecord = new MinBagPriceConditionRecord(minBagPrice);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, minBagPriceConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            case "MaxTotalProductAmountCondition" -> {
//                if (maxTotalProductAmount != null) {
//                    MaxTotalProductAmountConditionRecord maxTotalProductAmountConditionRecord =
//                            new MaxTotalProductAmountConditionRecord(maxTotalProductAmount);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, maxTotalProductAmountConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            case "MinTotalProductAmountCondition" -> {
//                if (minTotalProductAmount != null) {
//                    MinTotalProductAmountConditionRecord minTotalProductAmountConditionRecord =
//                            new MinTotalProductAmountConditionRecord(minTotalProductAmount);
//                    ServiceBasicDiscount serviceBasicDiscount = new ServiceBasicDiscount(description, amount, minTotalProductAmountConditionRecord);
//                    server.addDiscount(request, serviceBasicDiscount, storeID);
//                }
//            }
//            default -> {
//            }
//        }
//        alert.reset();
//        discountInfo = server.getStoreDiscountInfo(request, storeID).getValue();
//        System.out.println("updated discountInfo: " + discountInfo);
//        return "redirect:/policies-and-discounts";
//    }
//
////    @PostMapping ("add-policy")
////    public String addNewPolicy(@RequestParam("policyConditionSelect") String condition,
////                               @RequestParam("policyDescription") String description,
////                               @RequestParam(value = "message", required = false) String message,
////                               Model model){
////
////        switch (condition) {
////            case "CompositeCondition" -> {
////                if (message != null) {
////                    ConditionRecord conditionRecord = new CompositeMessageCondition();
////                    ServicePolicy servicePolicy = new ServiceBasicPolicy(description, conditionRecord);
////                    server.addPolicy(request, servicePolicy, storeID);
////                }
////            }
////            case "OnlyIfCondition" -> {
////                    FilterOnlyIfCondition filterOnlyIfCondition = new FilterOnlyIfCondition()
////            }
////            case "FilterCondition" -> {
////
////            }
////            default -> {
////            }
////        }
////        alert.reset();
////        policyInfo = server.getStorePolicy(request, storeID).getValue();
////        System.out.println("updated policyInfo: " + policyInfo);
////        return "redirect:/policies-and-discounts";
////    }
//
//    @RequestMapping(value="/dailyStoreIncome", method = RequestMethod.GET)
//    public String showDailyIncomeToStore(@RequestParam("date") String dateString,
//                                         Model model) {
//
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("storeName", StoreName);
//
//        String[] dateParts = dateString.split("-");
//        int year = Integer.parseInt(dateParts[0]);
//        int month = Integer.parseInt(dateParts[1]);
//        int day = Integer.parseInt(dateParts[2]);
//        System.out.println(month + "/"+ day +"/"+ year);
//        Response<Integer> response = server.getDailyIncomeByStore(request, day, month, year, storeID);
//        if (response.isError()) {
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//            System.out.println("income for storeId " + storeID + " : " + response.getValue());
//            model.addAttribute("dailyIncome", response.getValue());
//        }
//
//        alert.reset();
////        return "MyStore";
//        return "redirect:/MyStore/" + storeID + "?storeName=" + StoreName;
//    }
//
//    @PostMapping("/acceptBid")
//    public String acceptBid(@RequestParam("productId") int productId,
//                            @RequestParam("userName") String userName,
//                            Model model) {
//
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("storeName", StoreName);
//
//        Response<?> response = server.voteOnBid(request, productId, storeID, userName, true);
//        if (response.isError()) {
//            System.out.println("acceptBid success.");
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            System.out.println("acceptBid success.");
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        }
//        alert.reset();
////        return "MyStore";
//        return "redirect:/MyStore/" + storeID + "?storeName=" + StoreName;
//    }
//
//    @PostMapping("/declineBid")
//    public String declineBid(@RequestParam("productId") int productId,
//                             @RequestParam("userName") String userName,
//                             Model model) {
//
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("storeName", StoreName);
//
//        Response<?> response = server.voteOnBid(request, productId, storeID, userName, false);
//        if (response.isError()) {
//            System.out.println("declineBid success.");
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            System.out.println("declineBid success.");
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        }
//        alert.reset();
////        return "MyStore";
//        return "redirect:/MyStore/" + storeID + "?storeName=" + StoreName;
//    }
//
//    @PostMapping("/counterOfferBid")
//    public String counterOfferBid(@RequestParam("productId") int productId,
//                                  @RequestParam("userName") String userName,
//                                  @RequestParam("newPrice") double newPrice,
//                                  @RequestParam("message") String message,
//                                  Model model) {
//
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("Admin", server.isAdmin(request).getValue());
//        model.addAttribute("storeName", StoreName);
//
//        Response<?> response = server.counterOfferBid(request, productId, storeID, userName, newPrice, message);
//        if (response.isError()) {
//            System.out.println("counterOfferBid failed." + response.getMessage());
//            alert.setFail(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        } else {
//            System.out.println("counterOfferBid success.");
//            alert.setSuccess(true);
//            alert.setMessage(response.getMessage());
//            model.addAttribute("alert", alert.copy());
//        }
//        alert.reset();
////        return "MyStore";
//        return "redirect:/MyStore/" + storeID + "?storeName=" + StoreName;
//    }
//}
