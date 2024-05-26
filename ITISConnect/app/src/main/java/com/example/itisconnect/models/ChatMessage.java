package com.example.itisconnect.models;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChatMessage
{
    private String senderId;
    private String messageText;
    private long time;
    private boolean isMine;
    private String senderEmail;

    public ChatMessage()
    {
    }

    public ChatMessage(String senderId, String messageText, long time, String senderEmail)
    {
        this.senderId = senderId;
        this.messageText = messageText;
        this.time = time;
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail()
    {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail)
    {
        this.senderEmail = senderEmail;
    }

    public String getSenderId()
    {
        return senderId;
    }

    public void setSenderId(String senderId)
    {
        this.senderId = senderId;
    }

    public String getMessageText()
    {
        return messageText;
    }

    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public boolean isMine()
    {
        if (senderId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            isMine = true;
        }
        else
        {
            isMine = false;
        }
        return isMine;
    }

    public void setMine(boolean mine)
    {
        isMine = mine;
    }

    public String convertTime()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(getTime());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return simpleDateFormat.format(date);
    }
}
