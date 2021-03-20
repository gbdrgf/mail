package com.mail.mail.UserAndTools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.stream.Stream;

public class mailDeleter {
    public void del(String user,String folder,String filename,String key){
        File from=new File("Users/"+user+"/"+folder+"/"+filename);
        File to=new File("Users/"+user+"/Trash/"+filename);
        try {
        	copyFolder(from.toPath(),to.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser=new JSONParser();
        JSONObject org=new JSONObject();
        JSONObject trash=new JSONObject();
        try{
        	FileReader r1 = new FileReader("Users/"+user+"/"+folder+"/index.json");
            Object obj=parser.parse(r1);
            r1.close();
            org=(JSONObject)obj;
            FileReader r2 = new FileReader("Users/"+user+"/Trash/index.json");
            obj=parser.parse(r2);
            r2.close();
            trash=(JSONObject)obj;

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!folder.equals("Trash")) {
        	trash.put(key,org.get(key));
        	org.remove(key);
        }else {
        	trash.remove(key);
        }
        
        try(FileWriter fileWriter= new FileWriter("Users/"+user+"/"+folder+"/index.json")) {
            fileWriter.write(org.toString());
            fileWriter.flush();
        }catch (IOException e){
        	e.printStackTrace();
        }
        try(FileWriter fileWriter= new FileWriter("Users/"+user+"/Trash/index.json")) {
            fileWriter.write(trash.toString());
            fileWriter.flush();
        }catch (IOException e){
        	e.printStackTrace();
        }
        try(FileReader r= new FileReader("Users/"+user+"/Trash/" + filename +"/attIndex.json")) {
            JSONObject jobj = (JSONObject)parser.parse(r);
            r.close();
            JSONArray jarr = (JSONArray)jobj.get("index");
            JSONObject newObj = new JSONObject();
            JSONArray newArr = new JSONArray();
            Iterator<String> it = jarr.iterator();
            while(it.hasNext()) {
            	String temp = (String)it.next();
            	temp = temp.replaceAll(folder, "Trash");
            	newArr.add(temp);
            }newObj.put("index", newArr);
            FileWriter fw= new FileWriter("Users/"+user+"/Trash/" + filename +"/attIndex.json");
            fw.write(newObj.toString());
            fw.flush();
        } catch (IOException e){
        	e.printStackTrace();
        } catch (ParseException e) {
        	e.printStackTrace();
		}
    }
    private  void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }
 
    private void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
