package com.example.instagram.Models;

public class Followers {
    private String username;
    public Followers( String _username){
        username = _username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
