package com.example.thuchanh;

import java.util.List;

public class Player
{
    private String name;
    private String date;
    private String sex;
    private List<String> positions;

    public Player()
    {

    }

    public Player(String name, String date, String sex, List<String> position)
    {
        this.name = name;
        this.date = date;
        this.sex = sex;
        this.positions = position;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public List<String> getPosition()
    {
        return positions;
    }

    public void setPosition(List<String> position)
    {
        this.positions = position;
    }
}

