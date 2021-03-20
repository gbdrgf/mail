package com.mail.mail.UserAndTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Proxy class
 * @author Team
 * This class checks whether the request comes from an authorized user or not and then pass the request to the rest of classes
 */
public class Tools {
    User user = null;
    IUser immutable = null;
    Path path = Paths.get("");
    Path absPath = path.toAbsolutePath();
    String backendPath = absPath.toString();
    public String mailPath() {
    	return backendPath;
    }
    /**
     * Sign up method
     * @param email
     * @param username
     * @param password
     * @param avatarNum
     * @return true if sign up successful
     */
    public boolean signup(String email, String username, String password, String avatarNum){
        File file = new File("Users/" + email);
        String temp = email.substring(email.length()-13, email.length());
    	if((email.length() < 14) || !temp.equals("@farmmail.com")) {
    		return false;
    	}
        if(file.mkdir()){
            user = new User(email, username, password, avatarNum);
            user = null;
            return true;
        }else{
            return false;
        }
    }
    /**
     * Sign in method
     * @param email
     * @param password
     * @return true if sign in was successful
     */
    public boolean signin(String email, String password){
        File file = new File("Users/" + email);
        if(file.exists()){
            user = new User(email);
            immutable = user;
            if(!immutable.getPassword().equals(password)){
                user = null;
                return false;
            }
            checkTrash();
            return true;
        }else{
            return false;
        }
    }
    /**
     * Sign out method
     */
    
