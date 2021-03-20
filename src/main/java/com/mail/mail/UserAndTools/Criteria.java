package com.mail.mail.UserAndTools;

import java.util.ArrayList;

public class Criteria {
    public String[] filter(String [] arr,String filter_by, String search_word){
        ArrayList<String> x=new ArrayList();
        int index=0;
        if(filter_by.equals("date")){
            index=0;
        }else if(filter_by.equals("priority")){
            index=1;
        }else if(filter_by.equals("sender")){
            index=2;
        }else if(filter_by.equals("receiver")){
            index=3;
        }else if(filter_by.equals("topic")){
            index=4;
        }
        for(int i =0;i< arr.length;i=i+6){
            String test=arr[i+index];
            if(test.contains(search_word)){
                for(int j=i;j<(i+6);j++){
                    x.add(arr[j]);
                }
            }
        }
        String [] ans= x.toArray(new String[0]);
        return ans;
    }
}
