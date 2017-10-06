/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phu_tro;

/**
 *
 * @author Hung Nguyen Manh
 */
public class Verify {
   public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    } 
}
