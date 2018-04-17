package Client.Panels;

import Client.ClientView;
import Client.Listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class provides the mainChatPanel for the ClientView.
 */
public class ChatPanel extends JPanel {

    private ClientView cv;
    private JTextArea textAreaView, //TextArea that displays messages.
                      onlineTextArea,
                      groupTextArea,
                      chatTextArea;
    private JTextField chatField;
    private JComboBox onlineBox, onlineGroupBox;
    private JLabel currentUserLabel;
    private DefaultComboBoxModel dcm;
    private JButton privateSendMsg, privateGroupSendMsg;

    public ChatPanel(ClientView cv) {
        this.cv = cv;
        cv.getChatContainer().add(this, cv.CHAT_PANEL);
        this.setBackground(Color.yellow);
        init();
    }

    public void init() {
        this.setLayout(new BorderLayout());
        initUserListView();
        initUserChat();
        initGroupListView();
    }

    private void initUserListView() {
        JPanel leftContainer = new JPanel();
        JLabel privateMsg = new JLabel(); // this is the "private message" label seen on the chat view.
        JLabel onlineListLabel = new JLabel();
        JButton refreshUserList = new JButton();
        Box verticalBox = Box.createVerticalBox();
        JScrollPane jScrollPane;


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



        privateMsg.setText("Private message: ");
        verticalBox.add(Box.createVerticalStrut(25));
        //verticalBox.add(privateMsg);


        onlineBox = new JComboBox();
        verticalBox.add(Box.createVerticalStrut(10));
        //verticalBox.add(onlineBox);
        activateonlinebox(false);




        privateSendMsg = new JButton();
        privateSendMsg.addActionListener(new Send_Private_Msg_Listener(cv));
        privateSendMsg.setText("Private Message");
        privateSendMsg.setEnabled(false);
        //verticalBox.add(privateSendMsg);

        refreshUserList.setText("Refresh user list");
        refreshUserList.setPreferredSize(new Dimension(90,40));
        refreshUserList.addActionListener(new Refresh_User_Listener(cv));
        verticalBox.add(refreshUserList);


        leftContainer.add(verticalBox);
    }

    private void initUserChat() {
        JPanel centerContainer = new JPanel();
        JScrollPane jScrollPane;
        JButton submit = new JButton();

        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();


        this.add(centerContainer, BorderLayout.CENTER);
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
        chatField.setColumns(10);
        chatField.setPreferredSize(new Dimension(10, 20));
        ActionListener sml = new Send_Message_Listener(cv);
        chatField.addActionListener(sml);
        horizontalBox.add(chatField);
        horizontalBox.add(Box.createHorizontalStrut(20));

        submit.setText("Send");
        submit.setPreferredSize(new Dimension(90,40));

        submit.addActionListener(sml);
        horizontalBox.add(submit);

        verticalBox.add(horizontalBox);
        centerContainer.add(verticalBox);
    }

    private void initGroupListView() {
        JPanel leftContainer = new JPanel();
        JLabel privateGroupMsg = new JLabel(); // this is the "private message" label seen on the chat view.
        JLabel onlineListLabel = new JLabel();
        JButton refreshGroupList = new JButton();
        JButton addNewGroup = new JButton();
        Box verticalBox = Box.createVerticalBox();
        JScrollPane jScrollPane;


        leftContainer.setBackground(Color.gray);
        this.add(leftContainer, BorderLayout.LINE_END);

        onlineListLabel.setText("Online Groups: ");
        verticalBox.add(onlineListLabel);
        verticalBox.add(Box.createVerticalStrut(5));


        groupTextArea = new JTextArea();
        groupTextArea.setEditable(false);
        jScrollPane = new JScrollPane(groupTextArea);
        jScrollPane.getViewport().setPreferredSize(new Dimension(95,300));
        verticalBox.add(jScrollPane);



        privateGroupMsg.setText("Group message: ");
        verticalBox.add(Box.createVerticalStrut(25));
        verticalBox.add(privateGroupMsg);


        onlineGroupBox = new JComboBox();
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(onlineGroupBox);
        activateonlinebox(false);




        privateGroupSendMsg = new JButton();
        privateGroupSendMsg.addActionListener(new Open_Group_Chat_Listener(cv));
        privateGroupSendMsg.setText("Open Group Chat");
        privateGroupSendMsg.setEnabled(false);
        verticalBox.add(privateGroupSendMsg);

        refreshGroupList.setText("Refresh group list");
        refreshGroupList.setPreferredSize(new Dimension(90,40));
        refreshGroupList.addActionListener(new Refresh_Group_Listener(cv));
        verticalBox.add(refreshGroupList);

        addNewGroup.setText("Create new chat room");
        addNewGroup.setPreferredSize(new Dimension(90,40));
        addNewGroup.addActionListener(new Create_Group_Listener(cv));
        verticalBox.add(addNewGroup);


        leftContainer.add(verticalBox);
    }

