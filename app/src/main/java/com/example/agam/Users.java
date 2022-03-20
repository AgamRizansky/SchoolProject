package com.example.agam;

public class Users {

    public String userEmail, userId, userUniqId;

    public Users() {
    }

    public Users(String userEmail, String userId, String userUniqId) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userUniqId = userUniqId;
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

    public void setUserUniqId(String userUniqId) {
        this.userUniqId = userUniqId;
    }
}
