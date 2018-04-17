package Client;

import Shared.ChatRoom;
import Shared.Payload;

import java.util.ArrayList;

/**
 * This class connects the ClientView/GroupClientViews with the ClientLogic.
 */
public class ClientController {

    private ClientView cv;
    private ClientLogic cl;
    private ArrayList<MultipleFrame> activeGroupWindows; //Group Chat and Instant Messenger boxes;
    private ArrayList<ChatRoom> chatRooms;
    private boolean lastCheckedUserPermission;
    private int port;
    private GroupClientView test;
    private String server, username;

    public ClientController(int port){
        this.port = port;
    }
    public void start() {
        init();
    }

    /**
     * Initialization of everything needed.
     */
    private void init() {
        //Instantiate View
        cv = new ClientView(this);
        MultipleFrame mainThread = new MultipleFrame(cv);
        cl = new ClientLogic(this);
//        test = new GroupClientView(this);
        cl.setPort(this.port);
        activeGroupWindows = new ArrayList<>();
        chatRooms = new ArrayList<>();
        lastCheckedUserPermission = false;

    }

    /**
     * This method sends the collected username (from the loginField)
     * @param username
     */
    public void sendUsername(String username) {
        System.out.println(username);

        //TODO server thingy
        cl.setUsername(username);
        if(!cl.connectToServer())
            return;

        this.username = username;
        cv.getChatContainer().showCard(cv.CHAT_PANEL);
        cv.getChatpanel().setCurrentUserLabel(username);

        System.out.println(cv.getChatpanel().getOnlineBox().getItemCount());
        if(cv.getChatpanel().getOnlineBox().getItemCount() != 0){
            cv.getChatpanel().activateonlinebox(true);
            cv.getChatpanel().activatePrivateMessageBtn(true);
        }

        System.out.println(cv.getChatpanel().getOnlineBox().getItemCount());
        if(cv.getChatpanel().getOnlineGroupBox().getItemCount() != 0){
            cv.getChatpanel().activateonlinegroupbox(true);
            cv.getChatpanel().activateGroupMessageBtn(true);
        }
    }

    /**
     * This method sends the collected server from the login panel.
     * @param server
     */
    public void sendServer(String server){
        System.out.println(server);
        cl.setServer(server);
    }

    /**
     * Sends the message typed by the user to everyone else in the chatroom.
     * @param user user who sent the msg
     * @param msg the msg to be sent.
     */
    public void sendMessage(String user, String msg) {
        System.out.println(msg);

        //TODO server thingy
        cl.sendMessage(new Payload(1, msg));

        //TEMPORARY output message to self
//        outPutMessage(user, msg, cv.LOBBY_CHAT);
//        Updated to outPutMessage after server has received.
    }

    /**
     * Output the msg received from the server.
     * @param user the user who sent the msg
     * @param msg the content of the msg
     * @param type the type of message (based from view's contant final strings) ; For now its: (Private) or (Lobby) types
     */
    public void outPutMessage(String user, String msg, String type) {
        String finalMsg = type + " "  + user + ": " + msg + "\n";

        //paste msg to textarea(temporary)
        cv.getChatpanel().addToChatArea(finalMsg);
    }

    /**
     * @param msg the content of the message.
     */
    public void outPutMessage(String msg){
        cv.getChatpanel().addToChatArea(msg);
    }


    /**
     * Try to connect to the server.
     */
    public void connectToServer(){
        cl.connectToServer();
    }

    /**
     * Updates the current online users.
     * @param username - the username of the newly logged-in user.
     */
    public void updateOnlineUser(String username) {
        cv.getChatpanel().addOnlineUser(username);
    }

    /**
     * Requests the user list from the server.
     */
    public void requestUserList(){
        cl.requestUserList();
    }

    /**
     * Requests the group list from the server.
     */
    public void requestGroupList(){
        cl.requestGroupList();
    }

    public void requestChatRooms() { cl.requestChatRooms(); }

    public void checkUserPermission(String groupname){
        cl.checkUserPermission(groupname);
    }


    /**
     * Sends the private message to the server.
     * @param sender - username of who sent it
     * @param receiver - username of who's receiving it.
     * @param msg - message to be delivered privately.
     */
    public void sendPrivateMessage(String sender, String receiver, String msg) {
        System.out.println("Sender: " + sender);
        System.out.println("Receiver: " + receiver);
        System.out.println("Message: " + msg);

        //TODO Do server thingy

        //Output message to own view (TEMPORARY)
        outPutMessage(sender + " to " + receiver, msg, cv.PRIVATE_CHAT);
    }

