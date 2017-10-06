/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.corba.se.spi.activation.Server;
import model.User;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hung Nguyen Manh
 */
public class Controller {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);

            while (true) {
                System.out.println("Vao controller");
                Socket socket = serverSocket.accept();

                new UserThread(socket).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Socket connectPrimaryServer(){
        Socket socket = null;
        try{
            socket = new Socket("localhost",6666);
            //System.out.println("Connected to primary server!");
        }
        catch (Exception e){
            e.getMessage();
        }

        return socket;
    }
}
