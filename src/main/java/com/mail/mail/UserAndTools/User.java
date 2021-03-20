package com.mail.mail.UserAndTools;

import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardCopyOption.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class User implements IUser {
    private String email, username, password, avatarNum;
    private String inboxNum, sentNum, trashNum, draftNum;
    // sign up constructor
    public User(String email, String username, String password, String avatarNum) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatarNum = avatarNum;
        this.inboxNum = "0";
        this.sentNum = "0";
        this.trashNum = "0";
        this.draftNum = "0";
        File file = new File("Users/"+email +"/"+ "Contacts.json");
        JSONObject contacts = new JSONObject();
        contacts.put("", "");
        try {
			file.createNewFile();
			FileWriter writer = new FileWriter("Users/"+ email +"/"+ "Contacts.json");
			writer.write(contacts.toJSONString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
        File inbox = new File("Users/"+ email +"/Inbox");
        File inboxi = new File("Users/"+ email +"/Inbox/index.json");
        
        File sent = new File("Users/"+ email +"/Sent");
        File senti = new File("Users/"+ email +"/Sent/index.json");

        File trash = new File("Users/"+ email +"/Trash");
        File trashi = new File("Users/"+ email +"/Trash/index.json");

        File draft = new File("Users/"+ email +"/Draft");
        File drafti = new File("Users/"+ email +"/Draft/index.json");
        
        inbox.mkdir();
        sent.mkdir();
        trash.mkdir();
        draft.mkdir();
        
        String[] arr = new String[4];
        arr[0] = "Inbox"; arr[1] = "Sent"; arr[2] = "Trash"; arr[3] = "Draft";
        for(int i = 0; i < arr.length; i++) {
            JSONObject jobj = new JSONObject();
            jobj.put("", "");
            try {
    			file.createNewFile();
    			FileWriter writer = new FileWriter("Users/"+ email +"/"+ arr[i] + "/index.json");
    			writer.write(jobj.toJSONString());
    			writer.flush();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
        }   
        modify(true);
    }

    // sign in constructor (email already exists)
    public User(String email) {
        JSONParser parser = new JSONParser();
        String path = "Users/"+email +"/"+ "BasicData.json";
        try {
            FileReader reader = new FileReader(path);
            JSONObject obj = (JSONObject)parser.parse(reader);
            reader.close();
            this.email = (String)obj.get("email");
            this.username = (String)obj.get("username");
            this.password = (String)obj.get("password");
            this.avatarNum = (String)obj.get("avatarNum");
            this.inboxNum = (String)obj.get("inboxNum");
            this.sentNum = (String)obj.get("sentNum");
            this.draftNum = (String)obj.get("draftNum");
            this.trashNum = (String)obj.get("trashNum");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
    public String getAvatarNum(){
        return avatarNum;
    }
    public void setAvatarNum(String avatarNum){
        this.avatarNum = avatarNum;
        modify(false);
    }
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
        modify(false);
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
        modify(false);  
    }public void addContact(String email) {
    	JSONParser parser = new JSONParser();
    	try {
            FileReader reader = new FileReader("Users/"+ this.email +"/"+ "Contacts.json");
            JSONObject obj = (JSONObject)parser.parse(reader);
            reader.close();
            obj.put(Integer.toString(obj.size()), email);
            File file = new File("Users/"+ this.email +"/"+ "Contacts.json");
            file.delete();
            File newFile = new File("Users/"+ this.email +"/"+ "Contacts.json");
            newFile.createNewFile();
            FileWriter writer = new FileWriter("Users/"+ this.email +"/"+ "Contacts.json");
            writer.write(obj.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }public void deleteContact(String email) {
    	JSONParser parser = new JSONParser();
    	int key = 0;
    	try {
            FileReader reader = new FileReader("Users/"+ this.email +"/"+ "Contacts.json");
            JSONObject obj = (JSONObject)parser.parse(reader);
            reader.close();
            for(int i = 1; i < obj.size(); i++) {
            	if(((String)obj.get(Integer.toString(i))).equals(email)) {
            		key = i;
            		break;
            	}
            }
            obj.remove(Integer.toString(key));
            JSONObject newObj = new JSONObject();
            newObj.put("", "");
            
            int j = 1;
            for(int i = 1; i < obj.size(); i++) {
            	while(!obj.containsKey(Integer.toString(j))) {
            		j++;
            	}
            	newObj.put(Integer.toString(i), (String)obj.get(Integer.toString(j)));
            	j++;
            }
            FileWriter writer = new FileWriter("Users/"+ this.email +"/"+ "Contacts.json");
            writer.write(newObj.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }public boolean isContact(String email) {
    	JSONParser parser = new JSONParser();
    	try {
            FileReader reader = new FileReader("Users/"+ this.email +"/"+ "Contacts.json");
            JSONObject obj = (JSONObject)parser.parse(reader);
            reader.close();
            if(!obj.containsValue(email)) {
            	return false;
            }return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }return false;
    }
    public void incInbox() {
    	int temp = Integer.parseInt(inboxNum);
    	temp++;
    	inboxNum = Integer.toString(temp);
    	modify(false);
    }
    public void incSent() {
    	int temp = Integer.parseInt(sentNum);
    	temp++;
    	sentNum = Integer.toString(temp);
    	modify(false);
    }public void incDraft() {
    	int temp = Integer.parseInt(draftNum);
    	temp++;
    	draftNum = Integer.toString(temp);
    	modify(false);
    }public void incTrash() {
    	int temp = Integer.parseInt(trashNum);
    	temp++;
    	trashNum = Integer.toString(temp);
    	modify(false);
    }public void decInbox() {
    	int temp = Integer.parseInt(inboxNum);
    	temp--;
    	inboxNum = Integer.toString(temp);
    	modify(false);
    }
    public void decSent() {
    	int temp = Integer.parseInt(sentNum);
    	temp--;
    	sentNum = Integer.toString(temp);
    	modify(false);
    }public void decDraft() {
    	int temp = Integer.parseInt(draftNum);
    	temp--;
    	draftNum = Integer.toString(temp);
    	modify(false);
    }public void decTrash() {
    	int temp = Integer.parseInt(trashNum);
    	temp--;
    	trashNum = Integer.toString(temp);
    	modify(false);
    }
    private void modify(boolean signup){
    	String path = "Users/"+ email +"/"+ "BasicData.json";
        if(signup){
        	File i = new File(path);
            try {
                i.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject user = new JSONObject();
        user.put("email", email);
        user.put("username", username);
        user.put("password", password);
        user.put("avatarNum", avatarNum);
        user.put("inboxNum", inboxNum);
        user.put("sentNum", sentNum);
        user.put("draftNum", draftNum);
        user.put("trashNum", trashNum);
        
        try (FileWriter file = new FileWriter(path)) {
            file.write(user.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
