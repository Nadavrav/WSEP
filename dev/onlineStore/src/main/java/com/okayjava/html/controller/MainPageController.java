package com.okayjava.html.controller;

import DomainLayer.Response;

import com.okayjava.html.CommunicateToServer.Alert;
import com.okayjava.html.CommunicateToServer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.model.IModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class MainPageController{
    @Autowired
    private HttpServletRequest request;
    Alert alert = Alert.getInstance();
    private final Server server = Server.getInstance();
    private static boolean isInitialized = false;
    Model m;
//    @Resource
//    private HttpServletRequest request;

    @GetMapping("/")
    public String mainPage(Model model) throws Exception {
        if (!isInitialized){
//            HttpSession session = request.getSession();
//            String sessionId = session.getId();
//            server.initNewSession(sessionId);
            Response<?> response = server.loadData(request);
            System.out.println("Loading Data ... ");
           // if (response.isError()){
           //     alert.setFail(true);
           //     alert.setMessage(response.getMessage());
           //     model.addAttribute("alert", alert.copy());
           // }
            isInitialized = true;
        }
        model.addAttribute("alert", alert.copy());
//        server.EnterNewSiteVisitor();
        System.out.println("user logged in with id: " + server.EnterNewSiteVisitor(request).getValue());
        m = model;
        alert.reset();
        return "MainPage";
    }

    @GetMapping("MainPage")
    public String reMainPage(Model model) {
        model.addAttribute("alert", alert.copy());
        model.addAttribute("logged", server.isLogged());
        if (server.isAdmin(request).getValue()) {
            model.addAttribute("Admin", true);
        }
        alert.reset();
        return "MainPage";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signIn(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Model model) throws Exception {

        Response<?> response = server.login(request,username, password);
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
            if (server.isAdmin(request).getValue()) {
                model.addAttribute("Admin", true);
                alert.setMessage("HELLO " + username);
                model.addAttribute("alert", alert.copy());
            }
//            Thread T1= new Thread();
//            T1.start();
            if(!server.checkForNewMessages(request).isError()){
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
        model.addAttribute("logged", server.isLogged());
        alert.reset();
        return "MainPage";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Page Not Found");
        return "error";
    }

//    @Override
//    public void run() {
//        while (true){
//            try {
//                if(server.checkForNewMessages().getValue()){
//                    alert.setSuccess(true);
//                    alert.setMessage("You Have New Messages");
//                    m.addAttribute("alert", alert.copy());
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
