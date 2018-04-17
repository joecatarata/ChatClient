package Client.Listeners;

import Client.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Switch_Card_Listener implements ActionListener {

    private ClientView cv;
    private String name;

    public Switch_Card_Listener(ClientView cv, String name) {
        this.cv = cv;
        this.name = name;
    }

    public void actionPerformed(ActionEvent e) {
        cv.getChatContainer().showCard(name);
        System.out.println(name);
    }
}
