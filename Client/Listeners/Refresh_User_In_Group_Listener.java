package Client.Listeners;

import Client.GroupClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Refresh_User_In_Group_Listener implements ActionListener {
    private GroupClientView gcv;
    public Refresh_User_In_Group_Listener(GroupClientView gcv){
        this.gcv = gcv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        gcv.getGroup_chat_panel().clearUserList();
        gcv.getCc().requestUsersInGroupList(gcv.getGroupName());
    }
}
