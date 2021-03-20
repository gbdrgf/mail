package com.mail.mail.UserAndTools;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

public class tempMain {

	public static void main(String[] args) {

        
//		String [] arr=new String[]{"afares@farmmail.com","2","this is the message","ftopic55","Avatars/1.png","Avatars/3.png"};
//        String [] arr2=new String[]{"hfares@farmmail.com","4","this is the message","gtopic55","Avatars/1.png","Avatars/3.png"};
//        String [] arr3=new String[]{"bmarwan@farmmail.com","7","this is the message","rtopic55","Avatars/1.png","Avatars/3.png"};
//        String [] arr4=new String[]{"kfares@farmmail.com","5","this is the message","vtopic55","Avatars/1.png","Avatars/3.png"};
//        String [] arr5=new String[]{"zmarwan@farmmail.com","1","this is the message","ftopic55","Avatars/1.png","Avatars/3.png"};
//        String [] arr6=new String[]{"marwan@farmmail.com","3","this is the message","etopic55","Avatars/1.png","Avatars/3.png"};
//        String [] arr7=new String[]{"lmarwan@farmmail.com","6","this is the message","utopic55","Avatars/1.png","Avatars/3.png"};
//        Tools tool1 = new Tools();
//        tool1.signup("fares@farmmail.com", "zFares", "password", "4");
//        tool1.signup("afares@farmmail.com", "bFares", "password", "4");
//        tool1.signup("hfares@farmmail.com", "Fares M. Fouad", "password", "4");
//        tool1.signup("bmarwan@farmmail.com", "fmarwan", "password", "4");
//        tool1.signup("kfares@farmmail.com", "Fares M. Fouad", "password", "4");
//        tool1.signup("zmarwan@farmmail.com", "Fares M. Fouad", "password", "4");
//        tool1.signup("marwan@farmmail.com", "Amarwan", "password", "4");
//        tool1.signup("lmarwan@farmmail.com", "lMarwan", "password", "4");
//        //Tools tool2 = new Tools();
//        tool1.signin("fares@farmmail.com", "password");
//        tool1.addContact("afares@farmmail.com");
//        tool1.addContact("bmarwan@farmmail.com");
//        tool1.addContact("lmarwan@farmmail.com");
//        tool1.addContact("marwan@farmmail.com");
//        //tool1.sortContacts(tool1.getContacts());
        //tool1.deleteContact("fares@farmmail.com");
        //tool1.sortContacts(tool1.getContacts());
        
//        String[] index;
//        index = tool1.getIndex("Sent");
//        System.out.println(index.length);
//        
//
//        tool1.sendEmail(arr);
//
//        tool1.sendEmail(arr2);
//
//        tool1.sendEmail(arr3);
//
//        tool1.sendEmail(arr4);
//
//        tool1.sendEmail(arr5);
//
//        tool1.sendEmail(arr6);
//
//        tool1.sendEmail(arr7);
//
//        index = tool1.getIndex("Sent");
//        System.out.println(index.length);
		Controller tool = new Controller();
		tool.signIn(new String[] {"fares@farmmail.com", "password"});
		//tool.sendEmail(new String[] {"marwan@farmmail.com", "5", "MSG", "Subject"});
		//tool.getIndex("Sent");
		tool.filter(new String[] {"Sent", "topic", "test"});
	}

}
