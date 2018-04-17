package Shared;
import java.io.Serializable;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Payload implements Serializable{
    protected static final long serialVersionUID = 8888120120L;
    public static final int WHOISIN = 0,
            MESSAGE = 1,
            FILE = 2,
            PRIVATE_MESSAGE = 3,
            USER_LIST = 4,
            GROUP_MESSAGE = 5,
            GROUP_LIST = 6,
            ADD_GROUP = 7,
            IS_USER_PERMITTED = 8,
            USERS_IN_GROUP = 9,
            PERMIT_USER = 10,
            CHAT_ROOMS = 11,
            LOGOUT = 50;

    private int type;
    private String username;
    private String message;
    private String groupName;
    private String password;
    private String fileName;
    private boolean isUserPermitted;
    private ArrayList<String> userList, groupList, usersInGroupList;
    private ArrayList<ChatRoom> chatRooms;
    private byte[] file;

    public Payload(int type, String message){
        this.type = type;
        this.message = message;
    }

    public Payload(int type){
        this.type = type;
        userList = new ArrayList<>();
        groupList = new ArrayList<>();
        usersInGroupList = new ArrayList<>();
        chatRooms = new ArrayList<>();
    }

    public ArrayList<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(ArrayList<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void setFile(byte[] file){
        this.file = Arrays.copyOf(file, file.length);
    }

    public int getType(){
        return this.type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage(){
        System.out.println(message);
        return this.message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGroupName(String groupName){this.groupName = groupName;}

    public void setUserList(ArrayList<String> ul){
        for(int i=0; i<ul.size(); i++){
            userList.add(ul.get(i));
        }
    }

    public byte[] getFile() {
        return file;
    }

    public static int getMESSAGE() {
        return MESSAGE;
    }

    public static int getFILE() {
        return FILE;
    }

    public static int getPrivateMessage() {
        return PRIVATE_MESSAGE;
    }

    public ArrayList<String> getUsersInGroupList() {
        return usersInGroupList;
    }

    public void setUsersInGroupList(ArrayList<String> usersInGroupList) {
        this.usersInGroupList = usersInGroupList;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setGroupList(ArrayList<String> groupList) {
        this.groupList = groupList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUserPermitted() {
        return isUserPermitted;
    }

    public void setUserPermitted(boolean userPermitted) {
        isUserPermitted = userPermitted;
    }

    public String getGroupName() {
        return groupName;
    }

    public static int getLOGOUT() {
        return LOGOUT;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static int getWHOISIN() {
        return WHOISIN;
    }

    public static int getGroupMessage() {
        return GROUP_MESSAGE;
    }

    public ArrayList<String> getGroupList() {
        return groupList;
    }
}
