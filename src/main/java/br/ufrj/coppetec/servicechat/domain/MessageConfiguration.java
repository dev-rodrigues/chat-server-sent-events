package br.ufrj.coppetec.servicechat.domain;

import java.time.LocalDateTime;

public class MessageConfiguration {
    private Message message;
    private LocalDateTime createdAt;
    private Boolean delivered;

    public MessageConfiguration(Message message, LocalDateTime createdAt, Boolean delivered) {
        this.message = message;
        this.createdAt = createdAt;
        this.delivered = delivered;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }
}
