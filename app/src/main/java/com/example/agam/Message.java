package com.example.agam;

public class Message {

    public String encrypt_msg , shortId, userName;

    public Message() {
    }

    public Message(String encrypt_msg, String shortId, String userName) {
        this.shortId = shortId;
        this.encrypt_msg = encrypt_msg;
        this.userName = userName;

    }
}
