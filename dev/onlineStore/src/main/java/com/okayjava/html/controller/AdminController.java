package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();

    @GetMapping("Admin")
    public String adminPage(Model model) {
        model.addAttribute("logged", server.isLogged(request));
        model.addAttribute("Admin", server.isAdmin(request).getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();

        Response<List<ServiceUser>> response = server.getOnlineUsers(request);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("onlineUsersInfo", response.getValue());
        }

        Response<List<ServiceUser>> responseOff = server.getOfflineUsers(request);
        if (responseOff.isError()) {
            alert.setFail(true);
            alert.setMessage(responseOff.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(responseOff.getMessage());
            model.addAttribute("offlineUsersInfo", responseOff.getValue());
        }

        Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
        if (!responseRequest.isError()) {
            Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
            model.addAttribute("appointmentRequests", appointmentRequests);
            System.out.println("Appointment Requests: " + appointmentRequests);
        }
        return "Admin";
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("usernameToDelete") String username,
                             Model model) {

        Response<?> response = server.deleteUser(request,username);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(username + " Is Deleted!");
            model.addAttribute("alert", alert.copy());
        }

        return "redirect:/Admin";
    }

    @RequestMapping(value="/daily-income", method = RequestMethod.POST)
    public String showDailyIncome(@RequestParam("date") String dateString,
                                  Model model) {

        String[] dateParts = dateString.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        System.out.println(month + "/"+ day +"/"+ year);
        Response<Integer> response = server.getDailyIncome(request, day, month, year);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("System Income: " + response.getValue());
            model.addAttribute("dailyIncome", response.getValue());
        }
        alert.reset();
        return "Admin";
    }

    @RequestMapping(value="/daily-store-income", method = RequestMethod.POST)
    public String showDailyIncomeForStore(@RequestParam("storeId") int storeId,
                                          @RequestParam("date") String dateString,
                                          Model model) {

        String[] dateParts = dateString.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        System.out.println(month + "/"+ day +"/"+ year);
        Response<Integer> response = server.getDailyIncomeByStore(request, day, month, year, storeId);
        if (response.isError()) {
            System.out.println("error message: " + response.getMessage());
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Income for store " + storeId + " :" + response.getValue());
            model.addAttribute("dailyStoreIncome", response.getValue());
        }
        alert.reset();
        return "Admin";
    }

    @RequestMapping(value = "/notification-history", method = RequestMethod.POST)
    public String showNotifications(Model model) {
        alert.reset();
        return "redirect:/Admin";
    }
}
