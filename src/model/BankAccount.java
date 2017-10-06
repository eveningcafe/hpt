/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hung Nguyen Manh
 */
/*
public class BankAccount {

    private long accountID;

    public BankAccount(long acountID) {
        this.accountID = acountID;
    }
    public long getAccountID(){
        return accountID;
    }

    public void setAccountID(long accountID){
        this.accountID = accountID;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO code application logic here
//        User users[] = new User[2];
//        users[0] = new User(19249343, "Nguyễn Nhật Nam", new Date(96, 0, 1), "Hoàng Mai - Hà Nội");
//        users[1] = new User(19223200, "Nguyễn thị Thúy", new Date(96, 9, 10), "Hai Bà Trưng - Hà Nội");
//        createAccount(0, users);
//        deleteAccount(7);
//        BankAccount account = new BankAccount(21);
//        System.out.println(account.getDetail());
//        System.out.println(User.checkPassword(19249343, null));
        User.setPassword("root", "1234");
        System.out.println(User.checkPassword("root", "1234"));
//        System.out.println(account.getBalance());
//        account.addMoney(1000);
//        System.out.println(account.getBalance());
//        account.minusMoney(500);
//        System.out.println(account.getBalance());
    }

    public static boolean checkExist(long accountID) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        String sql = "select * from tai_khoan where so_TK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, accountID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static long insertAccountTable(long iniValue) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into tai_khoan values (" + iniValue + ", getdate())");
        ResultSet rs = stm.executeQuery("Select max(so_TK) from tai_khoan");
        rs.next();
        return rs.getInt(1);
    }

    private static void insertUserTalble(User users[]) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        for (User user : users) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
            stm.executeUpdate("insert into chu_TK values (" + user.id + ",N\'" + user.fullName + "\', \'" + dateFormater.format(user.birthDay) + "\', N\'" + user.address + "\', \'\')");
        }
    }

    private static void insertUser_AccountTable(long accountId, User users[]) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into chuTK_TK values (" + users[0].id + ", " + accountId + ", 1)");
        for (int i = 1; i < users.length; i++) {
            stm.executeUpdate("insert into chuTK_TK values (" + users[i].id + ", " + accountId + ", 0)");
        }
    }

    public static long createAccount(long initValue, User users[]) throws ClassNotFoundException, SQLException {
        long accountID;
        accountID = insertAccountTable(initValue);
        insertUserTalble(users);
        insertUser_AccountTable(accountID, users);
        return accountID;
    }


    public static void deleteAccount(long idAccount) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from tai_khoan where so_TK = " + idAccount);
    }


    public void addMoney(long value) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("update tai_khoan set so_du = so_du + " + value + " where so_TK = " + accountID);
    }

    public boolean minusMoney(long value) throws ClassNotFoundException, SQLException {
        if(value > getBalance()){
            return false;
        }
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("update tai_khoan set so_du = so_du - " + value + " where so_TK = " + accountID);
        return true;
    }

    public String getDetails() {
        String result = null;
        try {
            Connection conn = connection.Connector.getConnection();
            String sql = "select * from tai_khoan where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = "Số tài khoản: " + rs.getLong(1) + "; số dư: " + rs.getInt(2) + "; ngày tạo:" + rs.getTimestamp(3);
            }
            sql = "select * from chu_TK, chuTK_TK where chu_TK.so_CMTND = chuTK_TK.so_CMTND and so_TK = ? ORDER BY la_chu_chinh DESC";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("la_chu_chinh") == 1) {
                    result += "; chủ chính: ";
                } else {
                    result += "; chủ phụ: ";
                }
                result += rs.getString("ho_ten") + "; số CMTND: " + rs.getLong("so_CMTND")
                        + "; ngày sinh: " + rs.getDate("ngay_sinh") + "; địa chỉ: " + rs.getString("dia_chi_thuong_chu");
            }
            return result;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getBalance() {
        long result = -1;
        try {
            Connection conn = connection.Connector.getConnection();
            String sql = "select so_du from tai_khoan where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }






    public static boolean checkExist1(long accountID) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        String sql = "select * from tai_khoan_1 where so_TK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, accountID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static long insertAccountTable1(long iniValue) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into tai_khoan_1 values (" + iniValue + ", getdate())");
        ResultSet rs = stm.executeQuery("Select max(so_TK) from tai_khoan_1");
        rs.next();
        return rs.getInt(1);
    }

    private static void insertUserTalble1(User users[]) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        for (User user : users) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
            stm.executeUpdate("insert into chu_TK_1 values (" + user.id + ",N\'" + user.fullName + "\', \'" + dateFormater.format(user.birthDay) + "\', N\'" + user.address + "\', \'\')");
        }
    }

    private static void insertUser_AccountTable1(long accountId, User users[]) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into chuTK_TK_1 values (" + users[0].id + ", " + accountId + ", 1)");
        for (int i = 1; i < users.length; i++) {
            stm.executeUpdate("insert into chuTK_TK_1 values (" + users[i].id + ", " + accountId + ", 0)");
        }
    }

    public static long createAccount1(long initValue, User users[]) throws ClassNotFoundException, SQLException {
        long accountID;
        accountID = insertAccountTable1(initValue);
        insertUserTalble1(users);
        insertUser_AccountTable1(accountID, users);
        return accountID;
    }

    public static void deleteAccount1(long idAccount) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from tai_khoan_1 where so_TK = " + idAccount);
    }

    public void addMoney1(long value) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("update tai_khoan_1 set so_du = so_du + " + value + " where so_TK = " + accountID);
    }

    public boolean minusMoney1(long value) throws ClassNotFoundException, SQLException {
        if(value > getBalance1()){
            return false;
        }
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("update tai_khoan_1 set so_du = so_du - " + value + " where so_TK = " + accountID);
        return true;
    }


    public long getBalance1() {
        long result = -1;
        try {
            Connection conn = connection.Connector.getConnection1();
            String sql = "select so_du from tai_khoan_1 where so_TK = " + accountID + "";
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setLong(1, accountID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getStackTrace());
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }










    public static boolean checkExist2(long accountID) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        String sql = "select * from tai_khoan_2 where so_TK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, accountID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static long insertAccountTable2(long iniValue) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into tai_khoan_2 values (" + iniValue + ", getdate())");
        ResultSet rs = stm.executeQuery("Select max(so_TK) from tai_khoan_2");
        rs.next();
        return rs.getInt(1);
    }

    private static void insertUserTalble2(User users[]) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        for (User user : users) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
            stm.executeUpdate("insert into chu_TK_2 values (" + user.id + ",N\'" + user.fullName + "\', \'" + dateFormater.format(user.birthDay) + "\', N\'" + user.address + "\', \'\')");
        }
    }

    private static void insertUser_AccountTable2(long accountId, User users[]) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into chuTK_TK_2 values (" + users[0].id + ", " + accountId + ", 1)");
        for (int i = 1; i < users.length; i++) {
            stm.executeUpdate("insert into chuTK_TK_2 values (" + users[i].id + ", " + accountId + ", 0)");
        }
    }

    public static long createAccount2(long initValue, User users[]) throws ClassNotFoundException, SQLException {
        long accountID;
        accountID = insertAccountTable2(initValue);
        insertUserTalble2(users);
        insertUser_AccountTable2(accountID, users);
        return accountID;
    }

    public static void deleteAccount2(long idAccount) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from tai_khoan_2 where so_TK = " + idAccount);
    }

    public void addMoney2(long value) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("update tai_khoan_2 set so_du = so_du + " + value + " where so_TK = " + accountID);
    }

    public boolean minusMoney2(long value) throws ClassNotFoundException, SQLException {
        if(value > getBalance2()){
            return false;
        }
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("update tai_khoan_2 set so_du = so_du - " + value + " where so_TK = " + accountID);
        return true;
    }


    public long getBalance2() {
        long result = -1;
        try {
            Connection conn = connection.Connector.getConnection2();
            String sql = "select so_du from tai_khoan_2 where so_TK = " + accountID + "";
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setLong(1, accountID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getStackTrace());
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
*/

	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hung Nguyen Manh
 */