    /**
     * Activates online combobox.
     * @param activate true if enable ; false if disabled
     */
    public void activateonlinebox(Boolean activate) {
        onlineBox.setEnabled(activate);
    }

    /**
     * Activates online group combobox.
     * @param activate true if enable ; false if disabled
     */
    public void activateonlinegroupbox(Boolean activate) {
        onlineGroupBox.setEnabled(activate);
    }


    /**
     * Adds online user on both the online list and online combobox
     * Note: Whenever there's a new user, that would be the target in the combo box.
     * @Param username: username to add.
     */
    public void addOnlineUser(String username) {
        addToOnlineBox(username);
        addToOnlineTextArea(username);
    }

    public void addToOnlineBox(String name) {
        onlineBox.addItem(name);
    }

    public void addToOnlineTextArea(String name) {
        onlineTextArea.append(name+"\n");
    }


    public void addGroup(String groupName){
        addToOnlineGroupBox(groupName);
        addGroupToGroupTextArea(groupName);
    }

    public void addToOnlineGroupBox(String groupName){
        onlineGroupBox.addItem(groupName);
    }

    public void addGroupToGroupTextArea(String groupName){
        System.out.println(groupName);
        groupTextArea.append(groupName+"\n");
    }

    public void clearGroupListPanel(){
        groupTextArea.setText("");
    }


    /**
     * This function clears the list in the online list text area.
     */
    public void clearOnlineListPanel() {
        onlineTextArea.setText("");
    }

    /**
     * Clears the contents of the drop down menu for the group in the right side of the panel.
     */

    public void clearOnlineGroupListComboBox(){onlineGroupBox.removeAllItems();}

    /**
     * Clears the contents of the drop down menu for the users in the left side of the panel.
     */

    public void clearOnlineListComboBox(){onlineBox.removeAllItems();}

    /**
     * Removes this certain user when they log out.
     * @Param index: The index of the user in the combo box
     */
    public void removeOnlineUser(int index) {
        dcm.removeElementAt(index);
    }

    /**
     * Appends the msg to the chat text area.
     * @param msg - msg to append
     */
    public void addToChatArea(String msg) {
        chatTextArea.append(msg);
    }



    /*
     * Resets the chat text field.
     */
    public void resetChatFieldText() {
        chatField.setText("");
    }

    /**
     * Retrieves the content of the text field
     * @return the msg written on the text field.
     */
    public String getOnlineFieldText() {
        return chatField.getText();
    }

    /**
     * Retreives the username that sent the msg.
     * @return the username of the sender.
     */
    public String getUserLabel() {
        return currentUserLabel.getText();
    }

    /**
     * Sets name of the l that's using the chatroom.
     * @param currentUserLabel
     */
    public void setCurrentUserLabel(String currentUserLabel) {
        this.currentUserLabel.setText(currentUserLabel);
    }

    /**
     * Activates the private message button
     * @param activate true if enabled ; false if disabled
     */
    public void activatePrivateMessageBtn(Boolean activate) {
        privateSendMsg.setEnabled(activate);
    }

    /**
     * Activates the group message button
     * @param activate true if enabled ; false if disabled
     */
    public void activateGroupMessageBtn(Boolean activate) {
        privateGroupSendMsg.setEnabled(activate);
    }

    /**
     * Gets the online box.
     * @return
     */

    public JComboBox getOnlineBox() {
        return onlineBox;
    }

    public void setOnlineBox(JComboBox onlineBox) {
        this.onlineBox = onlineBox;
    }

    public JComboBox getOnlineGroupBox() {
        return onlineGroupBox;
    }

    public void setOnlineGroupBox(JComboBox onlineGroupBox) {
        this.onlineGroupBox = onlineGroupBox;
    }

    /**
     * This retrieves the selected user in the online combobox for private messaging.
     * @return the username selected to be sent a msg privately.
     */
    public String getSelectedUser() {
        return (String) onlineBox.getSelectedItem();
    }
}
