package com.example.itisconnect.models;

import com.google.firebase.Timestamp;

public class Task
{
    private String taskId;
    private String title;
    private String description;
    private String assignedToEmail;
    private String startDate, dueDate;
    private String status;
    private String comment;

    public Task()
    {
    }

    public Task(String title, String description, String assignedToEmail, String startDate, String dueDate, String status, String comment)
    {
        this.taskId = "task_" + Timestamp.now().getSeconds();
        this.title = title;
        this.description = description;
        this.assignedToEmail = assignedToEmail;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.comment = comment;
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

    public String getAssignedToEmail()
    {
        return assignedToEmail;
    }

    public void setAssignedToEmail(String assignedToEmail)
    {
        this.assignedToEmail = assignedToEmail;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getTaskId()
    {
        return taskId;
    }
}