public class BankAccount {

    private long accountID;

    public BankAccount(long acountID) {
        this.accountID = acountID;
    }

    public long getAccountID() {
        return accountID;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO code application logic here
        User.setPassword("root", "1234");
        System.out.println(User.checkPassword("root", "1234"));
    }

    public static boolean checkExist(long accountID) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        String sql = "select * from tai_khoan where so_TK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, accountID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static long insertAccountTable(long iniValue) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into tai_khoan values (" + iniValue + ", getdate())");
        ResultSet rs = stm.executeQuery("Select max(so_TK) from tai_khoan");
        rs.next();
        return rs.getInt(1);
    }

    private static void insertUserTalble(User users[]) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        for (User user : users) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
            stm.executeUpdate("insert into chu_TK values (" + user.id + ",N\'" + user.fullName + "\', \'" + dateFormater.format(user.birthDay) + "\', N\'" + user.address + "\', \'\')");
        }
    }

    private static void insertUser_AccountTable(long accountId, User users[]) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into chuTK_TK values (" + users[0].id + ", " + accountId + ", 1)");
        for (int i = 1; i < users.length; i++) {
            stm.executeUpdate("insert into chuTK_TK values (" + users[i].id + ", " + accountId + ", 0)");
        }
    }

    public static long createAccount(long initValue, User users[]) throws ClassNotFoundException, SQLException {
        long accountID;
        accountID = insertAccountTable(initValue);
        insertUserTalble(users);
        insertUser_AccountTable(accountID, users);
        return accountID;
    }

    public static String deleteAccount(long idAccount) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection();
        String sql = "select so_CMTND from chuTK_TK where  so_TK = ?";
        ArrayList user_ids = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement(sql);) {
            preStmt.setLong(1, idAccount);
            try (ResultSet rs = preStmt.executeQuery()) {
                int i = 0;
                if (!rs.next()) {
                    return "tài khoản không tồn tại";
                }

                do{
                    user_ids.add(rs.getLong("so_CMTND"));
                    System.out.println(user_ids.get(i++));
                }while (rs.next());
            }
        }
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from tai_khoan where so_TK = " + idAccount);
        int i = 0;
        while (i < user_ids.size()) {
            stm.executeUpdate("delete from chu_TK where so_CMTND = " + (long) user_ids.get(i));
            i++;
        }
        return "Xóa tài khoản thành công";
    }

    public String addMoney(long value) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        int i = stm.executeUpdate("update tai_khoan set so_du = so_du + " + value + " where so_TK = " + accountID);

        if (i==0){
            return "Tài khoản không tồn tại";
        }
        else{
            return "Cộng tiền thành công";
        }
    }

    public String minusMoney(long value) throws ClassNotFoundException, SQLException {
        long balance = getBalance();
        if (balance >=0 && value > balance ) {
            return "Số tiền trong tài khoản không đủ";
        }
        Connection conn = connection.Connector.getConnection();
        Statement stm = conn.createStatement();
        int i = stm.executeUpdate("update tai_khoan set so_du = so_du - " + value + " where so_TK = " + accountID);

        if (i==0){
            return "Tài khoản không tồn tại";
        }
        else {
            return "Rút tiền thành công";
        }
    }

    public String getDetails() {
        String result = null;
        try {
            Connection conn = connection.Connector.getConnection();
            String sql = "select * from tai_khoan where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = "Số tài khoản: " + rs.getLong(1) + "; số dư: " + rs.getInt(2) + "; ngày tạo:" + rs.getTimestamp(3);
            }
            sql = "select * from chu_TK, chuTK_TK where chu_TK.so_CMTND = chuTK_TK.so_CMTND and so_TK = ? ORDER BY la_chu_chinh DESC";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("la_chu_chinh") == 1) {
                    result += "; chủ chính: ";
                } else {
                    result += "; chủ phụ: ";
                }
                result += rs.getString("ho_ten") + "; số CMTND: " + rs.getLong("so_CMTND")
                        + "; ngày sinh: " + rs.getDate("ngay_sinh") + "; địa chỉ: " + rs.getString("dia_chi_thuong_chu");
            }
            return result;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getBalance() {
        long result = -1;
        try {
            Connection conn = connection.Connector.getConnection();
            String sql = "select so_du from tai_khoan where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


















    public static boolean checkExist1(long accountID) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        String sql = "select * from tai_khoan_1 where so_TK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, accountID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static long insertAccountTable1(long iniValue) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into tai_khoan_1 values (" + iniValue + ", getdate())");
        ResultSet rs = stm.executeQuery("Select max(so_TK) from tai_khoan_1");
        rs.next();
        return rs.getInt(1);
    }

    private static void insertUserTalble1(User users[]) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        for (User user : users) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
            stm.executeUpdate("insert into chu_TK_1 values (" + user.id + ",N\'" + user.fullName + "\', \'" + dateFormater.format(user.birthDay) + "\', N\'" + user.address + "\', \'\')");
        }
    }

    private static void insertUser_AccountTable1(long accountId, User users[]) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into chuTK_TK_1 values (" + users[0].id + ", " + accountId + ", 1)");
        for (int i = 1; i < users.length; i++) {
            stm.executeUpdate("insert into chuTK_TK_1 values (" + users[i].id + ", " + accountId + ", 0)");
        }
    }

    public static long createAccount1(long initValue, User users[]) throws ClassNotFoundException, SQLException {
        long accountID;
        accountID = insertAccountTable1(initValue);
        insertUserTalble1(users);
        insertUser_AccountTable1(accountID, users);
        return accountID;
    }

    public static String deleteAccount1(long idAccount) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection1();
        String sql = "select so_CMTND from chuTK_TK_1 where  so_TK = ?";
        ArrayList user_ids = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement(sql);) {
            preStmt.setLong(1, idAccount);
            try (ResultSet rs = preStmt.executeQuery()) {
                int i = 0;
                if (!rs.next()) {
                    return "tài khoản không tồn tại";
                }

                do{
                    user_ids.add(rs.getLong("so_CMTND"));
                    System.out.println(user_ids.get(i++));
                }while (rs.next());
            }
        }
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from tai_khoan_1 where so_TK = " + idAccount);
        int i = 0;
        while (i < user_ids.size()) {
            stm.executeUpdate("delete from chu_TK_1 where so_CMTND = " + (long) user_ids.get(i));
            i++;
        }
        return "Xóa tài khoản thành công";
    }

    public String addMoney1(long value) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        int i = stm.executeUpdate("update tai_khoan_1 set so_du = so_du + " + value + " where so_TK = " + accountID);

        if (i==0){
            return "Tài khoản không tồn tại";
        }
        else{
            return "Cộng tiền thành công";
        }
    }

    public String minusMoney1(long value) throws ClassNotFoundException, SQLException {
        long balance = getBalance1();
        if (balance >=0 && value > balance ) {
            return "Số tiền trong tài khoản không đủ";
        }
        Connection conn = connection.Connector.getConnection1();
        Statement stm = conn.createStatement();
        int i = stm.executeUpdate("update tai_khoan_1 set so_du = so_du - " + value + " where so_TK = " + accountID);

        if (i==0){
            return "Tài khoản không tồn tại";
        }
        else {
            return "Rút tiền thành công";
        }
    }

    public String getDetails1() {
        String result = null;
        try {
            Connection conn = connection.Connector.getConnection1();
            String sql = "select * from tai_khoan_1 where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = "Số tài khoản: " + rs.getLong(1) + "; số dư: " + rs.getInt(2) + "; ngày tạo:" + rs.getTimestamp(3);
            }
            sql = "select * from chu_TK_1, chuTK_TK_1 where chu_TK_1.so_CMTND = chuTK_TK_1.so_CMTND and so_TK = ? ORDER BY la_chu_chinh DESC";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("la_chu_chinh") == 1) {
                    result += "; chủ chính: ";
                } else {
                    result += "; chủ phụ: ";
                }
                result += rs.getString("ho_ten") + "; số CMTND: " + rs.getLong("so_CMTND")
                        + "; ngày sinh: " + rs.getDate("ngay_sinh") + "; địa chỉ: " + rs.getString("dia_chi_thuong_chu");
            }
            return result;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getBalance1() {
        long result = -1;
        try {
            Connection conn = connection.Connector.getConnection1();
            String sql = "select so_du from tai_khoan_1 where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }













    public static boolean checkExist2(long accountID) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        String sql = "select * from tai_khoan_2 where so_TK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, accountID);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    private static long insertAccountTable2(long iniValue) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into tai_khoan_2 values (" + iniValue + ", getdate())");
        ResultSet rs = stm.executeQuery("Select max(so_TK) from tai_khoan_2");
        rs.next();
        return rs.getInt(1);
    }

    private static void insertUserTalble2(User users[]) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        for (User user : users) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
            stm.executeUpdate("insert into chu_TK_2 values (" + user.id + ",N\'" + user.fullName + "\', \'" + dateFormater.format(user.birthDay) + "\', N\'" + user.address + "\', \'\')");
        }
    }

    private static void insertUser_AccountTable2(long accountId, User users[]) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        stm.executeUpdate("insert into chuTK_TK_2 values (" + users[0].id + ", " + accountId + ", 1)");
        for (int i = 1; i < users.length; i++) {
            stm.executeUpdate("insert into chuTK_TK_2 values (" + users[i].id + ", " + accountId + ", 0)");
        }
    }

    public static long createAccount2(long initValue, User users[]) throws ClassNotFoundException, SQLException {
        long accountID;
        accountID = insertAccountTable2(initValue);
        insertUserTalble2(users);
        insertUser_AccountTable2(accountID, users);
        return accountID;
    }

    public static String deleteAccount2(long idAccount) throws SQLException, ClassNotFoundException {
        Connection conn = connection.Connector.getConnection2();
        String sql = "select so_CMTND from chuTK_TK_2 where  so_TK = ?";
        ArrayList user_ids = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement(sql);) {
            preStmt.setLong(1, idAccount);
            try (ResultSet rs = preStmt.executeQuery()) {
                int i = 0;
                if (!rs.next()) {
                    return "tài khoản không tồn tại";
                }

                do{
                    user_ids.add(rs.getLong("so_CMTND"));
                    System.out.println(user_ids.get(i++));
                }while (rs.next());
            }
        }
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from tai_khoan_2 where so_TK = " + idAccount);
        int i = 0;
        while (i < user_ids.size()) {
            stm.executeUpdate("delete from chu_TK_2 where so_CMTND = " + (long) user_ids.get(i));
            i++;
        }
        return "Xóa tài khoản thành công";
    }

    public String addMoney2(long value) throws ClassNotFoundException, SQLException {
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        int i = stm.executeUpdate("update tai_khoan_2 set so_du = so_du + " + value + " where so_TK = " + accountID);

        if (i==0){
            return "Tài khoản không tồn tại";
        }
        else{
            return "Cộng tiền thành công";
        }
    }

    public String minusMoney2(long value) throws ClassNotFoundException, SQLException {
        long balance = getBalance2();
        if (balance >=0 && value > balance ) {
            return "Số tiền trong tài khoản không đủ";
        }
        Connection conn = connection.Connector.getConnection2();
        Statement stm = conn.createStatement();
        int i = stm.executeUpdate("update tai_khoan_2 set so_du = so_du - " + value + " where so_TK = " + accountID);

        if (i==0){
            return "Tài khoản không tồn tại";
        }
        else {
            return "Rút tiền thành công";
        }
    }

    public String getDetails2() {
        String result = null;
        try {
            Connection conn = connection.Connector.getConnection2();
            String sql = "select * from tai_khoan_2 where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = "Số tài khoản: " + rs.getLong(1) + "; số dư: " + rs.getInt(2) + "; ngày tạo:" + rs.getTimestamp(3);
            }
            sql = "select * from chu_TK_2, chuTK_TK_2 where chu_TK_2.so_CMTND = chuTK_TK_2.so_CMTND and so_TK = ? ORDER BY la_chu_chinh DESC";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("la_chu_chinh") == 1) {
                    result += "; chủ chính: ";
                } else {
                    result += "; chủ phụ: ";
                }
                result += rs.getString("ho_ten") + "; số CMTND: " + rs.getLong("so_CMTND")
                        + "; ngày sinh: " + rs.getDate("ngay_sinh") + "; địa chỉ: " + rs.getString("dia_chi_thuong_chu");
            }
            return result;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getBalance2() {
        long result = -1;
        try {
            Connection conn = connection.Connector.getConnection2();
            String sql = "select so_du from tai_khoan_2 where so_TK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
