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

import javax.servlet.http.HttpSession;

@Controller
public class MainPageController{
    Alert alert = Alert.getInstance();
//    private final Server server = Server.getInstance();
    Server server = new Server();
    private static boolean isInitialized = false;

    @GetMapping("/")
    public String mainPage(HttpSession session, Model model) throws Exception {
        if (!isInitialized){
            Response<?> response = server.loadData();
            System.out.println("Loading Data ... ");
            if (response.isError()){
                alert.setFail(true);
                alert.setMessage(response.getMessage());
                model.addAttribute("alert", alert.copy());
                return "/";
            }
            isInitialized = true;
        }

        Response<Integer> responseId = server.EnterNewSiteVisitor();
        System.out.println("user logged in with id: " + responseId.getValue());
        session.setAttribute("userId", responseId.getValue());
        model.addAttribute("alert", alert.copy());
        alert.reset();

        return "MainPage";
    }

//    @MessageMapping("/websocket-endpoint")
//    @SendTo("/topic/messages")
//    public String handleWebSocketMessage(String message) {
//        // Logic to handle the received message
//        return "Response from server";
//    }

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
    public String signIn(HttpSession session,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model) throws Exception {

        Response<?> response = server.login(username, password);
        System.out.println(response.getMessage());
        if (response.isError()) {
            model.addAttribute("logged", server.isLogged());
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            server.setLogged(true);
            alert.setSuccess(true);

            session.setAttribute("username", username);

            model.addAttribute("logged", server.isLogged());
            if (server.isAdmin().getValue()) {
                model.addAttribute("Admin", true);
                alert.setMessage("HELLO " + username);
                model.addAttribute("alert", alert.copy());
            }

            if(!server.checkForNewMessages().isError()){
                alert.setMessage("Hey " + username + "! You Have New Messages.");
            } else alert.setMessage("WELCOME TO OUR STORE");
            model.addAttribute("alert", alert.copy());
        }
        alert.reset();
        return ("MainPage");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("register-name") String username,
                           @RequestParam("register-password") String password,
                           Model model) {

        Response<?> response = server.Register(username, password);
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
    public String logout(HttpSession session, Model model) {
        Response<?> response = server.logout();
        if (response.isError()){
            alert.setFail(true);
            alert.setMessage(response.getMessage());
            model.addAttribute("alert", alert.copy());
        } else {
            alert.setSuccess(true);
            alert.setMessage("Logging out..");
            model.addAttribute("alert", alert.copy());
        }
        model.addAttribute("logged", server.isLogged());
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

        model.addAttribute("alert", alert.copy());
        alert.reset();
        Response<Integer> response = server.OpenStore(storeName);
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
        model.addAttribute("logged", server.isLogged());
        alert.reset();
        return "MainPage";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Page Not Found");
        return "error";
    }
}
