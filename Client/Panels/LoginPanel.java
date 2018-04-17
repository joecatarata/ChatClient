package Client.Panels;

import Client.ClientView;
import Client.Listeners.Login_Listener;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {



    private ClientView cv;
    private JTextField user_field, addressField;
    private JLabel addressLabel, usernameLabel;
    public LoginPanel(ClientView cv) {
        this.cv = cv;
        this.setBackground(Color.white);

        cv.getChatContainer().add(this, cv.LOG_IN_PANEL);

        init();
    }

    private void init() {
        this.setLayout(null);

        //button
        JButton loginBtn = new JButton("login");
        Font fBtn = new Font("serif", Font.PLAIN, 28);
        loginBtn.setFont(fBtn);
        loginBtn.setBounds(250, 250, 200, 50);

        loginBtn.addActionListener(new Login_Listener(cv));

        usernameLabel = new JLabel("Enter your username: ");
        usernameLabel.setBounds(100, 150, 500,40);

        user_field = new JTextField();
        Font fField = new Font("serif", Font.PLAIN, 28);
        user_field.setFont(fField);
        //user_field.setHorizontalAlignment(JTextField.CENTER);
        user_field.setColumns(5);
        user_field.setBounds(100, 200, 500, 40);

        addressField = new JTextField();
        Font fField2 = new Font("serif", Font.PLAIN, 28);
        addressField.setFont(fField2);
        //addressField.setHorizontalAlignment(JTextField.CENTER);
        addressField.setColumns(5);
        addressField.setBounds(100, 100, 500, 40);

        addressLabel = new JLabel("IP Address of Server: ");
        addressLabel.setBounds(100,50,500,40);

        this.add(user_field);
        this.add(addressField);
        this.add(loginBtn);
        this.add(addressLabel);
        this.add(usernameLabel);
    }

    /**
     * Retrieves the input of the user (username)
     * @return the username entered.
     */
    public String getUserFieldText() {
        return user_field.getText();
    }

    /**
     * Retrieves the input of the user (address)
     * @return the ip address entered.
     */
    public String getAddressFieldText() {
        return addressField.getText();
    }
}
