package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shop extends JFrame implements ActionListener {
    private JButton signin_btn;
    private JButton newuser_btn;
    private JLabel[] storeNamess;
    private JLabel[] productNamess;
    private JLabel s1name, s2name, s3name, s4name, s5name, s6name,
                    p1name, p2name, p3name, p4name, p5name, p6name;
    private JTextArea welcome_txt;
    private JTextArea suggestedStores_txt;
    private JTextArea suggestedProducts_txt;

    public Shop() {
        super("Shop Menu Main Page");

        // Set up the main frame
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        Color panColor = new Color(210, 211, 213);
        setLayout(new BorderLayout());

        //printing welcome message on the top
        JPanel wlcMsgPanel = new JPanel();
        wlcMsgPanel.setBackground(panColor);
        welcome_txt = new JTextArea("WELCOME TO STORE SHOP");
        wlcMsgPanel.add(welcome_txt);
        add(wlcMsgPanel, BorderLayout.NORTH);

        // buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signin_btn = new JButton("SIGN IN");
        newuser_btn = new JButton("NEW USER");
        btnPanel.setBackground(panColor);
        btnPanel.add(signin_btn);
        btnPanel.add(newuser_btn);
        Dimension buttonPanelSize = new Dimension(getWidth(), 50);
        btnPanel.setPreferredSize(buttonPanelSize);
        add(btnPanel, BorderLayout.BEFORE_LINE_BEGINS);

        // for printing "suggested stores"
        JPanel txt1Panel = new JPanel();
        txt1Panel.setBackground(panColor);
        txt1Panel.add(suggestedStores_txt);
        add(txt1Panel, BorderLayout.LINE_END);

        // store panel
        JPanel storePanel = new JPanel(new GridLayout(2, 6));
        storePanel.setBackground(panColor);
        storeNamess = new JLabel[6];
        storeNamess[0] = new JLabel("Store 1");
        storeNamess[1] = new JLabel("Store 2");
        storeNamess[2] = new JLabel("Store 3");
        storeNamess[3] = new JLabel("Store 4");
        storeNamess[4] = new JLabel("Store 5");
        storeNamess[5] = new JLabel("Store 6");

        for (int i = 0; i < 6; i++) {
            storePanel.add(storeNamess[i]);
        }

//        add(storePanel, BorderLayout.CENTER);

        //panel for printing "suggested products"
        JPanel txt2Panel = new JPanel();
        txt2Panel.setBackground(panColor);
        txt2Panel.add(suggestedProducts_txt);
//        add(txt2Panel, BorderLayout.SOUTH);

        // products panel
        JPanel productPanel = new JPanel(new GridLayout(2, 6));
        productPanel.setBackground(panColor);

        productNamess = new JLabel[6];
        productNamess[0] = new JLabel("Product 1");
        productNamess[1] = new JLabel("Product 2");
        productNamess[2] = new JLabel("Product 3");
        productNamess[3] = new JLabel("Product 4");
        productNamess[4] = new JLabel("Product 5");
        productNamess[5] = new JLabel("Product 6");

        for (int i = 0; i < 6; i++) {
            productPanel.add(productNamess[i]);
        }

//        add(productPanel, BorderLayout.SOUTH);

        signin_btn.addActionListener(this);
        newuser_btn.addActionListener(this);

        // Show the main frame
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signin_btn) {
            // Handle signup button action
            Login login_frame = new Login();
            login_frame.setVisible(true);
            setVisible(false); // hide login frame
        }
        if (e.getSource() == newuser_btn){
            SignUp signup_frame = new SignUp();
            signup_frame.setVisible(true);
            setVisible(false); // hide login frame
        }
    }

    public static void main(String[] args) {
        new Shop();
    }
}
