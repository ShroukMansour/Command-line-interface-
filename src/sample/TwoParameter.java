/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwoParameter extends Command {
   public String firstParameter; 
   public String secondParameter;
   public String realCmd;

    @Override
    public void executeCommand(String Cmd) {  
        if(verifyCommand(Cmd)){
            if("cp".equals(realCmd)){
                System.out.println("reached cp");
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                try{
                    File infile = new File(System.getProperty("user.dir") + "\\"+ firstParameter);
                    File outfile = new File(System.getProperty("user.dir") + "\\"+ secondParameter);
                    instream = new FileInputStream(infile);
                    outstream = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int length;
                   while ((length = instream.read(buffer)) > 0){
                       outstream.write(buffer, 0, length);
                   }
                   instream.close();
                   outstream.close();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            } else if("mv".equals(realCmd)) {
                File Source = new File (firstParameter);
                File Target = new File (secondParameter);
                Path source = Paths.get(System.getProperty("usefirstParameterr.dir") + firstParameter);
                
                try {
                    Files.move(source, source.resolveSibling(secondParameter));
                } catch (IOException ex) {
                    Logger.getLogger(TwoParameter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else{
            System.out.println("Please write valid command");
        }
      
     
}

    @Override
    public void splitCommand(String Cmd) {
        String[] params;
        params =   Cmd.split("\\s+");
        realCmd = params[0];
        firstParameter = params[1];
        secondParameter = params[2];
    }

    @Override
    public boolean verifyCommand(String Cmd) {
        splitCommand(Cmd);
        if("cp".equals(realCmd)  || "mv".equals(realCmd) ){
            return true;
        }
        return false;
    }
    
    
    
}
