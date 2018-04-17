package Client.Listeners;

import Client.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Send_Message_Listener implements ActionListener, KeyListener {

    private ClientView cv;
    public Send_Message_Listener(ClientView cv) {
        this.cv = cv;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = cv.getChatpanel().getOnlineFieldText();
        String user = cv.getChatpanel().getUserLabel();
        cv.getChatpanel().resetChatFieldText();
        cv.getController().sendMessage(user, msg);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            String msg = cv.getChatpanel().getOnlineFieldText();
            String user = cv.getChatpanel().getUserLabel();
            cv.getChatpanel().resetChatFieldText();
            cv.getController().sendMessage(user, msg);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
