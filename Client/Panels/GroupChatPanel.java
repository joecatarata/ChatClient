package Client.Panels;

import Client.GroupClientView;
import Client.Listeners.*;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides the layout for the client when he
 * joins a group conversation.
 */
public class GroupChatPanel extends JPanel {
    private GroupClientView gcv;
    private JTextArea chatTextArea, onlineTextArea;
    private JTextField chatField;
    private JComboBox onlineBox;
    private JLabel currentUserLabel;
    private JComboBox permitUserBox;
    private JFileChooser fc;
    public GroupChatPanel(GroupClientView gcv){
        this.gcv = gcv;
        gcv.getChatContainer().add(this, gcv.PRIVATE_CHAT_PANEL);
        this.setBackground(Color.YELLOW);
        init();
    }

    /**
     * Calls the various init functions.
     */
    public void init(){
        this.setLayout(new BorderLayout());
        fc = new JFileChooser();
        initUserListView();
        initUserChat();
    }

    /**
     * Initializes the userListView on the left side of the group chat panel.
     */
    private void initUserListView() {
        JPanel leftContainer = new JPanel();
        JLabel onlineListLabel = new JLabel();
        JButton refresh_user_list = new JButton();
        JButton permit_user = new JButton();
        JButton refresh_permitUser_List = new JButton();
        Box verticalBox = Box.createVerticalBox();
        JScrollPane jScrollPane;
        JComboBox files = new JComboBox();

        leftContainer.setBackground(Color.gray);
        this.add(leftContainer, BorderLayout.LINE_START);

        onlineListLabel.setText("Online: ");
        verticalBox.add(onlineListLabel);
        verticalBox.add(Box.createVerticalStrut(5));


        onlineTextArea = new JTextArea();
        onlineTextArea.setEditable(false);
        jScrollPane = new JScrollPane(onlineTextArea);
        jScrollPane.getViewport().setPreferredSize(new Dimension(95,300));
        verticalBox.add(jScrollPane);

        refresh_user_list.setText("Refresh users");
        refresh_user_list.addActionListener(new Refresh_User_In_Group_Listener(gcv));
        refresh_user_list.setEnabled(true);
        verticalBox.add(refresh_user_list);

        permitUserBox = new JComboBox();
        verticalBox.add(permitUserBox);

        refresh_permitUser_List.setText("Refresh Invite User List");
        refresh_permitUser_List.addActionListener(new UpdateInviteUserList(gcv));
        verticalBox.add(refresh_permitUser_List);
        permit_user.setText("Permit User");
        permit_user.addActionListener(new Permit_User_Listener(gcv));
        verticalBox.add(permit_user);

        leftContainer.add(verticalBox);
    }

    /**
     * Initializes the user chat part of the group chat panel.
     */
    private void initUserChat() {
        JPanel rightContainer = new JPanel();
        JScrollPane jScrollPane;
        JButton submit = new JButton();
        JButton sendFileBtn = new JButton();

        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();


        this.add(rightContainer, BorderLayout.CENTER);
        currentUserLabel = new JLabel();
        verticalBox.add(currentUserLabel);
        verticalBox.add(Box.createVerticalStrut(5));

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        jScrollPane = new JScrollPane(chatTextArea);
        jScrollPane.getViewport().setPreferredSize(new Dimension(500,400));
        verticalBox.add(jScrollPane);
        verticalBox.add(Box.createVerticalStrut(1));

        horizontalBox.add(Box.createHorizontalStrut(20));
        chatField = new JTextField();
        chatField.addActionListener(new Send_Group_Message_Listener(gcv));
        chatField.setColumns(10);
        chatField.setPreferredSize(new Dimension(10, 20));
        horizontalBox.add(chatField);
        horizontalBox.add(Box.createHorizontalStrut(20));

        submit.setText("Send");
        submit.setPreferredSize(new Dimension(90,40));
        submit.addActionListener(new Send_Group_Message_Listener(gcv));
        horizontalBox.add(submit);



        sendFileBtn.setText("Send File to Chat");
        sendFileBtn.setPreferredSize(new Dimension(90,40));
        sendFileBtn.addActionListener(new Send_File_Listener(gcv));
        horizontalBox.add(sendFileBtn);




        verticalBox.add(horizontalBox);
        rightContainer.add(verticalBox);
    }

    public void clearUserList(){
        onlineTextArea.setText("");
    }

    public void addToOnlineTextArea(String name){onlineTextArea.append(name+"\n");}

    public void addToChatTextArea(String msg) {
        chatTextArea.append(msg);
    }

    public String getOnlineFieldText(){
        return chatField.getText();
    }

    public void resetOnlineFieldText(){
        chatField.setText("");
    }

    public void clearInviteUserList(){
        this.permitUserBox.removeAllItems();
    }

    public void populateInviteUserList(){
        for(int i=0; i<gcv.getCc().getClientView().getChatpanel().getOnlineBox().getItemCount(); i++){
            this.permitUserBox.addItem(gcv.getCc().getClientView().getChatpanel().getOnlineBox().getItemAt(i));
        }
    }

    public JComboBox getPermitUserBox() {
        return permitUserBox;
    }

    public void setPermitUserBox(JComboBox permitUserBox) {
        this.permitUserBox = permitUserBox;
    }

    public JFileChooser getFileChooser() {
        return fc;
    }

    public void setFileChooser(JFileChooser fc) {
        this.fc = fc;
    }
}
