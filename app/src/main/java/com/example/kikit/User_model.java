package com.example.kikit;

import android.net.Uri;

public class User_model {

    String Name;
    String Email;
    String Password;
    String uid;
    String description;
    String User_key;
    String profileUrl;

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public User_model(String name, String email, String password, String uid, String description, String user_key, String profileUrl, Uri photo_url, String joins) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
        this.description = description;
        User_key = user_key;
        this.profileUrl = profileUrl;
        this.photo_url = photo_url;
        this.joins = joins;
    }

    public String getUser_key() {
        return User_key;
    }

    public void setUser_key(String user_key) {
        User_key = user_key;
    }

    public User_model(String name, String email, String password, String uid, String description, String user_key, Uri photo_url, String joins) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
        this.description = description;
        User_key = user_key;
        this.photo_url = photo_url;
        this.joins = joins;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User_model(String name, String email, String password, String uid, String description, Uri photo_url, String joins) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
        this.description = description;
        this.photo_url = photo_url;
        this.joins = joins;
    }

    Uri photo_url;

    public User_model(String name, String email, String password, String uid) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
    }

    public User_model(String name, String email, String password, String uid, Uri photo_url) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
        this.photo_url = photo_url;
    }

    public User_model(String name, String email, String password, String uid, Uri photo_url, String joins) {
        Name = name;
        Email = email;
        Password = password;
        this.uid = uid;
        this.photo_url = photo_url;
        this.joins = joins;
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
