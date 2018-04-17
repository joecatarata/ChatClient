package Server;
import Shared.ChatRoom;
import Shared.Payload;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * This class is the server's thread for
 * a client that is connected to the
 * system.
 */
public class ClientThread extends Thread{

    private Socket socket;
    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;
    private String username;
    private String date;
    private Payload payload;
    private int id;
    private boolean isRunning;
    private ServerController sc;

    public ClientThread(ServerController sc, Socket socket, int id){
        this.sc = sc;
        this.id = id;
        this.socket = socket;

        System.out.println("Creating input/output streams");
        try{
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            sInput = new ObjectInputStream(socket.getInputStream());
            username = (String) sInput.readObject();
            for(int i=0; i<sc.getServerLogic().getClientThreads().size(); i++){
                if(sc.getServerLogic().getClientThreads().get(i).getUsername().equalsIgnoreCase(username)){
                    System.out.println("Username already taken");
                    return;
                }
            }
            System.out.println(username + " just connected.");

        }catch(IOException IOexc){
            System.out.println("Error creating input and output streams "+IOexc);
            sc.getServerLogic().removeUser(this.id);
        }catch(ClassNotFoundException cnfe){
        }

        this.date = new Date().toString() + "\n";
    }


    /**
     * This method decides what the server should do
     * in response to the data that is being given
     * to it by the clients.
     */
    public void run(){
        isRunning = true;
        while(isRunning){
            try{
                payload = (Payload) sInput.readObject();
            }catch(IOException IOexc){
                System.out.println("Error creating input and output streams "+IOexc);
                sc.getServerLogic().removeUser(this.id);
                break;
            }catch(ClassNotFoundException cnfe){break;}

            String msg, groupName;
            int index;
            switch(payload.getType()){
                case Payload.MESSAGE:
                    msg = payload.getMessage();
                    this.sc.getServerLogic().broadcast(msg, username);
                    break;
                case Payload.FILE:
                    sc.getServerLogic().addFileToGroup(payload.getGroupName(), payload.getFileName(), payload.getFile());
                    break;
                case Payload.LOGOUT:
                    System.out.println(username + " disconnected");
                    isRunning = false;
                    break;
                case Payload.WHOISIN:
                    ArrayList<String> userList = new ArrayList<>();
                    for(int i = 0; i < sc.getServerLogic().getClientThreads().size(); ++i) {
                        userList.add(sc.getServerLogic().getClientThreads().get(i).getUsername());
                    }
                    sendUserList(userList);
                    break;
                case Payload.GROUP_MESSAGE:
                    msg = payload.getMessage();
                    groupName = payload.getGroupName();
                    sc.getServerLogic().sendMessageToGroup(groupName,msg,username);
                    break;
                case Payload.GROUP_LIST:
                    ArrayList<String> groupList = new ArrayList<>();
                    for(int i=0; i<sc.getServerLogic().getChatRooms().size(); i++){
                        groupList.add(sc.getServerLogic().getChatRooms().get(i).getGroupChatName());
                    }
                    sendGroupList(groupList);
                    break;
                case Payload.ADD_GROUP:
                    if(payload.getPassword() != null){
                        sc.getServerLogic().addChatRoom(payload.getGroupName(), payload.getPassword());
                    }
                    else{
                        sc.getServerLogic().addChatRoom(payload.getGroupName());
                    }
                    index = sc.getServerLogic().findChatRoom(payload.getGroupName());
                    sc.getServerLogic().getChatRooms().get(index).addPermittedUser(payload.getUsername());
                    System.out.println(payload.getUsername());
                    sc.getServerLogic().getChatRooms().get(index).loginUser(payload.getUsername(), id);
                    sc.getServerLogic().broadCastGroupList();
                    sc.getServerLogic().broadCastChatRoomList();
                    System.out.println(payload.getGroupName());
                    break;
                case Payload.IS_USER_PERMITTED:
                    boolean isUserPermitted = sc.getServerLogic().isUserPermitted(payload.getGroupName(), payload.getUsername());
                    sendUserPermission(isUserPermitted);
                    break;
                case Payload.USERS_IN_GROUP:
                    index = sc.getServerLogic().findChatRoom(payload.getGroupName());
                    ArrayList<String> usersInGroup = new ArrayList<>();
                    Map<String,Integer> temp = sc.getServerLogic().getChatRooms().get(index).getCurrentUsers();
                    Iterator it = temp.entrySet().iterator();
                    while(it.hasNext()){
                        Map.Entry pair = (Map.Entry)it.next();
                        System.out.println(pair.getKey());
                        usersInGroup.add((String)pair.getKey());
                    }
                    sendUsersInGroup(usersInGroup, payload.getGroupName());
                    break;
                case Payload.PERMIT_USER:
                    index = sc.getServerLogic().findChatRoom(payload.getGroupName());
                    sc.getServerLogic().getChatRooms().get(index).addPermittedUser(payload.getUsername());
                    sc.getServerLogic().getChatRooms().get(index).loginUser(payload.getUsername(), id);
                    System.out.println(payload.getUsername() + " is permitted on group " + payload.getGroupName());
                    break;
                case Payload.CHAT_ROOMS:
                    sc.getServerLogic().broadCastChatRoomList();
                    break;
            }
        }
    }

