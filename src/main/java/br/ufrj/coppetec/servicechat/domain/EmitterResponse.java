package br.ufrj.coppetec.servicechat.domain;

import java.util.UUID;

public class EmitterResponse {
    private UUID uuid;
    private long currentTime;
    private String userMessage;
    private String message;

    public EmitterResponse(UUID uuid, long currentTime, String userMessage, String message) {
        this.uuid = uuid;
        this.currentTime = currentTime;
        this.userMessage = userMessage;
        this.message = message;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
