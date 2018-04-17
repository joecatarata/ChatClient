package Client;

public class ChatClient {

    public static void main(String[] args){
        ClientController cc = new ClientController(8700);
        cc.start();
    }

}
