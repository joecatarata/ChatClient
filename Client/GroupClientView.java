package Client;

import Client.Panels.ChatCardContainer;
import Client.Panels.GroupChatPanel;

import javax.swing.*;

/**
 * This class provides the main JFrame for the
 * chatRoom/group conversation layout of the
 * system.
 */
public class GroupClientView extends JFrame {

    private ChatCardContainer chatContainer;
    private String groupName;
    private ClientController cc;
    public static final String PRIVATE_CHAT_PANEL = "(Private)";
    private GroupChatPanel group_chat_panel;

    public GroupClientView(ClientController cc, String groupName){
        this.cc = cc;
        this.chatContainer = new ChatCardContainer(this);
        this.group_chat_panel = new GroupChatPanel(this);
        this.groupName = groupName;
        super.setTitle(this.groupName);
        init();
    }

    /**
     * Initialize frame.
     */
    private void init() {
        this.setSize(750,500);
        this.setResizable(false);
        this.setVisible(true);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        chatContainer.showCard(PRIVATE_CHAT_PANEL);
    }

    public ClientController getCc() {
        return cc;
    }

    public void setCc(ClientController cc) {
        this.cc = cc;
    }

    public GroupChatPanel getGroup_chat_panel() {
        return group_chat_panel;
    }

    public void setGroup_chat_panel(GroupChatPanel group_chat_panel) {
        this.group_chat_panel = group_chat_panel;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    public ChatCardContainer getChatContainer() {
        return chatContainer;
    }

    public void setChatContainer(ChatCardContainer chatContainer) {
        this.chatContainer = chatContainer;
    }
}
