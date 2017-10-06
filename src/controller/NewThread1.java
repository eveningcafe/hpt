package controller;

import model.BankAccount;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Do Chi Thanh on 4/21/2017.
 */
public class NewThread1 {
    private Socket socket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objecOutput;
    private PrintWriter output;

    public Socket connectPrimaryServer() {
        Socket socket = null;
        try {
            socket = new Socket("192.168.137.216", 6666);
            //System.out.println("Connected to primary server!");
        } catch (Exception e) {
            e.getMessage();
        }

        return socket;
    }

    NewThread1() {
        //this.socket = connectPrimaryServer();
        //start();
    }

    NewThread1(PrintWriter output) {
        this.output = output;
    }

    NewThread1(Socket primarySocket) {
        this.socket = primarySocket;
        //start();
    }

    NewThread1(PrintWriter output, ObjectInputStream objectInput) {
        this.objectInput = objectInput;
        //this.objecOutput = objectOutput;
        this.output = output;
        //start();
    }


    public static void main(String args[]) {

        ServerSocket primarySocket = null;
        Socket priSock = null;
        ObjectOutputStream objectOutput = null;
        ObjectInputStream objectInput = null;
        try {

            primarySocket = new ServerSocket(8889);
            //priSock = primarySocket.accept();

        } catch (Exception e) {
            e.printStackTrace();
        }

        while(true){
            System.out.println("New thread 1 da duoc tao");
            try {
                priSock = primarySocket.accept();
                objectOutput = new ObjectOutputStream(priSock.getOutputStream());
                objectInput = new ObjectInputStream(priSock.getInputStream());
               // this.objectInput = objectInput;
            } catch (IOException e) {
                e.printStackTrace();
            }

            outerloop:
            while (true) {

                String command = null;
                try {

                    //Socket priSock = primarySocket.accept();

                    command = (String)  objectInput.readObject();     //Wait for command from primary server
                    System.out.println("New Thread command: " + command);


                    switch (command) {
                        case "withdraw": {
                            long value = 0;
                            long accID = 0;
                            //try {
                                value = (long) objectInput.readObject();
                                accID = (long) objectInput.readObject();
                                BankAccount acc = new BankAccount(accID);
                                String result = acc.minusMoney2(value);
                                //this.output.println("Rút tiền thành công!");
                                System.out.println(result);
                                objectOutput.writeObject(result);

                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            //}
                            break;
                        }
                        case "recharge": {
                            long value = 0;
                            long accID = 0;
                           // try {
                                value = (long) objectInput.readObject();
                                accID = (long) objectInput.readObject();

                                BankAccount acc = new BankAccount(accID);
                                String result = acc.addMoney2(value);
                                //this.output.println("Cộng tiền thành công!");
                                System.out.println(result);
                                objectOutput.writeObject(result);

                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            //}

                            break;
                        }
                        case "reset password": {
                            long userID = 0;
                            String newPassword = null;

                            //try {
                                userID = (long) objectInput.readObject();
                                newPassword = (String) objectInput.readObject();

                                User.setPassword2(userID, newPassword);
                                //output.println("Thay đổi mật khẩu thành công!");

                                objectOutput.writeObject("OK");

                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            //}

                            break;
                        }
                        case "create account": {
                            long IDAccount = 0;
                            long IDAccountFromPrimaryServer = 0;
                            long initvalue = 0;
                            User[] accHost = null;
                            //try {
                                initvalue = (long) objectInput.readObject();
                                accHost = (User[]) objectInput.readObject();
                                BankAccount.createAccount2(initvalue, accHost);      // Check when IDAccount are the same in 2 database

                                //output.println("Tạo tài khoản thành công!");

                                objectOutput.writeObject("OK");
                            //} catch (ClassNotFoundException e) {
                             //   e.printStackTrace();
                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            //}
                            break;
                        }
                        case "delete account": {
                            long IDAccount = 0;
                            //try {
                                IDAccount = (long) objectInput.readObject();
                                String result = BankAccount.deleteAccount2(IDAccount);

                                objectOutput.writeObject(result);

                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            //}
                            //output.println("Xóa tài khoản thành công!");
                            break;
                        }
                        case "exit":{
                            objectOutput.writeObject("OK");

                            //try {
                                //output.close();
                                //objectInput.close();
                                priSock.close();
                            //}
                            //catch (Exception e){
                            //    e.printStackTrace();
                            //}
                            System.out.println("Da close trong new thread");
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                } catch (Exception e) {
                    //System.out.println("New Thread error socket!");
                    //e.printStackTrace();
                    break outerloop;
                }
            }
            /*
            try{
                primarySocket.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            */
        }
    }
}
