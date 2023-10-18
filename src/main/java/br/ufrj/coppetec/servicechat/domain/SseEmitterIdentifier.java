package br.ufrj.coppetec.servicechat.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public class SseEmitterIdentifier {
    private SseEmitter sseEmitter;
    private UUID uuid;

    public SseEmitterIdentifier(SseEmitter sseEmitter, UUID uuid) {
        this.sseEmitter = sseEmitter;
        this.uuid = uuid;
    }

    public SseEmitter getSseEmitter() {
        return sseEmitter;
    }

    public void setSseEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
