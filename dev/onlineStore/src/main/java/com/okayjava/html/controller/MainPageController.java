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
import org.thymeleaf.model.IModel;

import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Controller
public class MainPageController extends Thread{
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();
    private static boolean isInitialized = false;
    Model m;

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
//        server.EnterNewSiteVisitor();
        System.out.println("user logged in with id: " + server.EnterNewSiteVisitor().getValue());
        m = model;
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

            model.addAttribute("logged", server.isLogged());
            if (server.isAdmin().getValue()) {
                model.addAttribute("Admin", true);
                alert.setMessage("HELLO " + username);
                model.addAttribute("alert", alert.copy());
            }
            else {
                //alert.setMessage("SHALOM " + username);
            }
//            (!server.checkForNewMessages().isError()){
//               // alert.setMessage("Hey " + username + "! You Have New Messages.");
//            } else alert.setMessage("WELCOME TO OUR STORE");
            alert.setMessage("HELLO2 " + username);
            model.addAttribute("alert", alert.copy());
            alert.reset();
            new MyThread(model).start();
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
    public String logout(Model model) {
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
        //alert.reset();
        return "MainPage";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Page Not Found");
        return "error";
    }

//    @Override
//    public void run() {

//    }


    class MyThread extends Thread {

        private Model model1;

        public MyThread(Model model1) {
            this.model1 = model1;
        }

        @Override
        public void run() {
            while (true) {

                try {
                    Response r = server.checkForNewMessages();
                    if (!r.isError() && r.getValue().equals(true)) {
                    //if (!r.isError()) {
                        alert.setSuccess(true);
                        System.out.println("You Have New Messages BDIKA!" + LocalDateTime.now());
                        alert.setMessage("You Have New Messages BDIKA!" + LocalDateTime.now());
                        model1.addAttribute("alert", alert.copy());

                        server.markMessagesAsRead();
                        //alert.reset();
                    }
                    else {
//                        alert.setSuccess(true);
//                        System.out.println("NO NEW MESSAGES :(" + LocalDateTime.now());
//                        alert.setMessage("NO NEW MESSAGES :(" + LocalDateTime.now());
//                        model1.addAttribute("alert", alert.copy());

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}




