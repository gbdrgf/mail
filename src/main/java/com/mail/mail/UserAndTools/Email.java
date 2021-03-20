package com.mail.mail.UserAndTools;

public class Email implements IEmail {
    String sender;
    String receiver;
    int priority;
    String message;
    String topic;
    long time;
    public Email(String sender,String receiver,int priority,String message,String topic){
        this.sender=sender;
        this.receiver=receiver;
        this.priority=priority;
        this.message=message;
        this.topic=topic;
        time=System.currentTimeMillis();
    }
    public String getSender(){
        return sender;
    }
    public String getReceiver(){
        return receiver;
    }
    public String getMessage(){
        return message;
    }
    public int getPriority(){
        return priority;
    }
    public String getTopic(){
        return topic;
    }
    public long getTime() {
        return time;
    }
}
