/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.via.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Vasek
 */
public class TimestampParser {
    
    public static void main(String[] args) {
        
        String in = "2013-11-21 19:40:48.0";
        String out = "";
        
        DateFormat dfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        DateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        
        Date dateIn;
        try {
            dateIn = dfIn.parse(in);
            out = dfOut.format(dateIn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        
        System.out.println("Out> "+out);
        
    }
    
}
