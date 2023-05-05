package GUI;

import ServiceLayer.Service;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    private Service service;

    private JPanel Login;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton signin_btn;
    private JButton signup_btn;
    private JButton guest_btn;
    private JLabel txt_entername;
    private JLabel txt_enterpsw;


    public Login() {
        super("Sign in"); // Set the title of the JFrame
        setSize(300, 130); // Set the size of the JFrame
        this.setLocationRelativeTo(null); // center the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation
        setLayout(new BorderLayout());
        Color panColor = new Color(210, 211, 213);
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.setBackground(panColor);

        //add labels to the frame
        txt_entername = new JLabel("User Name: ");
        nameField = new JTextField(20);

        txt_enterpsw = new JLabel("Password: ");
        passwordField = new JPasswordField(20);

        formPanel.add(txt_entername);
        nameField.setColumns(20);
        formPanel.add(nameField);

        formPanel.add(txt_enterpsw);
        passwordField.setColumns(10);
        formPanel.add(passwordField);

        JPanel signin_btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        signin_btnPanel.setBackground(panColor);
        signin_btn = new JButton("SIGN IN");
        signin_btnPanel.add(signin_btn);

        JPanel btnsPanel = new JPanel(new FlowLayout());
        btnsPanel.setBackground(panColor);
        // Create a button and add it to the frame
        signup_btn = new JButton("SIGN UP");
        guest_btn = new JButton("GUEST");

        btnsPanel.add(signup_btn);
        btnsPanel.add(guest_btn);

        add(formPanel, BorderLayout.CENTER);
        add(signin_btnPanel, BorderLayout.EAST);
        add(btnsPanel, BorderLayout.SOUTH);

        signin_btn.addActionListener(this);
        signup_btn.addActionListener(this);
        guest_btn.addActionListener(this);

    }

    public Login(String sign_up) {}

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signin_btn) {
            // Handle login button action
            String username = nameField.getText();
            String password = new String(passwordField.getPassword());

        } else if (e.getSource() == signup_btn) {
            // Handle signup button action
            SignUp signup_frame = new SignUp();
            signup_frame.setVisible(true);
            setVisible(false); // hide login frame

        } else if (e.getSource() == guest_btn) {
            // Handle forgot password button action
            System.out.println("---GUEST---");
        }
    }

    public static void main(String[] args) {
        Login myFrame = new Login();
        myFrame.setVisible(true);
    }
}




