package com.mail.mail.UserAndTools;

public class mailFactory {
    public IEmail makeEmail(String[] arr){
        Email x=new Email(arr[0],arr[1],Integer.parseInt(arr[2]),arr[3],arr[4]);
        return x;
    }
}
