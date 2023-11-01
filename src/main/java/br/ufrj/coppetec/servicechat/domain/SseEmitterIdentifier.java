package br.ufrj.coppetec.servicechat.domain;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseEmitterIdentifier {
    private SseEmitter sseEmitter;
    private String uuid;

    public SseEmitterIdentifier(SseEmitter sseEmitter, String uuid) {
        this.sseEmitter = sseEmitter;
        this.uuid = uuid;
    }

    public SseEmitter getSseEmitter() {
        return sseEmitter;
    }

    public void setSseEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    public String getId() {
        return uuid;
    }

    public void setId(String uuid) {
        this.uuid = uuid;
    }
}
