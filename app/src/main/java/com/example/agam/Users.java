package com.example.agam;

public class Users {

    public String userEmail, userId, userUniqId, userName;

    public Users() {
    }

    public Users(String userEmail, String userId, String userUniqId, String userName) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userUniqId = userUniqId;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUniqId() {
        return userUniqId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}

