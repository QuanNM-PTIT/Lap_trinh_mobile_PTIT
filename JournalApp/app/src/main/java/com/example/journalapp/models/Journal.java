package com.example.journalapp.models;

import com.google.firebase.Timestamp;

public class Journal
{
    private String title;
    private String thoughts;
    private String imageUrl;
    private String userId;
    private Timestamp timeAdded;
    private String username;

    public Journal()
    {
    }

    public Journal(String title, String thoughts, String imageUrl, String userId, Timestamp timeAdded, String username)
    {
        this.title = title;
        this.thoughts = thoughts;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.username = username;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getThoughts()
    {
        return thoughts;
    }

    public void setThoughts(String thoughts)
    {
        this.thoughts = thoughts;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Timestamp getTimeAdded()
    {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded)
    {
        this.timeAdded = timeAdded;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
