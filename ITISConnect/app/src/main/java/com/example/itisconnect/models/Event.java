package com.example.itisconnect.models;

import java.util.List;

public class Event
{
    private String eventId;
    private String title;
    private String description;
    private String status;

    public Event()
    {
    }

    public Event(String eventId, String title, String description, String status)
    {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Event(String title, String description, String status)
    {
        this.eventId = "event_" + System.currentTimeMillis();
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
