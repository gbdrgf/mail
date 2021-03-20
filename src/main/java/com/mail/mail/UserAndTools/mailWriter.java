package com.mail.mail.UserAndTools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class mailWriter {
    private mailFactory x=new mailFactory();
    public void makeMail(String arr[]){  //success
        IEmail email= x.makeEmail(arr);
        String name= Long.toString(email.getTime());
        File file = new File("Users/"+email.getSender()+"/Sent/"+name);
        if(!file.exists()){
            file.mkdir();
        }
        file =new File("Users/"+email.getReceiver()+"/Inbox/"+name);
        if(!file.exists()){
            file.mkdir();
        }
        JSONObject object=new JSONObject();
        object.put("sender",email.getSender());
        object.put("receiver",email.getReceiver());
        object.put("priority",Integer.toString(email.getPriority()));
        object.put("message",email.getMessage());
        object.put("topic",email.getTopic());
        try(FileWriter fileWriter= new FileWriter("Users/"+email.getSender()+"/Sent/"+name+"/message.json")) {
            fileWriter.write(object.toString());
            fileWriter.flush();
        }catch (IOException e){
        	e.printStackTrace();
        }
        try(FileWriter fileWriter= new FileWriter("Users/"+email.getReceiver()+"/Inbox/"+name+"/message.json")) {
            fileWriter.write(object.toString());
            fileWriter.flush();
        }catch (IOException e){
        	e.printStackTrace();
        }


        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String date=formatter.format(new Date(email.getTime()));
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(date);
        jsonArray.add(Integer.toString(email.getPriority()));
        jsonArray.add(email.getSender());
        jsonArray.add(email.getReceiver());
        jsonArray.add(email.getTopic());
        jsonArray.add(Long.toString(email.getTime()));
        JSONParser parser=new JSONParser();

        try{
        	FileReader r1 = new FileReader("Users/"+email.getSender()+"/Sent/index.json");
            Object obj=parser.parse(r1);
            r1.close();
            JSONObject jsonObject=(JSONObject)obj;
            jsonObject.put(date,jsonArray);
            FileWriter fileWriter= new FileWriter("Users/"+email.getSender()+"/Sent/index.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
        	FileReader r2 = new FileReader("Users/"+email.getReceiver()+"/Inbox/index.json");
            Object obj=parser.parse(r2);
            r2.close();
            JSONObject jsonObject=(JSONObject)obj;

            jsonObject.put(date,jsonArray);
            FileWriter fileWriter= new FileWriter("Users/"+email.getReceiver()+"/Inbox/index.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        attachmentSaver(arr,name);
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    public IEmail getMail(String user,String folder,String name){ //success
        JSONParser parser=new JSONParser();
        String[] arr=new String[5];
        try{
        	FileReader r3 = new FileReader("Users/"+user+"/"+folder+"/"+name+"/message.json");
            Object obj=parser.parse(r3);
            r3.close();
            JSONObject jsonObject=(JSONObject)obj;
            arr[0]=(String) jsonObject.get("sender");
            arr[1]=(String) jsonObject.get("receiver");
            arr[2]=(String) jsonObject.get("priority");
            arr[3]=(String) jsonObject.get("message");
            arr[4]=(String) jsonObject.get("topic");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return x.makeEmail(arr);
    }
    public String[] getIndex(String user,String folder) { //success
        int count=0;
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
        	FileReader r4 = new FileReader("Users/" + user + "/" + folder + "/index.json");
            Object obj = parser.parse(r4);
            r4.close();
            jsonObject = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator it = jsonObject.keySet().iterator();
        it.next();
        while (it.hasNext()) {
            JSONArray arrayJson = (JSONArray) jsonObject.get(it.next());
            Iterator<String> iterator = arrayJson.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
        }
        String[] ans=new String[count];
        int i=0;
        it = jsonObject.keySet().iterator();
        it.next();
        while (it.hasNext()) {
            JSONArray arrayJson = (JSONArray) jsonObject.get(it.next());
            Iterator<String> iterator = arrayJson.iterator();
            while (iterator.hasNext()) {
                ans[i]=iterator.next();
                i++;
            }
        }
        return ans;
    }
    private void attachmentSaver(String[] arr,String name){
    	File attIndexS = new File("Users/"+arr[0]+"/Sent/"+name+"/attIndex.json");
    	File attIndexR = new File("Users/"+arr[1]+"/Inbox/"+name+"/attIndex.json");
    	try {
			attIndexS.createNewFile();
			attIndexR.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}    	
    	JSONArray jarrS = new JSONArray();
    	JSONObject jobjS = new JSONObject();
    	JSONArray jarrR = new JSONArray();
    	JSONObject jobjR = new JSONObject();
    	Tools tool3 = new Tools();
    	String mailPath = tool3.mailPath();
        if(arr.length>5){
            for(int i=5;i< arr.length;i++){
                File x=new File(arr[i]);
                String[]temp=arr[i].split("/");
                String temp1 = "Users\\"+arr[0]+"\\Sent\\"+name+"\\"+temp[temp.length-1];
                File sender=new File(temp1);
                jarrS.add(mailPath+"\\"+temp1);
                String temp2 = "Users\\"+arr[1]+"\\Inbox\\"+name+"\\"+temp[temp.length-1];
                File receiver=new File(temp2);
                jarrR.add(mailPath+"\\"+temp2);
                try {
                    Files.copy(x.toPath(),sender.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(x.toPath(),receiver.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            jobjS.put("index", jarrS);
            jobjR.put("index", jarrR);
            try {
            	FileWriter fileWriter1= new FileWriter("Users/"+arr[0]+"/Sent/"+name+"/attIndex.json");
            	FileWriter fileWriter2= new FileWriter("Users/"+arr[1]+"/Inbox/"+name+"/attIndex.json");
            	fileWriter1.write(jobjS.toString());
                fileWriter2.write(jobjR.toString());
                fileWriter1.flush();
                fileWriter2.flush();	
            }catch (IOException e){
            	e.printStackTrace();
            }
        }
    }
    public void mail_inFile(String arr[], String file){
        IEmail email=x.makeEmail(arr);
        String name= Long.toString(email.getTime());
        File m = new File("Users/"+email.getSender()+"/"+file+"/"+name);
        if(!m.exists()){
            m.mkdir();
        }
        JSONObject object=new JSONObject();
        object.put("sender",email.getSender());
        object.put("receiver",email.getReceiver());
        object.put("priority",Integer.toString(email.getPriority()));
        object.put("message",email.getMessage());
        object.put("topic",email.getTopic());
        try(FileWriter fileWriter= new FileWriter("Users/"+email.getSender()+"/"+file+"/"+name+"/message.json")) {
            fileWriter.write(object.toString());
            fileWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        String date=formatter.format(new Date(email.getTime()));
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(date);
        jsonArray.add(Integer.toString(email.getPriority()));
        jsonArray.add(email.getSender());
        jsonArray.add(email.getReceiver());
        jsonArray.add(email.getTopic());
        jsonArray.add(Long.toString(email.getTime()));
        JSONParser parser=new JSONParser();

        try{
        	FileReader r5 = new FileReader("Users/"+email.getSender()+"/"+file+"/index.json");
            Object obj=parser.parse(r5);
            r5.close();
            JSONObject jsonObject=(JSONObject)obj;
            jsonObject.put(date,jsonArray);
            FileWriter fileWriter= new FileWriter("Users/"+email.getSender()+"/"+file+"/index.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        //----------------------------------------------\
        File attIndexS = new File("Users/"+arr[0]+"/Draft/"+name+"/attIndex.json");
    	try {
			attIndexS.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}    	
    	JSONArray jarrS = new JSONArray();
    	JSONObject jobjS = new JSONObject();
    	Tools tool3 = new Tools();
    	String mailPath = tool3.mailPath();
        if(arr.length>5){
            for(int i=5;i< arr.length;i++){
                File x=new File(arr[i]);
                String[]temp=arr[i].split("/");
                String temp1 = "Users\\"+arr[0]+"\\Draft\\"+name+"\\"+temp[temp.length-1];
                File sender=new File(temp1);
                jarrS.add(mailPath+"\\"+temp1);
                try {
                    Files.copy(x.toPath(),sender.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            jobjS.put("index", jarrS);
            try {
            	FileWriter fileWriter1= new FileWriter("Users/"+arr[0]+"/Draft/"+name+"/attIndex.json");
            	fileWriter1.write(jobjS.toString());
                fileWriter1.flush();	
            }catch (IOException e){
            	e.printStackTrace();
            }
        }
        //----------------------------------------------\
    }

}
