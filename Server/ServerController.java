package Server;


public class ServerController {

    private ServerLogic sl;


    public ServerController(int port){
        sl = new ServerLogic(this, port);
    }

    public void start(){
        sl.start();
    }

    public ServerLogic getServerLogic() {
        return sl;
    }

    public void setSl(ServerLogic sl) {
        this.sl = sl;
    }
}
