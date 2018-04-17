package Client.Listeners;

import Client.ClientView;
import Client.GroupClientView;
import Shared.ChatRoom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Open_Group_Chat_Listener implements ActionListener {

    private ClientView cv;

    public Open_Group_Chat_Listener(ClientView cv) {
        this.cv = cv;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String groupName = (String) cv.getChatpanel().getOnlineGroupBox().getSelectedItem();

        //boolean isUserPermitted = cv.getController().isLastCheckedUserPermission(); //Implement after
        boolean isUserPermitted = cv.getController().isUserPermitted(cv.getController().getUsername(), groupName);
        System.out.println("Umabot");
        System.out.println(isUserPermitted);
        int index = cv.getController().findChatRoom(groupName);
        ChatRoom cr = cv.getController().getChatRooms().get(index);

        if (!isUserPermitted) {
            if (cr.hasPassword()) {

//                JPasswordField pField = new JPasswordField(10);
//                JPanel pPanel = new JPanel();
//                pPanel.add(new JLabel("Please Enter The Group's Password: "));
//                pPanel.add(pField);
//
//                int result = JOptionPane.showConfirmDialog(null, pPanel);
//                if (result == JOptionPane.OK_OPTION) {
//                    String password = String.valueOf(pField.getPassword());
//                    System.out.println(String.valueOf(pField.getPassword()));
//                    if (cr.getPassword().equalsIgnoreCase(password)) {
//                        isUserPermitted = true;
//                    }
//                }
                String password = JOptionPane.showInputDialog(null, "Enter group's password");
                if (cr.getPassword().equalsIgnoreCase(password)) {
                        isUserPermitted = true;
                        cv.getController().permitUser(cv.getController().getUsername(), groupName);
                        JOptionPane.showMessageDialog(null,"Access granted to group " + groupName + "!");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Incorrect password! :(");
                    return;
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "You are not permitted in " + groupName + "! :(");
                return;
            }
        }
        if (isUserPermitted) {
            if (cv.getController().isGroupWindowActive(groupName)) {
                int index2 = cv.getController().getGroupWindow(groupName);
                cv.getController().getActiveGroupWindows().get(index2).getView().setVisible(true);
                System.out.println("Shown!");
            } else {
                GroupClientView gcv = new GroupClientView(cv.getController(), groupName);
                cv.getController().addGroupClientView(gcv);
                System.out.println("Created!");
            }
            cv.getController().requestUsersInGroupList(groupName);
        }
    }
}