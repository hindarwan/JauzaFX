/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hindarwan
 */
public class Config {
    public static String defaultWebAddress;
    public static String sesameServer;
    public static String sesameRepositoryID;
    
    public static void write(){
        Properties p = new Properties();
        p.setProperty("defaultWebAddress", "http://localhost");
        p.setProperty("sesameServer", "http://localhost:8080/openrdf-sesame");
        p.setProperty("sesameRepositoryID", "opendata");
        try {
            p.store(new FileOutputStream("config.ini"), "jauza configuration");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void read(){
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("config.ini"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        defaultWebAddress = p.getProperty("defaultWebAddress");
        sesameServer = p.getProperty("sesameServer");
        sesameRepositoryID = p.getProperty("sesameRepositoryID");
    }
}
