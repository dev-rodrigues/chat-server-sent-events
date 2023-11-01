package br.ufrj.coppetec.servicechat.domain;

public class ChannelInfo {
    private String chatName;
    private String chatCode;
    private Integer totalConnected;

    public ChannelInfo() {
    }

    public ChannelInfo(String chatName, String chatCode, Integer totalConnected) {
        this.chatName = chatName;
        this.chatCode = chatCode;
        this.totalConnected = totalConnected;
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

    public Integer getTotalConnected() {
        return totalConnected;
    }

    public void setTotalConnected(Integer totalConnected) {
        this.totalConnected = totalConnected;
    }
}
