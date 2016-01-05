package com.playgames.remote;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by quikr on 4/1/16.
 */
public class Shell {

    public static String run (String command){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
           return e.getMessage();
        }
        BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
        StringBuilder sb = null;
        String s = null;
        try {
        	sb = new StringBuilder();
        	if(sb.length() >0){
        		sb.append(System.getProperty("line.separator"));
        	}
            while ((s = reader.readLine()) != null){
                sb.append(s);
            }
        } catch (IOException e) {
            return e.getMessage();
        }
        return sb.toString();
    }
}
