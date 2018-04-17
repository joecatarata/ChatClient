package Client.Panels;

import Client.ClientView;
import Client.GroupClientView;

import javax.swing.*;
import java.awt.*;

public class ChatCardContainer extends JPanel {

    private CardLayout cl;

    public ChatCardContainer(ClientView cv) {
        this.cl = new CardLayout();
        this.setLayout(cl);
        cv.add(this);
    }

    public ChatCardContainer(GroupClientView pcv){
        this.cl = new CardLayout();
        this.setLayout(cl);
        pcv.add(this);
    }

    public void showCard(String name) {
        cl.show(this, name);
    }


}
