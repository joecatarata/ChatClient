package Client.Listeners;

import Client.GroupClientView;
import Shared.Payload;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;

public class Send_File_Listener implements ActionListener {

    public GroupClientView gcv;

    public Send_File_Listener(GroupClientView gcv){
        this.gcv = gcv;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = gcv.getGroup_chat_panel().getFileChooser();
        int return_val = fc.showOpenDialog(null);
        fc.setDialogTitle("Select the file to send");

        if(return_val == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile(); //get file path

            //reading the file

            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                Payload p = new Payload(Payload.FILE);
                p.setFile(fileContent);
                p.setGroupName(gcv.getGroupName());
                p.setFileName(file.getName());
                gcv.getCc().sendFile(p);
            } catch (IOException e1) {
                System.out.println("Error reading file " + e1);
                e1.printStackTrace();
            }
        }

    }
}
