package Client.Listeners;

import Client.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Refresh_User_Listener implements ActionListener {

    private ClientView cv;

    public Refresh_User_Listener(ClientView cv){
        this.cv = cv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        cv.getChatpanel().clearOnlineListComboBox();
        cv.getChatpanel().clearOnlineListPanel();
        cv.getController().requestUserList();
        cv.getChatpanel().activateonlinebox(true);
        cv.getChatpanel().activatePrivateMessageBtn(true);
    }
}