    public void signout(){
        user = null;
    }
    /**
     * This method help user changes his/her password
     * @param password  old password
     * @param newPassword  new password
     * @return	true is password changed successfully
     */
    public boolean changePassword(String password, String newPassword){
        if(!immutable.getPassword().equals(password)){
            return false;
        }else{
            user.setPassword(newPassword);
            return true;
        }
    }
    /**
     * This method helps user change his/her username 
     * @param newUsername
     * @return true if username was changed successfully
     */
    public boolean changeUsername(String newUsername){
        if(immutable.getUsername().equals(newUsername)){
            return false;
        }else{
            user.setUsername(newUsername);
            return true;
        }
    }
    /**
     * This method helps user change his/her avatar
     * @param newAvatarNum
     * @return true if the avatar was changed successfully
     */
    public boolean changeAvatar(String newAvatarNum){
        if(immutable.getAvatarNum().equals(newAvatarNum)){
            return false;
        }else{
            user.setAvatarNum(newAvatarNum);
            return true;
        }
    }
    /**
     * This method is used to add contact to user's contacts
     * @param email
     * @return true is contact was added successfully
     */
    public boolean addContact(String email) {
    	File file = new File("Users/" + email);
        if(file.exists()){
        	if(user.isContact(email)) {
        		return false;
        	}else {
        		user.addContact(email);
        		return true;
        	}
        }else{
            return false;
        }
    }
    /**
     * This method is used to delete a guaranteed existing contact
     * @param email
     */
    public void deleteContact(String email) {
    	user.deleteContact(email);
    }
    /**
     * This method is used to show the contacts of a user
     * @return Strong array containing contacts data
     */
    public String[] getContacts() {
    	JSONParser parser = new JSONParser();
    	String[] arr;
    	int j = 0;
    	try {
            FileReader reader = new FileReader("Users/"+ user.getEmail() +"/"+ "Contacts.json");
            JSONObject obj = (JSONObject)parser.parse(reader);
            reader.close();
            arr = new String[(obj.size()-1)*3];
            for(int i = 1; i < obj.size(); i++) {
            	IUser contact = new User((String)obj.get(Integer.toString(i)));
            	arr[j] = contact.getAvatarNum(); j++;
            	arr[j] = contact.getUsername();	 j++;
            	arr[j] = contact.getEmail(); 	 j++;
            }return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }return null;
    }
    /**
     * This is the method responsible for sending email
     * @param a the email data
     * @return true if email was sent successfully
     */
    public boolean sendEmail(String[] a) {
    	//Array a contains receiver, priority, message, topic, date
    	File file = new File("Users/" + a[0]);
        if(!file.mkdir()){
        	mailWriter writer = new mailWriter();
        	String[] arr = new String[a.length+1];
        	arr[0] = user.getEmail();
        	for(int i = 0; i < a.length; i++) {
        		arr[i+1] = a[i];
        	}user.incSent();
        	User receiver = new User(arr[1]);
        	receiver.incInbox();
        	writer.makeMail(arr);
            return true;
        }else{
        	file.delete();
            return false;
        }    	
    }
    /**
     * This method is used to draft an email
     * @param a the email data
     */
    public void draftEmail(String[] a) {
    	mailWriter writer = new mailWriter();
    	String[] arr = new String[a.length+1];
    	arr[0] = user.getEmail();
    	for(int i = 0; i < a.length; i++) {
    		arr[i+1] = a[i];
    	}user.incDraft();
    	writer.mail_inFile(arr, "Draft");
    }
    /**
     * This method is used to move an email to trash
     * @param arr Containing two elements, the email date as email identifier and folder where email exists
     */
    public void deleteEmail(String[] arr) {
    	//arr contain two fields: date and folder example for folder: Inbox, Sent or Draft
    	user.incTrash();
    	if(arr[1].equals("Inbox")) {
    		user.decInbox();
    	}else if(arr[1].equals("Sent")) {
    		user.decSent();
    	}else if(arr[1].equals("Draft")) {
    		user.decDraft();
    	}
    	mailDeleter del = new mailDeleter();
    	del.del(user.getEmail(), arr[1], dateToMillis(arr[0], arr[1]), arr[0]);
    }
    /**
     * This method checks if there is  an email deleted more than a month ago
     */
    private void checkTrash() {
    	Long milliTime = System.currentTimeMillis();
    	Long month = 2629800000L;
    	JSONParser parser = new JSONParser();
        String path = "Users/"+ user.getEmail() + "/Trash/index.json";
        try {
            FileReader reader = new FileReader(path);
            JSONObject obj = (JSONObject)parser.parse(reader);
            reader.close();
            Iterator it = obj.keySet().iterator();
            it.next();
            while(it.hasNext()) {
            	String key = (String) it.next();
            	JSONArray arr = (JSONArray) obj.get(key);
            	String eMillis = (String)arr.get(0);
            	Long timeDiff = milliTime - Long.parseLong(dateToMillis(eMillis, "Trash"));
            	if(timeDiff > month) {
            		obj.remove(key);
            		File file = new File("Users/" + user.getEmail() + "/Trash/" + arr.get(0));
            		file.delete();
            	}
            }
            try {
    			FileWriter writer = new FileWriter("Users/"+ user.getEmail() + "/Trash/index.json");
    			writer.write(obj.toJSONString());
    			writer.flush();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method is used to map the email date to the email folder name
     * @param date  The email date
     * @param folder The folder where email exists
     * @return the millis of a date
     */
    public String dateToMillis(String date, String folder) {
    	String[] arr = getIndex(folder);
    	for(int i = 0; i < arr.length; i += 6) {
    		if(date.equals(arr[i])) {
    			return arr[i + 5];
    		}
    	}return null;
    }
    /**
     * This method is used to show email data
     * @param folder Folder where email exists
     * @param date The date of the email
     * @return the e-mails' list
     */
    public String[] getEmail(String folder, String date) {
    	String name = dateToMillis(date, folder);
    	mailWriter writer = new mailWriter();
    	IEmail mail = writer.getMail(user.getEmail(), folder, name);
    	String[] arr = new String[5];
    	arr[0] = mail.getSender(); arr[1] = mail.getReceiver();
    	arr[2] = Integer.toString(mail.getPriority());
    	arr[3] = mail.getMessage(); arr[4] = mail.getTopic();
    	return arr;
    }
    /**
     * This method lists the index file that maps the e-mails
     * @param folder
     * @return
     */
    public String[] getIndex(String folder) {
    	mailWriter writer = new mailWriter();
    	return writer.getIndex(user.getEmail(), folder);
    }
    /**
     * This method is used to list the attachments of an e-mail
     * @param name Email folder name
     * @param folder Folder where the e-mail folder exists
     * @return the attachments' list
     */
    public String[] getAttachments(String name, String folder) {
    	try {
			FileReader file = new FileReader("Users/"+ user.getEmail() +"/" + folder + "/" + name +"/attIndex.json");
			JSONParser parser = new JSONParser();
		    JSONObject jsonObject = new JSONObject();
		    jsonObject = (JSONObject)parser.parse(file);
		    file.close();
		    JSONArray jarr = (JSONArray) jsonObject.get("index");
		    Iterator<String> temp = jarr.iterator();
		    int count = 0;
		    while(temp.hasNext()) {
		    	count++;
		    	temp.next();
		    }Iterator<String> it = jarr.iterator();
		    String[] arr = new String[count];
		    for(int i = 0; i < arr.length; i++) {
		    	arr[i] = (String)it.next();
		    }return arr;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			return new String[0];
		}
    	return null;
    }
    /**
     * This method is used to filter the e-mails' list based on a certain criteria 
     * @param arr The index list 
     * @param filter_by The criteria to be filtered
     * @param search_word Filter key
     * @return the filtered e-mails' list
     */
    public String[] filter(String [] arr,String filter_by, String search_word) {
    	Criteria c = new Criteria();
    	return c.filter(arr, filter_by, search_word);
    }
    /**
     * This method is used to sort the e-mails' list based on a certain criteria
     * @param arr  The index list
     * @param criteria	The criteria to be sorted
     * @return the sorted e-mails' list
     */
    public String[] sort(String[] arr, String criteria) {
    	Sort sorter = new Sort();
    	return sorter.sort(arr, criteria);
    }
    /**
     * This method is used to sort contacts
     * @param arr the contacts to be sorted
     * @return the sorted contacts list
     */
    public String[] sortContacts(String[] arr, String criteria) {
    	Sort sorter = new Sort();
    	return sorter.sortContacts(arr, criteria);
    }
    /**
     * Returns current user's e-mail
     * @return
     */
    public String getEmail() {
    	return user.getEmail();
    }
    /**
     * Returns current user's username
     * @return
     */
    public String getUsername() {
    	return user.getUsername();
    }
}
