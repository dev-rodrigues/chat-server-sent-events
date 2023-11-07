package br.ufrj.coppetec.servicechat.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

public class SseEmitterIdentifier {
    private LocalDateTime lastKeepAlive;
    private SseEmitter sseEmitter;
    private String id;

    public SseEmitterIdentifier(LocalDateTime lastKeepAlive, SseEmitter sseEmitter, String id) {
        this.lastKeepAlive = lastKeepAlive;
        this.sseEmitter = sseEmitter;
        this.id = id;
    }

    public SseEmitter getSseEmitter() {
        return sseEmitter;
    }

    public void setSseEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    public String getId() {
        return id;
    }

    public void setId(String uuid) {
        this.id = uuid;
    }

    public LocalDateTime getLastKeepAlive() {
        return lastKeepAlive;
    }

    public void setLastKeepAlive(LocalDateTime lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
    }
}