    /**
     * Sends a group message.
     * @param username the sender
     * @param groupName the group on which the message is sent.
     * @param msg the message to be sent.
     */

    public void sendGroupMessage(String username, String groupName, String msg){
        Payload p = new Payload(Payload.GROUP_MESSAGE);
        p.setGroupName(groupName);
        p.setUsername(username);
        p.setMessage(msg);
        cl.sendGroupMessage(p);
    }

    /**
     * Checks if a groupWindow is active.
     * @param groupName
     * @return
     */

    public boolean isGroupWindowActive(String groupName){
        for(int i=0; i<activeGroupWindows.size(); i++){
            if (activeGroupWindows.get(i).getView().getGroupName().equalsIgnoreCase(groupName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the index of the groupWindow.
     */

    public int getGroupWindow(String groupName){
        for(int i=0; i<activeGroupWindows.size(); i++){
            if (activeGroupWindows.get(i).getView().getGroupName().equalsIgnoreCase(groupName))
                return i;
        }
        return -1;
    }

    /**
     * Returns the activeGroupWindows for this client.
     * @return
     */

    public ArrayList<MultipleFrame> getActiveGroupWindows() {
        return activeGroupWindows;
    }

    /**
     * Sets a value for the activeGroupWindows for this client.
     * @param activeGroupWindows
     */

    public void setActiveGroupWindows(ArrayList<MultipleFrame> activeGroupWindows) {
        this.activeGroupWindows = activeGroupWindows;
    }

    public void addGroupClientView(GroupClientView gcv){
        MultipleFrame t = new MultipleFrame((gcv));
        t.start();
        activeGroupWindows.add(t);
    }

    /**
     * Requests to create a group in the server.
     * @param username the username for the new group.
     */
    public void requestToCreateGroup(String username){
        cl.requestToCreateGroup(username);
    }

    /**
     * Requests to create a group in the server.
     * @param username the username for the new group.
     * @param password the password for the new group.
     */
    public void requestToCreateGroup(String username,String password){
        cl.requestToCreateGroup(username, password);
    }

    /**
     * Gets the main ClientView for the client.
     * @return
     */

    public ClientView getClientView() {
        return cv;
    }

    /**
     * Getter for the lastCheckedUserPermission variable.
     * @return
     */

    public boolean isLastCheckedUserPermission() {
        return lastCheckedUserPermission;
    }

    /**
     * Sets the last checked permission of the user.
     * @param lastCheckedUserPermission
     */

    public void setLastCheckedUserPermission(boolean lastCheckedUserPermission) {
        this.lastCheckedUserPermission = lastCheckedUserPermission;
    }

    public boolean isUserPermitted(String userName, String groupName){
        int index = findChatRoom(groupName);
        for(String name : chatRooms.get(index).getPermittedUsers()){
            if(name.equalsIgnoreCase(userName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Calls the permit function in the ClientLogic.
     * @param userToBePermitted the user to be permitted.
     * @param groupName the group where the user is to be permitted upon.
     */

    public void permitUser(String userToBePermitted, String groupName){
        cl.permitUser(userToBePermitted, groupName);
    }

    public void requestUsersInGroupList(String groupName){
        cl.getUsersInGroup(groupName);
    }

    public int findGroup(String groupName){
        for(int i=0; i<activeGroupWindows.size(); i++){
            if(activeGroupWindows.get(i).getView().getGroupName().equalsIgnoreCase(groupName))
                return i;
        }
        return -1;
    }

    public int findChatRoom(String groupName){
        for(int i=0; i<chatRooms.size(); i++){
            if(chatRooms.get(i).getGroupChatName().equalsIgnoreCase(groupName)){
                return i;
            }
        }
        return -1;
    }

    public void sendFile(Payload p){
        cl.sendFile(p);
    }

    public void outputMsgToGroup(String groupName, String message){
        int index = findGroup(groupName);
        if(index>-1){
            System.out.println("Found group");
            activeGroupWindows.get(index).getView().getGroup_chat_panel().addToChatTextArea(message);
        }
        else {
            System.out.println("Not found group");
        }
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(ArrayList<ChatRoom> crs) {

        chatRooms.clear();
        for(int i=0; i<crs.size(); i++){
            System.out.println(i);
            chatRooms.add(crs.get(i));
        }
        System.out.println("naset chatroom");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
