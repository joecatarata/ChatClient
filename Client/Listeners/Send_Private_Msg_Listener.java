package Client.Listeners;

import Client.ClientView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Send_Private_Msg_Listener implements ActionListener {

    private ClientView cv;
    private JTextField fieldMsg;


    public Send_Private_Msg_Listener(ClientView cv) {
        this.cv = cv;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String receiver = cv.getChatpanel().getSelectedUser();
        String sender = cv.getChatpanel().getUserLabel();
        String options[] = {"Send", "Cancel"};

        int value = JOptionPane.showOptionDialog(null,
                init(receiver),
                "Private Message",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, null);

        if(value == JOptionPane.YES_OPTION) {
            cv.getController().sendPrivateMessage(sender, receiver, fieldMsg.getText());
        }
        else {
            System.out.println("Cancelled private msg");
        }


    }

    private JPanel init(String receiver) {
        JPanel container = new JPanel(new GridLayout(0, 1));
        fieldMsg = new JTextField();
        container.add(new JLabel("Send message to: " + receiver));
        container.add(fieldMsg);
        return container;
    }


}
