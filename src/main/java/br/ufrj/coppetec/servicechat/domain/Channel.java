package br.ufrj.coppetec.servicechat.domain;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class Channel {
    private String chatName;
    private String chatCode;
    private List<SseEmitterIdentifier> emitters;
    private List<MessageConfiguration> messages;
    private List<String> usersBan;

    public Channel() {
        this.emitters = new CopyOnWriteArrayList<>();
        this.messages = new CopyOnWriteArrayList<>();
        this.usersBan = new CopyOnWriteArrayList<>();
    }

    public Channel(String chatName, String chatCode) {
        this.chatName = chatName;
        this.chatCode = chatCode;
        this.emitters = new CopyOnWriteArrayList<>();
        this.messages = new CopyOnWriteArrayList<>();
        this.usersBan = new CopyOnWriteArrayList<>();
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatCode() {
        return chatCode;
    }

    public void setChatCode(String chatCode) {
        this.chatCode = chatCode;
    }

    public List<SseEmitterIdentifier> getEmitters() {
        return emitters;
    }

    public void setEmitters(List<SseEmitterIdentifier> emitters) {
        this.emitters = emitters;
    }

    public List<MessageConfiguration> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageConfiguration> messages) {
        this.messages = messages;
    }
}
