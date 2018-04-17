package Client;

import Shared.Payload;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class provides the logic for a client in the system.
 * @author Cedric Lance Viaje
 */

public class ClientLogic {

    private ClientController cc;
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private String server, username;
    private Socket socket;
    private ArrayList<String> userList;
    private int port;


    public ClientLogic(ClientController cc){
        this.cc = cc;
    }

    public ObjectInputStream getsInput() {
        return sInput;
    }

    /**
     * Sends a message to everyone in the server.
     * @param cp The payload to be sent.
     */

    public void sendMessage(Payload cp){
        try{
            sOutput.writeObject(cp);
            System.out.println("Message sent to server.");
        }catch(IOException IOexc){
            System.out.println("Error writing to server " + IOexc);
        }
    }

    /**
     * Sends a message to a specific group in the server.
     * @param p The payload to be sent.
     */

    public void sendGroupMessage(Payload p){
        try{
            sOutput.writeObject(p);
            System.out.println("Message sent to server.");
        }catch(IOException IOexc){
            System.out.println("Error writing to server " + IOexc);
        }
    }

    /**
     * Sends a file to a group in the server.
     */

    public void sendFile(Payload p){
        try{
            sOutput.writeObject(p);
            System.out.println("File sent to server.");
        }catch(IOException IOexc){
            System.out.println("Error writing to server " + IOexc);
        }
    }

    /**
     * Connects this client to the server.
     * @return
     */

    public boolean connectToServer(){
        try{
            socket = new Socket(server, port);
        }catch(Exception exc){
            System.out.println("Failed to connect to server " + exc);
            return false;
        }

        System.out.println("Connection accepted");

        try{
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(IOException IOexc){
            System.out.println("Error creating new Input/Output streams " + IOexc);
            return false;
        }
        new ServerListener().start();
        System.out.println("IO stream created");

        try{
            sOutput.writeObject(this.username);
        }catch(IOException IOexc){
            System.out.println("Error logging in " + IOexc);
            disconnect();
        }
        System.out.println("Logged in");
        return true;

    }

    /**
     * Disconnects this client from the server.
     */

    private void disconnect(){
        try{
            if(sInput!=null){
                sInput.close();
            }
        }catch(Exception e){ }
        try{
            if(sOutput!=null){
                sOutput.close();
            }
        }catch(Exception e){ }
        try{
            if(socket!=null){
                socket.close();
            }
        }catch(Exception e){ }
    }

    public void requestChatRooms(){
        try{
            Payload p = new Payload(Payload.CHAT_ROOMS);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc);
        }
    }

    /**
     * Request for the total list of users in the server.
     */

    public void requestUserList(){
        try{
            Payload p = new Payload(Payload.WHOISIN);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc);
        }
    }

    /**
     * Request for the total list of groups in the server.
     */

    public void requestGroupList(){
        try{
            Payload p = new Payload(Payload.GROUP_LIST);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc );
        }
    }

    /**
     * Sends a request to the server to create a group.
     * @param groupName
     */

    public void requestToCreateGroup(String groupName){
        try{
            Payload p = new Payload(Payload.ADD_GROUP);
            p.setGroupName(groupName);
            p.setUsername(this.username);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc );
        }
    }

    /**
     * Sends a request to the server to create a group
     * @param groupName The name of the group to be created.
     * @param password The password of the group to be created.
     */

    public void requestToCreateGroup(String groupName, String password){
        try{
            Payload p = new Payload(Payload.ADD_GROUP);
            p.setGroupName(groupName);
            p.setUsername(this.username);
            p.setPassword(password);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc );
        }
    }

    /**
     * Checks the permission of a user.
     * @param groupName The group on which the user's permission should be checked.
     */

    public void checkUserPermission(String groupName){
        try{
            Payload p = new Payload(Payload.IS_USER_PERMITTED);
            p.setGroupName(groupName);
            p.setUsername(this.username);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc );
        }
    }

    /**
     * Request a list of users in a group from the server..
     * @param groupName
     */

    public void getUsersInGroup(String groupName){
        try{
            Payload p = new Payload(Payload.USERS_IN_GROUP);
            p.setGroupName(groupName);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc );
        }
    }

    /**
     * Requests the server to permit the username
     * @param userToBePermitted self-explanatory
     * @param groupName the group on which the user is to be permitted upon.
     */

    public void permitUser(String userToBePermitted, String groupName){
        try{
            Payload p = new Payload(Payload.PERMIT_USER);
            p.setUsername(userToBePermitted);
            p.setGroupName(groupName);
            sOutput.writeObject(p);
        }catch(IOException IOexc){
            System.out.println("Error sending request " + IOexc );
        }
    }

    /**
     * Sets the input stream for the client.
     * @param sInput
     */

    public void setsInput(ObjectInputStream sInput) {
        this.sInput = sInput;
    }

    /**
     * Gets the output stream for the client.
     * @return
     */

    public ObjectOutputStream getsOutput() {
        return sOutput;
    }

    /**
     * Sets the output stream for the client.
     * @param sOutput
     */

    public void setsOutput(ObjectOutputStream sOutput) {
        this.sOutput = sOutput;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * The thread of the client that listens to the server
     * for objects that are being sent.
     */

    public class ServerListener extends Thread {

        /**
         * This method decides what to do with data
         * received from the server.
         */
        public void run() {

            while(true) {

                try {
                    Object o = sInput.readObject();
                    if(o instanceof String){
                        String msg = (String) o;
                        cc.outPutMessage(msg);
                        System.out.println(msg);
                        System.out.print("> ");
                    }
                    else if(o instanceof Payload){
                        Payload p = (Payload) o;
                        int index;
                        switch(p.getType()){
                            case Payload.USER_LIST:
                                cc.getClientView().getChatpanel().clearOnlineListPanel();
                                cc.getClientView().getChatpanel().clearOnlineListComboBox();
                                for(int i=0; i<p.getUserList().size(); i++){
                                    if(!(p.getUserList().get(i).equalsIgnoreCase(getUsername()))) {
                                        cc.getClientView().getChatpanel().addOnlineUser(p.getUserList().get(i));
                                    }
                                }
                                break;
                            case Payload.GROUP_LIST:
                                cc.getClientView().getChatpanel().clearGroupListPanel();
                                cc.getClientView().getChatpanel().clearOnlineGroupListComboBox();
                                for(int i=0; i<p.getGroupList().size(); i++){
                                    cc.getClientView().getChatpanel().addGroup(p.getGroupList().get(i));
                                }
                                break;
                            case Payload.GROUP_MESSAGE:
                                System.out.println(p.getMessage());
                                System.out.println("Received group msg");
                                cc.outputMsgToGroup(p.getGroupName(), p.getMessage());
                                break;
                            case Payload.IS_USER_PERMITTED:
                                cc.setLastCheckedUserPermission(p.isUserPermitted());
                                break;
                            case Payload.USERS_IN_GROUP:
                                index = cc.findGroup(p.getGroupName());
                                for(int i=0; i<p.getUsersInGroupList().size(); i++) {
                                    cc.getActiveGroupWindows().get(index).getView().getGroup_chat_panel().addToOnlineTextArea(p.getUsersInGroupList().get(i));
                                }
                                break;
                            case Payload.CHAT_ROOMS:
                                System.out.println("Received a chatroom! Length: " + p.getChatRooms().size());
                                cc.setChatRooms(p.getChatRooms());
                                break;

                        }
                    }
                }
                catch(IOException e) {
                    System.out.println("Server has closed the connection: " + e);
                    break;
                }
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }

}
