package Client.Listeners;

import Client.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Refresh_Group_Listener implements ActionListener {

    private ClientView cv;

    public Refresh_Group_Listener(ClientView cv){
        this.cv = cv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        cv.getChatpanel().clearOnlineGroupListComboBox();
        cv.getChatpanel().clearGroupListPanel();
        cv.getController().requestGroupList();
        cv.getChatpanel().activateonlinegroupbox(true);
        cv.getChatpanel().activateGroupMessageBtn(true);


    }
}
