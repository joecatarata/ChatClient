package Client.Listeners;

import Client.GroupClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Send_Group_Message_Listener implements ActionListener, KeyListener {
    private GroupClientView gcv;
    public Send_Group_Message_Listener(GroupClientView gcv){
        this.gcv = gcv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = gcv.getGroup_chat_panel().getOnlineFieldText();
        String user = gcv.getCc().getClientView().getChatpanel().getUserLabel();
        gcv.getGroup_chat_panel().resetOnlineFieldText();
        gcv.getCc().sendGroupMessage(user, gcv.getGroupName(), msg);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
