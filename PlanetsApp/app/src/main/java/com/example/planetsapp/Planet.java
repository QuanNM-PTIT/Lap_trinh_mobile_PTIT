package com.example.planetsapp;

public class Planet
{
    private String name, moonCount;
    private int image;

    public Planet(String name, String moonCount, int image)
    {
        this.name = name;
        this.moonCount = moonCount;
        this.image = image;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMoonCount()
    {
        return moonCount;
    }

    public void setMoonCount(String moonCount)
    {
        this.moonCount = moonCount;
    }

    public int getImage()
    {
        return image;
    }

    public void setImage(int image)
    {
        this.image = image;
    }
}
