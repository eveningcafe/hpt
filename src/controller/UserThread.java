/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BankAccount;
import model.User;

/**
 *
 * @author Hung Nguyen Manh
 */
public class UserThread extends Thread {

    private BufferedReader input;
    private PrintWriter output;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;
    private Socket socket;

    public UserThread(Socket socket){
        System.out.println("Da vao UserThread");
        this.socket = socket;
        PrintWriter output = null;
        BufferedReader input = null;

        try {
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.input = input;
            this.output = output;

        } catch (Exception ex) {
            System.out.println("Socket error!");
            ex.printStackTrace();
            return;
        }
    }


    public UserThread(BufferedReader input, PrintWriter output) {
        this.input = input;
        this.output = output;
        //this.objectInput = objectInput;
        //this.objectOutput = objectOutput;
    }

    @Override
    public void run() {
        System.out.println("RUN");
        ObjectOutputStream objectOutput = null;
        ObjectInputStream objectInput = null;

        try {

            String user = login();

            if (user.equals("root")) {
                rootThread();
            } else {
                BankAccount acc = new BankAccount(User.getAccountID(Long.parseLong(user)));
                long userID = Long.parseLong(user);
                userThread(acc, userID);
            }
        } catch (IOException ex) {
            System.out.println("Lỗi vào ra");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Lỗi cơ sở dữ liệu");
            System.out.println( ex.getMessage() );
        }


    }

