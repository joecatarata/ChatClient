package Server;

public class ChatServer {
    public static void main(String[] args){
        ServerController sc = new ServerController(8700);
        sc.start();
    }
}
