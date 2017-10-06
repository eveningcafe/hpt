package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * Created by Do Chi Thanh on 5/4/2017.
 */
public class RecieveFromPrimaryServer{

    private String command;
    private Object object1;
    private Object object2;
    private Socket socket;
    private ObjectOutputStream objectOutput = null;
    private ObjectInputStream objectInput = null;
    private ObjectOutputStream out1 = null;
    private ObjectInputStream in1 = null;

    RecieveFromPrimaryServer(String command, Object object1, Object object2){
        this.command = command;
        this.object1 = object1;
        this.object2 = object2;
    }

    RecieveFromPrimaryServer(ObjectInputStream in1,ObjectOutputStream out1, ObjectInputStream objectInput,ObjectOutputStream objectOutput, String command, Object object1, Object object2){
        this.command = command;
        this.object1 = object1;
        this.object2 = object2;
        this.objectInput = objectInput;
        this.objectOutput = objectOutput;
        this.out1 = out1;
        this.in1 = in1;
    }

    RecieveFromPrimaryServer(ObjectInputStream objectInput,ObjectOutputStream objectOutput, String command, Object object1, Object object2){
        this.command = command;
        this.object1 = object1;
        this.object2 = object2;
        this.objectInput = objectInput;
        this.objectOutput = objectOutput;
    }

    public void run(){

        try {
            objectOutput.writeObject(command);
            if (command.compareTo("exit") != 0) {
                objectOutput.writeObject(object1);
                if (command.compareTo("delete account") != 0) {
                    objectOutput.writeObject(object2);
                }
            }

            String respond = (String) objectInput.readObject();
            //System.out.println(respond);
            out1.writeObject(respond);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
