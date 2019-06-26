package com.example.instagram.Models;

public class User {
    private String userName;
    private String password;
    private Integer gender;
    private String email;
    private Integer phone_number;

    public User(){

    }

    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public Integer getGender(){
        return gender;
    }
    public String getEmail(){
        return email;
    }
    public Integer getPhone_number(){
        return phone_number;
    }
    public void setUserName(String _username){
        this.userName = _username;
    }
    public void setPassword(String pass){
        this.password = pass;
    }
    public void setGender(Integer _gender){
        this.gender = _gender;
    }
    public void setEmail(String _email){
        this.email = _email;
    }
    public void setPhone_number(Integer number){
        this.phone_number = number;
    }
}
