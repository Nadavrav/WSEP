package GUI;

import ServiceLayer.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GUI.menu;

public class Login extends JFrame{
    private Service service;

    private JTextField nameField;
    private JPasswordField passwordField;
    private JLabel txt_name;
    private JLabel txt_psw;
    private JButton btn_signin;
    private JButton btn_signup;
    private JButton btn_guest;
    private JPanel Login;
    private JPanel panelMain;

    public void Login(){
        btn_signin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.login(nameField.getName(), new String (passwordField.getPassword()));
                //creating new instance of menu
                menu m = new menu();
                m.setVisible(true); //opens m
            }
        });

        btn_signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.Register(nameField.getName(), new String (passwordField.getPassword()));
            }
        });

        btn_guest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
