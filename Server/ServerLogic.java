package Server;

import Shared.ChatRoom;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class provides the logic for the server.
 */
public class ServerLogic {
    private int port;
    private int uniqueId;
    private ServerController sc;
    private Stack unusedIDs;
    private SimpleDateFormat sdf;
    private ArrayList<ClientThread> clientThreads;
    private ArrayList<ChatRoom> chatRooms;
    private boolean isServerRunning;
    private HashMap<String, Socket> sockets;

    public ServerLogic(ServerController sc, int port){
        this.port=port;
        this.sc = sc;
        sdf = new SimpleDateFormat("HH:mm:ss");
        // ArrayList for the Client list
        clientThreads = new ArrayList<>();
        chatRooms = new ArrayList<>();
        unusedIDs = new Stack();
        uniqueId = 0;
        sockets = new HashMap<>();
    }

    /**
     * Adds a file to a group/chatRoom in the server.
     * @param groupName The group where the file is to be sent.
     * @param fileName The fileName of the file.
     * @param file The file's contents
     */
    public void addFileToGroup(String groupName, String fileName, byte[] file){
        int index = findChatRoom(groupName);
        chatRooms.get(index).addFile(fileName, file);
        System.out.println(fileName + " is sent to group " + groupName);
    }

    /**
     * Starts the server. Also starts listening for clients trying to connect.
     */
    public void start(){
        isServerRunning = true;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(isServerRunning){
                System.out.println("Waiting for clients");

                Socket socket = serverSocket.accept();
                if(!isServerRunning)
                    break;

                ClientThread t;
                if(unusedIDs.empty()){
                    t = new ClientThread(this.sc, socket, uniqueId);
                    sockets.put(t.getUsername()+": "+t.getSocket().getInetAddress().toString(),  socket);
                    showSockets();
                    uniqueId++;
                }
                else{
                    t = new ClientThread(this.sc, socket, (int) unusedIDs.pop());
                }

                clientThreads.add(t);

                t.start();
                broadCastUserList();
                broadCastGroupList();
                broadCastChatRoomList();
            }
            try {
                serverSocket.close();
                for(int i = 0; i < clientThreads.size(); ++i) {
                    ClientThread t = clientThreads.get(i);
                    try {
                        t.getsInput().close();
                        t.getsOutput().close();
                        t.getSocket().close();
                    }
                    catch(IOException ioE) {
                    }
                }
            }catch(Exception e) {
                System.out.println("Exception closing the server and clients: " + e);
            }


        }catch(IOException IOexc){
            System.out.println("Error on server socket "+IOexc);
        }


    }

    /**
     * Stops the server and closes it.
     */

    public void stop(){
        isServerRunning = false;
        try{
            new Socket("localhost", port);
        }catch(Exception e){}
    }

    /**
     * Broadcasts a message to all the clients logged in the server.
     * @param message The message to be sent.
     * @param username The username of the sender.
     */

    public synchronized void broadcast(String message, String username){
        String time = sdf.format(new Date());
        String messageLf = username + " " + time + ": " + message + "\n";

        for(int i = clientThreads.size(); --i >= 0;) {
            ClientThread ct = clientThreads.get(i);
            // try to write to the Client if it fails remove it from the list
            if(!ct.writeMsg(messageLf)) {
                unusedIDs.push(clientThreads.get(i).getClientId());
                clientThreads.remove(i);
                System.out.println("Disconnected Client " + ct.getUsername() + " removed from list.");

            }
        }
    }

    /**
     * Broadcasts the user list to all the users.
     */
    public synchronized void broadCastUserList(){
        ArrayList<String> userList = new ArrayList<>();
        for(int i = 0; i < clientThreads.size(); ++i) {
            userList.add(clientThreads.get(i).getUsername());
        }
        for(int i=0; i<clientThreads.size();i++){
            clientThreads.get(i).sendUserList(userList);
        }
    }

    /**
     * Broadcasts the group list to all the users.
     */

    public synchronized void broadCastGroupList(){
        ArrayList<String> groupList = new ArrayList<>();
        for(int i=0; i<chatRooms.size(); i++){
            groupList.add(chatRooms.get(i).getGroupChatName());
        }
        for(int i=0; i<clientThreads.size();i++){
            clientThreads.get(i).sendGroupList(groupList);
        }

    }

    public synchronized  void broadCastChatRoomList(){
        ArrayList<ChatRoom> crs = new ArrayList<>();
        for(int i=0; i<chatRooms.size(); i++){
            crs.add(chatRooms.get(i));
        }
        for(int i=0; i<clientThreads.size();i++){
            clientThreads.get(i).sendChatRooms(crs);
        }
    }

    /**
     * Adds a chatRoom to the total list of chatRooms in the server.
     * @param groupChatName The name of the group chat.
     */
    public synchronized void addChatRoom(String groupChatName){
        ChatRoom cr = new ChatRoom(groupChatName);
        chatRooms.add(cr);
    }

    /**
     * Overload of the addChatRoom function
     * Adds a chatRoom to the total list of chatRooms in the server.
     * @param groupChatName The name of the group chat.
     * @param password Password for the chat room.
     */

    public synchronized void addChatRoom(String groupChatName, String password){
        ChatRoom cr = new ChatRoom(groupChatName, password);
        chatRooms.add(cr);
    }

    /**
     * Sends message to a group.
     * @param groupName the groupName where the message is sent.
     * @param message the message to be sent.
     * @param username the sender.
     */
    public synchronized void sendMessageToGroup(String groupName, String message, String username){
        String time = sdf.format(new Date());
        String messageLf = username + " :" + time + " " + message + "\n";

        if(chatRoomExists(groupName)){
            int chatRoomIndex = findChatRoom(groupName);
            if(chatRoomIndex == -1){
                System.out.println("Chat room does not exist");
                return;
            }
            ChatRoom cr = chatRooms.get(chatRoomIndex);
            System.out.println(groupName);
            Map<String,Integer> temp = cr.getCurrentUsers();
            Iterator it = temp.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey());
                int index = findUser((String)pair.getKey());
                if(index > -1)
                    clientThreads.get(index).writeGroupMsg(groupName, messageLf);
            }

        }

    }

    /**
     * Finds a specific user in the server.
     * @param userName the user to be searched.
     * @return the index of the user's thread in the server.
     */
    public synchronized int findUser(String userName){
        for(int i=0; i<clientThreads.size(); i++){
            if(clientThreads.get(i).getUsername().equalsIgnoreCase(userName)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the index of the group.
     * @param groupName
     * @return index of the group. Returns -1 if group cannot be found.
     */

    public synchronized int findChatRoom(String groupName){
        for(int i=0; i<chatRooms.size(); i++){
            if(chatRooms.get(i).getGroupChatName().equalsIgnoreCase(groupName)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if chatRoom (group) exists.
     * @param needle
     * @return true if ChatRoom exists.
     */
    public boolean chatRoomExists(String needle){
        for(int i=0; i<chatRooms.size(); i++){
            if(chatRooms.get(i).getGroupChatName().equalsIgnoreCase(needle)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if user is permitted in a chatroom.
     */

    public synchronized boolean isUserPermitted(String groupName,String username){
        int index = findChatRoom(groupName);
        ChatRoom cr = chatRooms.get(index);
        System.out.println(username);
        for(int i=0; i<cr.getPermittedUsers().size(); i++){
            if(cr.getPermittedUsers().get(i).equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Prints all the sockets accepted by the server.
     */

    public void showSockets(){
        Iterator it = sockets.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }

    /**
     * Removes a user based on their id.
     * @param id The id of the user.
     */

    public void removeUser(int id){
        unusedIDs.push(id);
        for(int i=0; i<clientThreads.size(); i++){
            if(clientThreads.get(i).getClientId() == id){

                String key = clientThreads.get(i).getUsername() + ": " + clientThreads.get(i).getSocket().getInetAddress().toString();
                sockets.remove(key);
                clientThreads.remove(i);

            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ServerController getSc() {
        return sc;
    }

    public void setSc(ServerController sc) {
        this.sc = sc;
    }


    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(ArrayList<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public ArrayList<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public void setClientThreads(ArrayList<ClientThread> clientThreads) {
        this.clientThreads = clientThreads;
    }
}
