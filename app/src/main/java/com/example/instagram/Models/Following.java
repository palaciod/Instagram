package com.example.instagram.Models;

public class Following {

    private String username;
    public Following(String _username){
        username = _username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
