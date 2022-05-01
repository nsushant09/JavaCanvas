package com.assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    public static void main(String[] args)
    {
    }

    private static void displayMessage(String s) {
        System.out.println(s);
    }

    private static void save(String command){
        File file  = new File ("/Users/test/Dev/OOPAssignment/CommandHistory.txt");
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(command + "\n");
            fileWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private static void load(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("/Users/test/Dev/OOPAssignment/CommandHistory.txt"));

            String currentLine = br.readLine();
            String lastLine = "";
            while(currentLine != null){
                lastLine = currentLine;
                if(currentLine.equalsIgnoreCase("save") || currentLine.equalsIgnoreCase("load")){}
                else{
                    System.out.println(currentLine);
                }
                currentLine = br.readLine();
            }
            if(!lastLine.equals("save")){
                displayMessage("File is not saved.");
            }
            System.out.println("Last line : " + lastLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
