package br.ufrj.coppetec.servicechat.core.apis;

import br.ufrj.coppetec.servicechat.core.services.EmitterServices;
import br.ufrj.coppetec.servicechat.domain.Message;
import br.ufrj.coppetec.servicechat.domain.MessageConfiguration;
import br.ufrj.coppetec.servicechat.domain.SseEmitterIdentifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@CrossOrigin(maxAge = 3600)
public class ChatApi {

    private final EmitterServices service;

    public ChatApi(EmitterServices emitterServices) {
        this.service = emitterServices;
    }

    @GetMapping(path = "/sse", produces = TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        var emitter = new SseEmitter(Long.MAX_VALUE);
        service.addEmitter(emitter);
        return emitter;
    }

    @GetMapping(path = "/users/connected")
    public ResponseEntity<List<SseEmitterIdentifier>> getUsers() {
        return ResponseEntity.ok().body(service.getUsers());
    }

    @GetMapping(path = "/messages")
    public ResponseEntity<List<MessageConfiguration>> getMessages() {
        return ResponseEntity.ok(service.getMessages());
    }

    @DeleteMapping(path = "/messages/{messageId}")
    public ResponseEntity<Void> removeMessage(@PathVariable String messageId) {
        service.removeMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/message")
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) {
        service.receiveMessage(
                new MessageConfiguration(message, LocalDateTime.now(), false)
        );

        return ResponseEntity.ok().build();
    }
}
