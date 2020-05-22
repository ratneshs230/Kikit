package com.example.kikit;

import android.net.Uri;

public class User_model {

    String Name;
    String Email;
    String Password;
    String uid;
    String description;
    String User_key;
    String storyKey;
    Uri photo_url;
    String profilePic_string;

    public String getProfilePic_string() {
        return profilePic_string;
    }

    public void setProfilePic_string(String profilePic_string) {
        this.profilePic_string = profilePic_string;
    }

    String name, email, password, user_key;


    public String getStoryKey() {
        return storyKey;
    }

    public void setStoryKey(String storyKey) {
        this.storyKey = storyKey;
    }


    public String getUser_key() {
        return User_key;
    }

    public void setUser_key(String user_key) {
        User_key = user_key;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public User_model() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Uri getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(Uri photo_url) {
        this.photo_url = photo_url;
    }

    public String getJoins() {
        return joins;
    }

    public void setJoins(String joins) {
        this.joins = joins;
    }

    String joins;

}
