/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

public abstract class Command {
    public static String Current_dir = System.getProperty("user dir");
    public String discription; 
    public abstract void executeCommand(String Cmd);
    public abstract void splitCommand(String Cmd);
    public abstract boolean verifyCommand(String Cmd); 
    
}
