package com.example.agam;

public class Message {

    public String encrypt_msg , shortId;

    public Message() {
    }

    public Message(String encrypt_msg, String shortId) {
        this.shortId = shortId;
        this.encrypt_msg = encrypt_msg;

    }
}
