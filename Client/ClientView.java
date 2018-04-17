package Client;

import Client.Panels.ChatCardContainer;
import Client.Panels.ChatPanel;
import Client.Panels.LoginPanel;

import javax.swing.*;

public class ClientView extends JFrame{

    private ClientController cc;

    private ChatCardContainer chatContainer;

    private ChatPanel chat_panel;
    private LoginPanel login_panel;
    public static final String LOG_IN_PANEL = "log-in";
    public static final String CHAT_PANEL = "chat";
    public static final String LOBBY_CHAT = "(Lobby)";
    public static final String PRIVATE_CHAT = "(Private)";


    public ClientView(ClientController cc) {

        this.cc = cc;
        this.chatContainer = new ChatCardContainer(this);
        this.login_panel = new LoginPanel(this);
        this.chat_panel = new ChatPanel(this);
        super.setTitle("Messenger");
        init();
    }

    /**
     * Initialize frame.
     */
    private void init() {
        this.setSize(1024,550);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        chatContainer.showCard(LOG_IN_PANEL);
    }

    public ChatCardContainer getChatContainer() {
        return chatContainer;
    }

    public ClientController getController() {
        return cc;
    }

    public LoginPanel getLoginPanel() {
        return login_panel;
    }

    public ChatPanel getChatpanel() {
        return chat_panel;
    }
}
