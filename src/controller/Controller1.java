/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.corba.se.spi.activation.Server;

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
public class Controller1 {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5556);

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter output = null;
                BufferedReader input = null;

                try {
                    output = new PrintWriter(socket.getOutputStream(), true);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (Exception ex) {
                    System.out.println("Socket error!");
                    ex.printStackTrace();
                    return;
                }

                new UserThread(input,output).start();
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
