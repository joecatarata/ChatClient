package Shared;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a layout for the group conversation feature of the system.
 */

public class ChatRoom implements Serializable {
    protected static final long serialVersionUID = 8888120120L;
    private String password;
    private String groupChatName;
    private ArrayList<String> permittedUsers;
    private ArrayList<String> messages;
    private Map<String, Integer> currentUsers;
    private Map<String, byte[]> files;


    public ChatRoom(String groupChatName){
        this.groupChatName = groupChatName;
        this.password = null;
        init();
    }

    public ChatRoom(String groupChatName, String password){
        this.groupChatName = groupChatName;
        this.password = password;
        init();
    }

    public void addFile(String fileName, byte[] file){
        files.put(fileName, file);
    }

    /**
     * Adds a user in the permitted list.
     * @param username
     */

    public void addPermittedUser(String username){
        this.permittedUsers.add(username);
    }

    /**
     * Logs in a user in the group conversation.
     * @param username
     * @param id
     */

    public void loginUser(String username, int id){
        currentUsers.put(username, id);
    }

    /**
     * Logs a user out in the group conversation.
     * @param username
     */

    public void logoutUser(String username){
        currentUsers.remove(username);
    }

    /**
     * Initializes the attributes of this class.
     */

    public void init(){
        this.files = new HashMap<>();
        this.permittedUsers = new ArrayList<>();
        this.currentUsers = new HashMap<>();
        this.messages = new ArrayList<>();
    }

    public boolean hasPassword(){
        return password != null;
    }

    public void addMessages(String message){
        messages.add(message);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupChatName() {
        return groupChatName;
    }

    public void setGroupChatName(String groupChatName) {
        this.groupChatName = groupChatName;
    }

    public ArrayList<String> getPermittedUsers() {
        return permittedUsers;
    }

    public void setPermittedUsers(ArrayList<String> permittedUsers) {
        this.permittedUsers = permittedUsers;
    }

    public Map<String, Integer> getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(Map<String, Integer> currentUsers) {
        this.currentUsers = currentUsers;
    }
}
