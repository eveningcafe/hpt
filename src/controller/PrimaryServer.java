package controller;

import model.BankAccount;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Do Chi Thanh on 4/18/2017.
 */
public class PrimaryServer {

    public static void main(String[] argv) {

        ServerSocket serverSocket = null;
        Socket sockSv1 = null;
        Socket socket = null;
        ObjectOutputStream objectOutput = null;
        ObjectInputStream objectInput = null;

        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println("loi ket noi new thread 1");
            e.printStackTrace();
            return ;
        }

        while(true) {
            System.out.println("Da vao primary server");

            try {
                socket = serverSocket.accept();
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectInput = new ObjectInputStream(socket.getInputStream());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Socket recvSocket = null;
            Socket recvSocket1 = null;
            ObjectOutputStream recvObOut = null;
            ObjectInputStream recvObIn = null;
            ObjectOutputStream recvObOut1 = null;
            ObjectInputStream recvObIn1 = null;

            try {
                recvSocket = new Socket("localhost", 8888);
                recvSocket1 = new Socket("localhost", 8889);

                recvObOut = new ObjectOutputStream(recvSocket.getOutputStream());
                recvObIn = new ObjectInputStream(recvSocket.getInputStream());

                recvObOut1 = new ObjectOutputStream(recvSocket1.getOutputStream());
                recvObIn1 = new ObjectInputStream(recvSocket1.getInputStream());

                outerloop:
                while (true) {
                    try {
                        String command = null;
                        //try {
                            command = (String) (objectInput.readObject());      // Recieve command from client->server->primary server
                        //} catch (Exception e) {
                        //    e.printStackTrace();
                        //    break;
                        //}

                        switch (command) {
                            case "create account": {
                                long IDAccount = 0;
                                long initvalue = 0;
                                User[] accHost = null;
                                //try {
                                    initvalue = (long) objectInput.readObject();
                                    accHost = (User[]) objectInput.readObject();
                                    IDAccount = BankAccount.createAccount1(initvalue, accHost);

                                    //objectOutput.writeObject(initvalue);
                                    //objectOutput.writeObject(accHost);

                                    new RecieveFromPrimaryServer(objectInput, objectOutput, recvObIn, recvObOut,command, initvalue, accHost).run();
                                    new RecieveFromPrimaryServer1(objectInput, objectOutput, recvObIn1, recvObOut1,command, initvalue, accHost).run();


                                //} catch (ClassNotFoundException e) {
                                 //   e.printStackTrace();
                                //} catch (SQLException e) {
                                 //   e.printStackTrace();
                                //}
                                break;
                            }
                            case "delete account": {            // Phai sua so luong tham so
                                long IDAccount = 0;
                                //try {
                                    IDAccount = (long) objectInput.readObject();
                                    BankAccount.deleteAccount1(IDAccount);

                                    //objectOutput.writeObject(IDAccount);

                                    new RecieveFromPrimaryServer(objectInput, objectOutput, recvObIn, recvObOut,command, IDAccount, null).run();
                                    new RecieveFromPrimaryServer1(objectInput, objectOutput, recvObIn1, recvObOut1,command, IDAccount, null).run();


                                //} catch (Exception e) {
                                //    e.printStackTrace();
                                //}
                                break;
                            }
                            case "recharge": {
                                long value = 0;
                                long accID = 0;
                                //try {
                                    value = (long) objectInput.readObject();
                                    accID = (long) objectInput.readObject();

                                    BankAccount acc = new BankAccount(accID);
                                    acc.addMoney1(value);

                                    //objectOutput.writeObject(value);
                                    //objectOutput.writeObject(accID);

                                    new RecieveFromPrimaryServer(objectInput, objectOutput, recvObIn, recvObOut, command, value, accID).run();
                                    new RecieveFromPrimaryServer1(objectInput, objectOutput, recvObIn1, recvObOut1, command, value, accID).run();

                                //} catch (Exception e) {
                                //    e.printStackTrace();
                                //}

                                break;
                            }
                            case "withdraw": {
                                System.out.println("Da nhan duoc withdraw!");
                                long value = 0;
                                long accID = 0;
                                //try {
                                    value = (long) objectInput.readObject();
                                    accID = (long) objectInput.readObject();
                                    BankAccount acc = new BankAccount(accID);
                                    acc.minusMoney1(value);


                                        //objectOutput.writeObject(value);
                                        //objectOutput.writeObject(accID);
                                        new RecieveFromPrimaryServer(objectInput, objectOutput, recvObIn, recvObOut, command, value, accID).run();
                                        new RecieveFromPrimaryServer1(objectInput, objectOutput, recvObIn1, recvObOut1, command, value, accID).run();


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

                                    User.setPassword1(userID, newPassword);

                                    //objectOutput.writeObject(userID);
                                    //objectOutput.writeObject(newPassword);

                                    new RecieveFromPrimaryServer(objectInput, objectOutput, recvObIn, recvObOut,command, userID, newPassword).run();
                                    new RecieveFromPrimaryServer1(objectInput, objectOutput, recvObIn1, recvObOut1,command, userID, newPassword).run();

                                //} catch (Exception e) {
                                //    e.printStackTrace();
                                //}

                                break;
                            }
                            case "exit":{

                                new RecieveFromPrimaryServer(objectInput, objectOutput, recvObIn, recvObOut, command, null, null).run();
                                new RecieveFromPrimaryServer1(objectInput, objectOutput, recvObIn1, recvObOut1, command, null, null).run();

                                //try {
                                    objectInput.close();
                                    objectOutput.close();
                                    System.out.println("Da close trong primary server");
                                //}
                               // catch (Exception e){
                                //    e.printStackTrace();
                                //}
                                break outerloop;
                            }
                            default: {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        //System.out.println(e.getMessage());
                        //e.printStackTrace();
                        break;
                    }

                }
            } catch (IOException e) {
                //e.printStackTrace();
                //break;
            }
        }
    }
}
