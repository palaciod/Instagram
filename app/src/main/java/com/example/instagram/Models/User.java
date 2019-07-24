package com.example.instagram.Models;
// I should call this model user account details
public class User {
    private String name;
    private String userName;
    private String gender;
    private String phone_number;
    private String bday;
    private int age;

    public User(String _name, String birthday, int _age,  String phoneNumber, String _gender, String _username){
        name = _name;
        bday = birthday;
        age = _age;
        phone_number = phoneNumber;
        bday = birthday;
        gender = _gender;
        userName = _username;
    }
    public String getName(){
        return name;
    }
    public String getUserName(){
        return userName;
    }

    public String getGender(){
        return gender;
    }
    public String getPhone_number(){
        return phone_number;
    }
    public void setUserName(String _username){
        this.userName = _username;
    }

    public void setPhone_number(String number){
        this.phone_number = number;
    }
    public void setName(String _name){
        name  = _name;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
