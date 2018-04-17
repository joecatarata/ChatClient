package Client.Listeners;

import Client.GroupClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateInviteUserList implements ActionListener {

    private GroupClientView gcv;
    public UpdateInviteUserList(GroupClientView gcv){
        this.gcv = gcv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        gcv.getGroup_chat_panel().clearInviteUserList();
        gcv.getGroup_chat_panel().populateInviteUserList();
    }
}
