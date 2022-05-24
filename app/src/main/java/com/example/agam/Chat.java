package com.example.agam;

public class Chat {
    public Message[] messagesArray;
    public String name;

    public Chat() {
    }

    public Chat(String name, Message[] messagesArray) {
        this.name = name;
        this.messagesArray = messagesArray;
    }

    public Message[] getMessagesArray() {
        return messagesArray;
    }

    public void setMessagesArray(Message[] messagesArray) {
        this.messagesArray = messagesArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
