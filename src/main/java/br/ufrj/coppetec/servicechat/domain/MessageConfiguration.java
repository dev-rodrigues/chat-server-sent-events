package br.ufrj.coppetec.servicechat.domain;

public class MessageConfiguration {
    private Message message;
    private String createdAt;
    private Boolean delivered;

    public MessageConfiguration(Message message, String createdAt, Boolean delivered) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }
}