    public boolean sendChatRooms(ArrayList<ChatRoom> chatRooms){

        if(!socket.isConnected()) {
            close();
            return false;
        }
        // write the message to the stream
        try {
            Payload p = new Payload(Payload.CHAT_ROOMS);
            p.setChatRooms(chatRooms);
            sOutput.writeObject(p);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending list to " + username);
            System.out.println(e.toString());
        }
        return true;
    }

    /**
     * Sends to the client the current users in a specific group
     * @param users_in_group The users in a specific group
     * @param groupName the group in which the said users are a part of.
     * @return false if failed, true if success.
     */
    private boolean sendUsersInGroup(ArrayList users_in_group, String groupName) {
        if(!socket.isConnected()) {
            close();
            return false;
        }
        // write the message to the stream
        try {
            Payload p = new Payload(Payload.USERS_IN_GROUP);
            p.setUsersInGroupList(users_in_group);
            p.setGroupName(groupName);
            sOutput.writeObject(p);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending list from" + username);
            System.out.println(e.toString());
        }
        return true;
    }

    /**
     * Closes the connection of the client to the server.
     */
    private void close() {

        // try to close the connection
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {}
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {};

        try {
            if(socket != null) socket.close();
        }
        catch(Exception e) {}

    }

    /**
     * Sends the user list to the outputStream.
     * @param user_list
     * @return
     */

    public boolean sendUserList(ArrayList user_list) {

        if(!socket.isConnected()) {
            close();
            return false;
        }

        // write the message to the stream
        try {
            Payload p = new Payload(Payload.USER_LIST);
            p.setUserList(user_list);
            sOutput.writeObject(p);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending list from" + username);
            System.out.println(e.toString());
        }
        return true;
    }

    /**
     * Sends the group list to the outputStream.
     * @param group_list
     * @return
     */

    public boolean sendGroupList(ArrayList group_list){
        if(!socket.isConnected()) {
            close();
            return false;
        }

        // write the message to the stream
        try {
            Payload p = new Payload(Payload.GROUP_LIST);
            p.setGroupList(group_list);
            sOutput.writeObject(p);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending list from" + username);
            System.out.println(e.toString());
        }
        return true;
    }

    /**
     * Sends the user permission to the output stream.
     * @param result
     * @return
     */

    public boolean sendUserPermission(boolean result){
        if(!socket.isConnected()) {
            close();
            return false;
        }

        // write the message to the stream
        try {
            Payload p = new Payload(Payload.IS_USER_PERMITTED);
            p.setUserPermitted(result);
            sOutput.writeObject(p);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending result from" + username);
            System.out.println(e.toString());
        }
        return true;
    }

    /**
     * Writes a message to this client.
     * @param msg
     * @return
     */
    public boolean writeMsg(String msg) {

        // if Client is still connected send the message to it

        if(!socket.isConnected()) {
            close();
            return false;
        }

        // write the message to the stream
        try {
            sOutput.writeObject(msg);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending message from" + username);
            System.out.println(e.toString());
        }
        return true;
    }

    /**
     * Writes a message to this client's group.
     * @param groupName The client's group.
     * @param msg The message to be sent.
     * @return
     */
    public boolean writeGroupMsg(String groupName, String msg){
        if(!socket.isConnected()) {
            close();
            return false;
        }

        // write the message to the stream
        try {
            System.out.println("Pumasok");
            Payload p = new Payload(Payload.GROUP_MESSAGE);
            p.setMessage(msg);
            p.setGroupName(groupName);
            sOutput.writeObject(p);
            System.out.println("Nawrite " + this.username);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending message from" + username);
            System.out.println(e.toString());
        }
        return true;
    }



    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getsInput() {
        return sInput;
    }

    public void setsInput(ObjectInputStream sInput) {
        this.sInput = sInput;
    }

    public ObjectOutputStream getsOutput() {
        return sOutput;
    }

    public void setsOutput(ObjectOutputStream sOutput) {
        this.sOutput = sOutput;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Payload getPayload() {
        return payload;
    }


    public int getClientId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
