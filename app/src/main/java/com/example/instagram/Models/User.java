package com.example.instagram.Models;
// I should call this model user account details
public class User {
    private String name;
    private String userName;
    private String gender;
    private String phone_number;
    private String bday;
    private int age;
    private int followers;
    private int following;
    private int posts;
    private String profileDescription;

    public User(String _name, String birthday, int _age,  String phoneNumber, String _gender, String _username, int followersCount, int followingCount, int _posts, String description){
        name = _name;
        bday = birthday;
        age = _age;
        phone_number = phoneNumber;
        bday = birthday;
        gender = _gender;
        userName = _username;
        followers = followersCount;
        following = followingCount;
        posts = _posts;
        profileDescription = description;
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }
}
