package com.example.sportsapp;

public class Sport
{
    String sportName;
    int sportImage;

    public Sport(String sportName, int sportImage)
    {
        this.sportName = sportName;
        this.sportImage = sportImage;
    }

    public String getSportName()
    {
        return sportName;
    }

    public void setSportName(String sportName)
    {
        this.sportName = sportName;
    }

    public int getSportImage()
    {
        return sportImage;
    }

    public void setSportImage(int sportImage)
    {
        this.sportImage = sportImage;
    }
}
