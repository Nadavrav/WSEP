package com.okayjava.html.controller;

import DomainLayer.Response;

import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class MainPageController{
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();
    private static boolean isInitialized = false;

    @GetMapping("/")
    public String mainPage(Model model) throws Exception {
        if (!isInitialized){
            Response<?> response = server.loadData(request);
            System.out.println("Loading Data ... ");
            if (response.isError()){
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                return "/";
            }
            isInitialized = true;
        }
        Response<Integer> responseId = server.EnterNewSiteVisitor(request);
        System.out.println("user logged in with id: " + responseId.getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();
        return "MainPage";
    }

        @GetMapping("MainPage")
        public String reMainPage(Model model) {
            model.addAttribute("alert", alert.copy());
            model.addAttribute("logged", server.isLogged(request));
            model.addAttribute("name", server.getUsername(request));
            if (server.isAdmin(request).getValue()) {
                model.addAttribute("Admin", true);
            }

            Response<Map<Integer, List<String>>> response = server.getAppointmentRequests(request);
            if (!response.isError()) {
                Map<Integer, List<String>> appointmentRequests = response.getValue();
                model.addAttribute("appointmentRequests", appointmentRequests);
                System.out.println("Appointment Requests: " + appointmentRequests);
            }

            alert.reset();
            return "MainPage";
        }
//    @GetMapping("MainPage")
//    public String reMainPage(Model model) {
//        model.addAttribute("alert", alert.copy());
//        model.addAttribute("logged", server.isLogged(request));
//        model.addAttribute("name", server.getUsername(request));
//        if (server.isAdmin(request).getValue()) {
//            model.addAttribute("Admin", true);
//             }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signIn(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model) throws Exception {

        Response<?> response = server.login(request,username, password);
        if (response.isError()) {
            model.addAttribute("logged", server.isLogged(request));
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);

            model.addAttribute("logged", server.isLogged(request));
            model.addAttribute("name", username);
            if (server.isAdmin(request).getValue()) {
                model.addAttribute("Admin", true);
                alert.setMessage("HELLO " + username);
                model.addAttribute("alert", alert.copy());
            }

            if(!server.checkForNewMessages(request).isError()){
                alert.setMessage("Hey " + username + "! You Have New Messages.");
            } else alert.setMessage("WELCOME TO OUR STORE");
            model.addAttribute("alert", alert.copy());

            Response<Map<Integer, List<String>>> responseRequest = server.getAppointmentRequests(request);
            if (!responseRequest.isError()) {
                Map<Integer, List<String>> appointmentRequests = responseRequest.getValue();
                model.addAttribute("appointmentRequests", appointmentRequests);
                System.out.println("Appointment Requests: " + appointmentRequests);
            }
        }
        alert.reset();
        return ("MainPage");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("register-name") String username,
                           @RequestParam("register-password") String password,
                           Model model) {

        Response<?> response = server.Register(request,username, password);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage("Registered Successfully!");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "MainPage";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        Response<?> response = server.logout(request);
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage("Logging out..");
            model.addAttribute("alert", alert.copy());
            System.out.println("logged success");
        }
        model.addAttribute("logged", server.isLogged(request));
        System.out.println("logged valus is: " + server.isLogged(request));
        alert.reset();
        return "MainPage";
    }

    @RequestMapping(value = "/complaints", method = RequestMethod.POST)
    public String complaints(@RequestParam("complaints") String message,
                             Model model) {

        return "MainPage";
    }

    @RequestMapping(value = "/open-store", method = RequestMethod.POST)
    public String openStore(@RequestParam("store-name") String storeName,
                            Model model) {

//        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<Integer> response = server.OpenStore(request,storeName);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(storeName + " Store is Opened - ID - " + response.getValue());
            model.addAttribute("alert", alert.copy());
            System.out.println("Store is opened successfully with id: " + response.getValue());
        }
        model.addAttribute("logged", server.isLogged(request));
        alert.reset();
        return "MainPage";
    }

    @PostMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("storeId") int storeId,
                                @RequestParam("owner") String owner,
                                Model model) {

        Response<?> response = server.acceptAppointment(request, storeId, owner);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/MainPage";
    }

    @PostMapping("/declineRequest")
    public String declineRequest(@RequestParam("storeId") int storeId,
                                 @RequestParam("owner") String owner,
                                 Model model) {
        Response<?> response = server.declineAppointment(request, storeId, owner);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "redirect:/MainPage";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Page Not Found");
        return "error";
    }
}
