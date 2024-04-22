package com.example.thuchanhthemsach;

import java.util.List;

public class News
{
    private String title, author, timeRelease;
    private List<String> categories;

    public News(String title, String author, String timeRelease, List<String> categories)
    {
        this.title = title;
        this.author = author;
        this.timeRelease = timeRelease;
        this.categories = categories;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTimeRelease()
    {
        return timeRelease;
    }

    public void setTimeRelease(String timeRelease)
    {
        this.timeRelease = timeRelease;
    }

    public List<String> getCategories()
    {
        return categories;
    }

    public void setCategories(List<String> categories)
    {
        this.categories = categories;
    }
}
