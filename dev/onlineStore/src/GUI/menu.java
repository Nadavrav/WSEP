package GUI;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class menu extends JPanel{
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel menu;
    private JPanel storesPanel;
    private JTextField txt_search;
    private JTextField txt_cart;
    private JButton searchButton;
    private JFormattedTextField highlyRatedStoresFormattedTextField;
    private JPanel panel1;
    private JTextField s1name;
    private JTextField s2name;
    private JTextField s3name;
    private JTextField s4name;

    public menu() {
        frame = new JFrame("menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // menu panel
        menu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl_search = new JLabel("Search: ");
        txt_search = new JTextField(10);
        searchButton = new JButton("Search");
        txt_cart = new JTextField(5);
        txt_cart.setEditable(false);
        txt_cart.setText("0");
        JLabel lbl_cart = new JLabel("Cart: ");
        menu.add(lbl_search);
        menu.add(txt_search);
        menu.add(searchButton);
        menu.add(lbl_cart);
        menu.add(txt_cart);

        NumberFormat format = NumberFormat.getIntegerInstance();
        highlyRatedStoresFormattedTextField = new JFormattedTextField(format);
        highlyRatedStoresFormattedTextField.setColumns(10);
        highlyRatedStoresFormattedTextField.setValue(0);
        JLabel highlyRatedStoresLabel = new JLabel("Highly rated stores");
        menu.add(highlyRatedStoresLabel);
        menu.add(highlyRatedStoresFormattedTextField);

        // stores panel
        storesPanel = new JPanel(new GridLayout(4, 1));
        s1name = new JTextField("Store 1", 10);
        s2name = new JTextField("Store 2", 10);
        s3name = new JTextField("Store 3", 10);
        s4name = new JTextField("Store 4", 10);
        storesPanel.add(s1name);
        storesPanel.add(s2name);
        storesPanel.add(s3name);
        storesPanel.add(s4name);

        // add menu and stores panels to main panel
        mainPanel.add(menu, BorderLayout.NORTH);
        mainPanel.add(storesPanel, BorderLayout.CENTER);

        // add main panel to frame
        frame.getContentPane().add(mainPanel);

        // display the frame
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new menu();
    }
}