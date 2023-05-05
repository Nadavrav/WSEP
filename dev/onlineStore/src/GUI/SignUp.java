package GUI;

import ServiceLayer.Service;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUp extends Login{
    private JTextField input_name;
    private JTextField input_email;
    private JPasswordField input_psw;
    private JButton GETSTARTEDButton;
    private JCheckBox iAgreeToTheCheckBox;
    private JPanel txt_agreetoterms;

    public SignUp(){
        super("Sign Up");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        setLayout(new BorderLayout());
        Color panColor = new Color(210, 211, 213);

        // Create the components
        input_name = new JTextField(20);
        input_email = new JTextField(20);
        input_psw = new JPasswordField(20);
        GETSTARTEDButton = new JButton("Get Started");
        iAgreeToTheCheckBox = new JCheckBox("I agree to the terms and conditions");
        txt_agreetoterms = new JPanel();

        // Add the components to the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(panColor);

        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel("Name:");
        namePanel.add(nameLabel);
        namePanel.add(input_name);

        JPanel emailPanel = new JPanel(new GridLayout(2, 1));
        JLabel emailLabel = new JLabel("Email:");
        emailPanel.add(emailLabel);
        emailPanel.add(input_email);

        JPanel passwordPanel = new JPanel(new GridLayout(2, 1));
        JLabel passwordLabel = new JLabel("Password:");
        passwordPanel.add(passwordLabel);
        passwordPanel.add(input_psw);

        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(emailPanel, BorderLayout.CENTER);
        mainPanel.add(passwordPanel, BorderLayout.SOUTH);

        JPanel agreePanel = new JPanel();
        agreePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        agreePanel.setBackground(panColor);
        agreePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        agreePanel.add(iAgreeToTheCheckBox);

        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));
        btnPanel.setBackground(panColor);
        GETSTARTEDButton = new JButton("GET STARTED");
        btnPanel.add(GETSTARTEDButton);

        add(mainPanel, BorderLayout.NORTH);
        add(agreePanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        GETSTARTEDButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == GETSTARTEDButton) {
            Login login_frame = new Login();
            login_frame.setVisible(true);
            setVisible(false); // hide login frame
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
