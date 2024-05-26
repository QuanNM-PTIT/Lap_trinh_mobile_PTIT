package com.example.itisconnect.models;

import com.google.firebase.Timestamp;

public class Post
{
    private String title;
    private String description;
    private String imageUrl;
    private String username;
    private String userEmail;
    private String userAvatar;
    private Timestamp timeAdded;

    public Post()
    {
    }

    public Post(String title, String description, String imageUrl, String username, String userEmail, String userAvatar, Timestamp timeAdded)
    {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.username = username;
        this.userEmail = userEmail;
        this.userAvatar = userAvatar;
        this.timeAdded = timeAdded;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getuserEmail()
    {
        return userEmail;
    }

    public void setuserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public String getUserAvatar()
    {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar)
    {
        this.userAvatar = userAvatar;
    }

    public Timestamp getTimeAdded()
    {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded)
    {
        this.timeAdded = timeAdded;
    }
}
