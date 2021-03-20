package com.mail.mail.UserAndTools;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Controller {
	Tools tool = new Tools();
	@GetMapping("/sendEmail")
	public String sendEmail(String[] arr) {
		//arr contains: receiver, priority, message, topic
		Boolean b = tool.sendEmail(arr);
		return b.toString();
	}
	@GetMapping("/draftEmail")
	public void draftEmail(String[] arr) {
		//arr contains: receiver, priority, message, topic
		tool.draftEmail(arr);
	}
	@GetMapping("/signin")
	public String signIn(String[] arr) {
		//arr: email, password
		Boolean b = tool.signin(arr[0], arr[1]);
		System.out.println("arrived");
		return b.toString();
	}
	@GetMapping("/signup")
	public String signUp(String[] arr) {
		//arr order: email, username, password
		Boolean b = tool.signup(arr[0], arr[1], arr[2], "1");
		return b.toString();
	}
	@GetMapping("/signout")
	public void signout() {
		tool.signout();
	}
	@GetMapping("/user")
	public String[] user() {
		String[] arr = new String[2];
		arr[0] = tool.getUsername();
		arr[1] = tool.getEmail();
		return arr;
	}
	@GetMapping("/getIndex")
	public JSONObject[] getIndex(String folder) {
		String[] ind = tool.getIndex(folder);
		String[] arrTemp = tool.sort(ind, "date");
		String[] arr = new String[arrTemp.length];
		int j = 0;
		for(int i = arr.length-6; i >= 0; i -= 6) {
			arr[j] = arrTemp[i]; j++;
			arr[j] = arrTemp[i + 1]; j++;
			arr[j] = arrTemp[i + 2]; j++;
			arr[j] = arrTemp[i + 3]; j++;
			arr[j] = arrTemp[i + 4]; j++;
			arr[j] = arrTemp[i + 5]; j++;
		}
		JSONObject[] jarr = new JSONObject[arr.length/6];
		for(int i = 0; i < jarr.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("subject", arr[i * 6 + 4]);
			obj.put("name", arr[i * 6 + 3]);
			obj.put("email", arr[i * 6 + 2]);
			obj.put("date", arr[i * 6 + 0]);
			obj.put("priority", arr[i * 6 + 1]);
			String[] email = tool.getEmail(folder, arr[0]);
			obj.put("message", email[3]);
			String[] att = tool.getAttachments(arr[i * 6 + 5], folder);
			String s = "";
			for(int k = 0; k < att.length; k++) {
				s += att[k];
				if(k != att.length - 1) {
					s+="$";
				}
			}	
			obj.put("attachments", s);
			jarr[i] = obj;
		}
		return jarr;
 	}
	@GetMapping("/sort")
	public JSONObject[] sort(String[] a) {
		String folder = a[0]; String critera = a[1];
		String[] arrTemp = tool.sort(tool.getIndex(folder), critera);
		String[] arr = new String[arrTemp.length];

		if(!critera.equals("date")) {
			arr = arrTemp;
		}else {
			int j = 0;
			for(int i = arr.length-6; i >= 0; i -= 6) {
				arr[j] = arrTemp[i]; j++;
				arr[j] = arrTemp[i + 1]; j++;
				arr[j] = arrTemp[i + 2]; j++;
				arr[j] = arrTemp[i + 3]; j++;
				arr[j] = arrTemp[i + 4]; j++;
				arr[j] = arrTemp[i + 5]; j++;
			}
		}
		JSONObject[] jarr = new JSONObject[arr.length/6];
		for(int i = 0; i < jarr.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("subject", arr[i * 6 + 4]);
			obj.put("name", arr[i * 6 + 3]);
			obj.put("email", arr[i * 6 + 2]);
			obj.put("date", arr[i * 6 + 0]);
			obj.put("priority", arr[i * 6 + 1]);
			String[] email = tool.getEmail(folder, arr[0]);
			obj.put("message", email[3]);
			String[] att = tool.getAttachments(arr[i * 6 + 5], folder);
			String s = "";
			for(int k = 0; k < att.length; k++) {
				s += att[k];
				if(k != att.length - 1) {
					s+="$";
				}
			}	
			obj.put("attachments", s);
			jarr[i] = obj;
		}
		return jarr;
	}
	@GetMapping("/filter")
	public JSONObject[] filter(String[] a) {
		System.out.println("Filter");
		String folder = a[0]; String criteria = a[1]; String word = a[2];
		String[] arr = tool.filter(tool.getIndex(folder), criteria, word);
		JSONObject[] jarr = new JSONObject[arr.length/6];
		for(int i = 0; i < jarr.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("subject", arr[i * 6 + 4]);
			obj.put("name", arr[i * 6 + 3]);
			obj.put("email", arr[i * 6 + 2]);
			obj.put("date", arr[i * 6 + 0]);
			obj.put("priority", arr[i * 6 + 1]);
			String[] email = tool.getEmail(folder, arr[0]);
			obj.put("message", email[3]);
			String[] att = tool.getAttachments(arr[i * 6 + 5], folder);
			String s = "";
			for(int k = 0; k < att.length; k++) {
				s += att[k];
				if(k != att.length - 1) {
					s+="$";
				}
			}	
			obj.put("attachments", s);
			jarr[i] = obj;
		}
		return jarr;
	}
	@GetMapping("/delete")
	public String delete(String[] arr) { //tested
		//arr contain 2 strings, the email date and the folder where email exists ex: Inbox
		tool.deleteEmail(arr);
		return "works";
	}
	@GetMapping("/addContacts")
	public String addContact(String email) {
		Boolean b = tool.addContact(email);
		return b.toString();
	}
	@PostMapping("/sortContacts")
	public JSONObject[] sortContacts(String criteria) {
		//Criteria is eithe "username" or "email"
		String[] arr = tool.sortContacts(tool.getContacts(), criteria);
		JSONObject[] jarr = new JSONObject[arr.length/3];
		for(int i = 0; i < jarr.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("name", arr[i * 3 + 1]);
			obj.put("email", arr[i * 3 + 2]);
			jarr[i] = obj;
		}return jarr;
	}
	@GetMapping("/getContacts")
	public JSONObject[] getContacts() {
		String[] arr = tool.getContacts();
		JSONObject[] jarr = new JSONObject[arr.length/3];
		for(int i = 0; i < jarr.length; i++) {
			JSONObject obj = new JSONObject();
			obj.put("name", arr[i * 3 + 1]);
			obj.put("email", arr[i * 3 + 2]);
			jarr[i] = obj;
		}return jarr;
	}
	@GetMapping("/deleteContact")
	public void deleteContact(String email) {
		tool.deleteContact(email);
	}
	@GetMapping("/changeUsername")
	public String changeUsername(String newUsername) { //tested
		Boolean b = tool.changeUsername(newUsername);
		return b.toString();
	}
	@GetMapping("/changePassword")
	public String changePassword(String currentPassword, String newPassword) {//tested
		Boolean b = tool.changePassword(currentPassword, newPassword);
		return b.toString();
	}


}
