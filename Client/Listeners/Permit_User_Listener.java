package Client.Listeners;

import Client.GroupClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Permit_User_Listener implements ActionListener {
    private GroupClientView gcv;
    public Permit_User_Listener(GroupClientView gcv){
        this.gcv = gcv;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gcv.getCc().permitUser((String)gcv.getGroup_chat_panel().getPermitUserBox().getSelectedItem(), gcv.getGroupName());

    }
}
