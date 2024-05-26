package com.example.itisconnect.models;

public class User
{
    private String email;
    private String fullName;
    private String birthDate;
    private String placeOfOrigin;
    private String sex;
    private String phoneNumber;
    private String cohort;
    private String department;
    private String position;
    private String avatar;
    private boolean roleAdmin;
    private int priority;

    public User()
    {
    }

    public User(String email, String fullName, String birthDate, String placeOfOrigin, String sex, String phoneNumber, String cohort, String department, String position, String avatar, boolean roleAdmin, int priority)
    {
        this.email = email;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.placeOfOrigin = placeOfOrigin;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.cohort = cohort;
        this.department = department;
        this.position = position;
        this.avatar = avatar;
        this.roleAdmin = roleAdmin;
        this.priority = priority;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(String birthDate)
    {
        this.birthDate = birthDate;
    }

    public String getPlaceOfOrigin()
    {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin)
    {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getCohort()
    {
        return cohort;
    }

    public void setCohort(String cohort)
    {
        this.cohort = cohort;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public boolean isRoleAdmin()
    {
        return roleAdmin;
    }

    public void setRoleAdmin(boolean roleAdmin)
    {
        this.roleAdmin = roleAdmin;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
