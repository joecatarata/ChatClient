package Client.Listeners;

import Client.ClientView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login_Listener implements ActionListener {

    private ClientView cv;

    public Login_Listener(ClientView cv) {
        this.cv = cv;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = cv.getLoginPanel().getUserFieldText();
        username = username.trim(); //remove leading and trailing spaces.
        String address = cv.getLoginPanel().getAddressFieldText().trim();
        boolean correctUsername = false, correctIP = false;

        if(address.equalsIgnoreCase("localhost")){
            address = "127.0.0.1";
        }

        if(!validIP(address)){
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid IP address.",
                    "Invalid IP Address",
                    JOptionPane.WARNING_MESSAGE);
            correctIP = false;
        }
        else{
            correctIP = true;
        }

        if(username.length() > 0) {
            correctUsername = true;
        }

        if(!correctUsername){
            JOptionPane.showMessageDialog(null,
                    "Please enter a username.",
                    "Invalid username",
                    JOptionPane.WARNING_MESSAGE);
        }

        if(correctIP && correctUsername){
            cv.getController().sendServer(address);
            cv.getController().sendUsername(username);
        }


    }

    public boolean validIP(String ip){

        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}