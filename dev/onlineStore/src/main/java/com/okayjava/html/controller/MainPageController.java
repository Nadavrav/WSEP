package com.okayjava.html.controller;

import DomainLayer.Response;
import ServiceLayer.*;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static DomainLayer.Users.Role.StoreFounder;

@Controller
public class MainPageController {
    private Server server = Server.getInstance();

    @GetMapping("/")
    public String mainPage(Model model) throws Exception {
        server.loadData();
        server.EnterNewSiteVisitor();
        System.out.println("user logged in with id: " + server.EnterNewSiteVisitor().getValue());
		System.out.println("Loading Data ... ");
        return "MainPage";
    }

    @GetMapping("MainPage")
    public String reMainPage(Model model) {
        model.addAttribute("logged", server.isLogged());

        return "MainPage";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signIn(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model) {

        Response<?> response = server.login(username, password);
        if (response.isError()) {
            System.out.println("error in login ");
            model.addAttribute("logged", server.isLogged());
            model.addAttribute("isError", true);
//            model.addAttribute("showModal", true);
//            model.addAttribute("activeModal", "loginModal");
            model.addAttribute("errorMessageLogin", response.getMessage());
//            return "error";
        } else {
            server.setLogged(true);
            model.addAttribute("logged", server.isLogged());
            if (server.isAdmin().getValue()) {
                model.addAttribute("Admin", true);
            }
        }
        return ("MainPage");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("register-name") String username,
                           @RequestParam("register-password") String password,
                           Model model) {

        Response<?> response = server.Register(username, password);
        if (response.isError()) {
            System.out.println("Error in register!!!");
            model.addAttribute("isError", true);
//            model.addAttribute("showModal", true);
//            model.addAttribute("activeModal", "registerModal");
            model.addAttribute("errorMessageRegister", response.getMessage());
//            return "error";
        } else {
            System.out.println("Registered Successfully\n");
        }
        return "MainPage";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        server.logout();
        model.addAttribute("logged", server.isLogged());
        System.out.println("logging out!\n" + "Status of logged:" + server.isLogged());
        return "MainPage";
    }

    @RequestMapping(value = "/complaints", method = RequestMethod.POST)
    public String complaints(@RequestParam("complaints") String message,
                             Model model) {
//		Response response = server.complaint(message);
//		if (response.isError()) {
//			model.addAttribute("isError", true);
//		model.addAttribute("showModal", true);
        model.addAttribute("showModal", true);
        model.addAttribute("activeModal", "complaintModal");
        model.addAttribute("complaintMessage", "Your Complaint Is On Processing.."); //change to response.getMessage()
//		}
        return "redirect:/MainPage";
    }

    @RequestMapping(value = "/open-store", method = RequestMethod.POST)
    public String openStore(@RequestParam("store-name") String storeName,
                            Model model) {

        Response<Integer> response = server.OpenStore(storeName);
        if (response.isError()) {
            System.out.println("Error with opening store.");
            System.out.println("response.getValue = " + response.getValue() + "," + response.getMessage());
            model.addAttribute("isError", true);
//            model.addAttribute("showModal", true);
//            model.addAttribute("activeModal", "storeModal");
            model.addAttribute("errorMessage", response.getMessage());
//            return "error";
        } else {
            model.addAttribute("message", response.getMessage());
            System.out.println("Store is opened successfully with id: " + response.getValue());
        }
        return "redirect:/MainPage";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Page Not Found");
        return "error";
    }
}
