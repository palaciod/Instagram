package com.example.instagram.Models;

public class Comment {
    private String username;
    private String comment;
    public Comment(String _username, String _comment){
        username = _username;
        comment = _comment;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
