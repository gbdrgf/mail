package com.mail.mail.UserAndTools;

public interface IEmail {
    public String getSender();
    public String getReceiver();
    public String getMessage();
    public int getPriority();
    public String getTopic();
    public long getTime();
}
