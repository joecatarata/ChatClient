package Client.Listeners;

import Client.ClientView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Create_Group_Listener implements ActionListener {

    private ClientView cv;

    public Create_Group_Listener(ClientView cv){
        this.cv = cv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String groupName = JOptionPane.showInputDialog("Enter new chatroom name");
        int withPassword = JOptionPane.showConfirmDialog(null,  "Do you want to put a password?");
        if(withPassword == 0){
            String password = JOptionPane.showInputDialog("Enter password");
            cv.getController().requestToCreateGroup(groupName,password);
        }
        else {
            cv.getController().requestToCreateGroup(groupName);
        }

    }
}
