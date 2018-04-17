package Client;

import javax.swing.*;

/**
 * This class handles the multiple JFrames in the system
 * using multi-threading.
 * Essentially, this class enables the system to run
 * multiple windows
 * at the same time.
 */
public class MultipleFrame extends Thread{

    private GroupClientView view;
    private ClientView cv;

    public MultipleFrame(GroupClientView view){
        this.view = view;
    }

    public MultipleFrame(ClientView cv){
        this.cv = cv;
    }

    @Override
    public void run() {
        view.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public ClientView getClientView(){
        return this.cv;
    }

    public void setClientView(ClientView cv){
        this.cv = cv;
    }

    public GroupClientView getView() {
        return view;
    }

    public void setView(GroupClientView view) {
        this.view = view;
    }
}