    private String login() throws IOException, ClassNotFoundException, SQLException {
        String userID;
        String pass;

        while (true) {
            userID = input.readLine();
            System.out.println(userID);
            pass = input.readLine();
            System.out.println(pass);
            if (isNumeric(userID)) {
                if (User.checkPassword(Long.parseLong(userID), pass)) {
                    output.println("Đăng nhập thành công");
                    break;
                } else {
                    output.println("Người dùng hoặc mật khẩu không đúng");
                }
            } else if (User.checkPassword(userID, pass)) {
                output.println("Đăng nhập thành công");
                break;
            } else {
                output.println("Người dùng hoặc mật khẩu không đúng");
            }
        }
        return userID;
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public Socket connectPrimaryServer(){
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

    public void userThread(BankAccount acc, long userID) {

        Socket primarySocket = connectPrimaryServer();
        try {
            objectOutput = new ObjectOutputStream(primarySocket.getOutputStream());
            objectInput = new ObjectInputStream(primarySocket.getInputStream());
            this.objectOutput = objectOutput;
            this.objectInput = objectInput;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("1");
        outerloop:
        while (true) {
            // This part transfer command from user to primary server
            String instructionClient = null;
            try {
                instructionClient = input.readLine();     // Recieve instruction from client
                if (instructionClient.compareTo("recharge") == 0
                        || instructionClient.compareTo("withdraw") == 0
                        || instructionClient.compareTo("reset password") == 0
                        || instructionClient.compareTo("exit") == 0)  {

                    objectOutput.writeObject(instructionClient);      // Send instruction to primary server
                }

            } catch (Exception ex) {
                output.println(ex.getMessage());
                break;
            }

            try {
                switch (instructionClient) {
                    case "get detail information": {
                        output.println(acc.getDetails());
                        break;
                    }
                    case "recharge": {

                        String value;
                        try {
                            value = input.readLine();
                        } catch (IOException ex) {
                            output.println(ex.getMessage());
                            break;
                        }

                        if (!isNumeric(value)) {
                            output.println("Giá trị không hợp lệ");
                            break;
                        }

                        try {
                            objectOutput.writeObject(Long.parseLong(value));
                            objectOutput.writeObject(acc.getAccountID());

                            String result = (String) objectInput.readObject();
                            System.out.println(result);
                            String result1 = (String)objectInput.readObject();
                            System.out.println(result1);

                            output.println("Cộng tiền thành công!");
                            System.out.println("Da gui thong bao");

                        } catch (Exception ex) {
                            output.println(ex.getMessage());
                            break;
                        }
                        break;
                    }
                    case "withdraw": {              // Need to change
                        String value;
                        try {
                            value = input.readLine();

                        } catch (IOException ex) {
                            System.out.println("withdraw error");
                            output.println(ex.getStackTrace());
                            break;
                        }
                        try {
                            objectOutput.writeObject(Long.parseLong(value));
                            objectOutput.writeObject(acc.getAccountID());
                            String result = (String) objectInput.readObject();
                            System.out.println(result);
                            String result1 = (String)objectInput.readObject();
                            System.out.println(result1);

                            output.println("Rút tiền thành công!");
                            System.out.println("Da gui thong bao");

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            break;
                        }
                        break;
                    }
                    case "reset password": {            // Need to change
                        String newPassword;
                        try {
                            newPassword = input.readLine();
                            objectOutput.writeObject(userID);
                            objectOutput.writeObject(newPassword);


                            String result = (String) objectInput.readObject();
                            System.out.println(result);
                            String result1 = (String)objectInput.readObject();
                            System.out.println(result1);

                            output.println("Thay đổi mật khẩu thành công!");
                            System.out.println("Da gui thong bao");
                        } catch (IOException ex) {
                            output.println(ex.getMessage());
                            break;
                        }

                        break;
                    }
                    case "exit":
                        //String closeCommand = (String) objectInput.readObject();
                        //if (closeCommand.compareTo("close") == 0){
                            //output.print("close");

                            try{
                                //input.close();
                                //output.close();
                                objectOutput.close();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            System.out.println("Da close trong userthread");
                        //}
                        break outerloop;
                    default: {
                        output.printf("The command not included! ");
                        break;
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void rootThread() {

        outerloop1:
        while (true) {
            // This part process command come frome client
            String instruction;
            try {
                instruction = input.readLine();     // Recieve instruction from client

                if (instruction.compareTo("recharge") == 0
                        || instruction.compareTo("withdraw") == 0
                        || instruction.compareTo("reset password") == 0
                        || instruction.compareTo("create account") == 0
                        || instruction.compareTo("delete account") == 0) {

                    objectOutput.writeObject(instruction);      // Send instruction to primary server
                }

            } catch (IOException ex) {
                output.println(ex.getMessage());
                break;
            }
            outerloop2:
            switch (instruction) {
                case "create account": {            // Need to change
                    String initialMoney;
                    try {
                        initialMoney = input.readLine();
                    } catch (IOException ex) {
                        output.println("InitialMoney wrong value!");
                        break;
                    }
                    ArrayList<User> users = new ArrayList<>();
                    long inivalue = 0;
                    String tmp;
                    try {
                        tmp = input.readLine();
                    } catch (IOException ex) {
                        output.println("Tmp wrong value!");
                        break;
                    }

                    //
                    while (tmp != null) {
                        if (tmp.equals("out of information")) {
                            break;
                        }
                        String id = tmp;
                        String fullName;
                        try {
                            fullName = input.readLine();
                        } catch (IOException ex) {
                            output.println("Full name wrong value!");
                            break outerloop2;
                        }
                        String birthDay;
                        try {
                            birthDay = input.readLine();
                        } catch (IOException ex) {
                            output.println("Birthday wrong value!");
                            break outerloop2;
                        }
                        String address;
                        try {
                            address = input.readLine();
                        } catch (IOException ex) {
                            output.println("Address wrong value");
                            break outerloop2;
                        }
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


                        User user;
                        try {
                            user = new User(Long.parseLong(id), fullName, dateFormatter.parse(birthDay), address);
                        } catch (ParseException | NumberFormatException ex) {
                            output.println("Lỗi: số chứng minh thư nhân dân phải là dạng số, định dạng ngày phải là yyyy-MM-dd");
                            break outerloop2;
                        }
                        users.add(user);
                        try {
                            inivalue = Long.parseLong(initialMoney);
                        } catch (NumberFormatException ex) {
                            output.println("Lỗi: giá trị khởi tạo phải là số");
                            break outerloop2;
                        }
                        try {
                            tmp = input.readLine();
                        } catch (IOException ex) {
                            output.println("Temp wrong value!");
                            break outerloop2;
                        }
                    }


                    User[] accHost = users.toArray(new User[1]);
                    if (accHost.length < 1) {
                        output.println("Phải có ít nhất một chủ tài khoản");
                        break;
                    }


                    for (User user : accHost) {
                        try {
                            if (User.checkExist(user.getID())) {
                                output.println("Người dùng " + user.getName() + " (" + user.getID() + ") đã có tài khoản");
                                break outerloop2;
                            }
                        } catch (ClassNotFoundException | SQLException ex) {
                            output.println("Error! User already have an account!");
                            break;
                        }
                    }

                    long IDAccount;
                    long IDAccount2;
                    try {
                        objectOutput.writeObject(inivalue);
                        objectOutput.writeObject(accHost);

                        String result = (String) objectInput.readObject();
                        System.out.println(result);
                        String result1 = (String)objectInput.readObject();
                        System.out.println(result1);

                        output.println("Tạo tài khoản thành công!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println(ex.getStackTrace());
                        break;
                    }
                    break;
                }
                case "delete account": {        // Need to change
                    String IDAccount;

                    try {
                        IDAccount = input.readLine();
                    } catch (IOException ex) {
                        output.println(ex.getMessage());
                        break;
                    }

                    if (!isNumeric(IDAccount)) {
                        output.println("Số tài khoản không hợp lệ!");
                        break;
                    }

                    try {
                        objectOutput.writeObject( Long.parseLong(IDAccount) );

                        String result = (String) objectInput.readObject();
                        System.out.println(result);
                        String result1 = (String)objectInput.readObject();
                        System.out.println(result1);

                        output.println(result);
                    } catch (Exception ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    break;
                }
                case "get detail information": {
                    String accID;
                    try {
                        accID = input.readLine();
                    } catch (IOException ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    if (!isNumeric(accID)) {
                        output.println("Tài khoản không hợp lệ");
                        break;
                    }
                    BankAccount acc = new BankAccount(Long.parseLong(accID));
                    output.println(acc.getDetails());

                    break;
                }
                case "recharge": {              // Need to change
                    String accID;
                    String value;
                    try {
                        accID = input.readLine();
                        value = input.readLine();
                    } catch (IOException ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    if (!isNumeric(accID) || !isNumeric(value)) {
                        output.println("Số tài khoản và giá trị phải là số");
                        break;
                    }
                    BankAccount acc = new BankAccount(Long.parseLong(accID));

                    try {
                        objectOutput.writeObject(Long.parseLong(value) );
                        objectOutput.writeObject(Long.parseLong(accID) );

                        String result = (String) objectInput.readObject();
                        System.out.println(result);
                        String result1 = (String)objectInput.readObject();
                        System.out.println(result1);

                        output.println(result);
                    } catch (Exception ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    break;
                }
                case "withdraw": {              // Need to change
                    String accID;
                    String value;
                    try {
                        accID = input.readLine();
                        value = input.readLine();
                    } catch (IOException ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    if (!isNumeric(accID) || !isNumeric(value)) {
                        output.println("Số tài khoản và giá trị phải là số");
                        break;
                    }
                    BankAccount acc = new BankAccount(Long.parseLong(accID));
                    try {
                        objectOutput.writeObject(Long.parseLong(value) );
                        objectOutput.writeObject(Long.parseLong(accID) );

                        String result = (String) objectInput.readObject();
                        System.out.println(result);
                        String result1 = (String)objectInput.readObject();
                        System.out.println(result1);

                        output.println(result);
                    } catch (Exception ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    break;
                }
                case "reset password": {        // Reset password root account so don't need to change
                    String accID;
                    String newPassword;
                    try {
                        accID = input.readLine();
                        newPassword = input.readLine();
                    } catch (IOException ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    try {
                        objectOutput.writeObject(Long.parseLong(accID));
                        objectOutput.writeObject(newPassword);

                        String result = (String) objectInput.readObject();
                        System.out.println(result);
                        String result1 = (String)objectInput.readObject();
                        System.out.println(result1);

                        output.println("Thay đổi password thành công!");
                    } catch (Exception ex) {
                        output.println(ex.getMessage());
                        break;
                    }
                    break;
                }
                case "exit": {
                    try {
                        //input.close();
                        //output.close();
                        objectOutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("IO error!");
                    }
                    break outerloop1;
                }
                default:
                    output.printf("The command is not included!");
                    break;
            }
        }
    }
}
