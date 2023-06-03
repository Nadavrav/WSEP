package com.okayjava.html.controller;

import DomainLayer.Response;

import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainPageController {
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();
    private static boolean isInitialized = false;

    @GetMapping("/")
    public String mainPage(Model model) throws Exception {
        if (!isInitialized){
            Response<?> response = server.loadData();
            System.out.println("Loading Data ... ");
            if (response.isError()){
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
            }
            isInitialized = true;
        }
        model.addAttribute("alert", alert.copy());
        server.EnterNewSiteVisitor();
        System.out.println("user logged in with id: " + server.EnterNewSiteVisitor().getValue());
        alert.reset();
        return "MainPage";
    }

    @GetMapping("MainPage")
    public String reMainPage(Model model) {
        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged());
        if (server.isAdmin().getValue()) {
            model.addAttribute("Admin", true);
        }
        alert.reset();
        return "MainPage";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signIn(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model) {

        Response<?> response = server.login(username, password);
        System.out.println(response.getMessage());
        if (response.isError()) {
            System.out.println("error in login ");
            model.addAttribute("logged", server.isLogged());
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            server.setLogged(true);
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            System.out.println(alert.getMessage());
            model.addAttribute("logged", server.isLogged());
            model.addAttribute("alert", alert.copy());
            if (server.isAdmin().getValue()) {
                model.addAttribute("Admin", true);
            }
        }
        alert.reset();
        return ("MainPage");
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("usernameToDelete") String username,
                             Model model) {

        Response<?> response = server.deleteUser(username);
        if (response.isError()) {
            System.out.println("error in deleting user ");
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage("User Is Deleted");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return ("redirect:/MainPage");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("register-name") String username,
                           @RequestParam("register-password") String password,
                           Model model) {

        Response<?> response = server.Register(username, password);
        if (response.isError()) {
            System.out.println("Error in register!!!");
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            System.out.println("Registered Successfully\n");
            alert.setSuccess(true);
            alert.setMessage("Registered Successfully!");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return "MainPage";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        Response response = server.logout();
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("logging out!\n" + "Status of logged:" + server.isLogged());
        }
        model.addAttribute("logged", server.isLogged());
        alert.reset();
        return "MainPage";
    }

    @RequestMapping(value = "/complaints", method = RequestMethod.POST)
    public String complaints(@RequestParam("complaints") String message,
                             Model model) {

        return "redirect:/MainPage";
    }

    @RequestMapping(value = "/open-store", method = RequestMethod.POST)
    public String openStore(@RequestParam("store-name") String storeName,
                            Model model) {

        Response<Integer> response = server.OpenStore(storeName);
        if (response.isError()) {
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Error with opening store.");
        } else {
            alert.setSuccess(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
            System.out.println("Store is opened successfully with id: " + response.getValue());
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
