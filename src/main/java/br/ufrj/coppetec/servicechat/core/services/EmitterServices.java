package br.ufrj.coppetec.servicechat.core.services;

import br.ufrj.coppetec.servicechat.domain.MessageConfiguration;
import br.ufrj.coppetec.servicechat.domain.SseEmitterIdentifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EmitterServices {

    private final List<SseEmitterIdentifier> emitters = new CopyOnWriteArrayList<>();
    private final List<MessageConfiguration> messages = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(new SseEmitterIdentifier(emitter, UUID.randomUUID()));
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    public void receiveMessage(MessageConfiguration message) {
        messages.add(message);
    }

    public List<SseEmitterIdentifier> getUsers() {
        return emitters;
    }

    public List<MessageConfiguration> getMessages() {
        return messages;
    }

    public void removeMessage(String messageId) {
        messages
                .stream()
                .filter(it -> it.getMessage().getUserName().equals(messageId))
                .peek(it -> {
                    it.setDelivered(false);
                })
                .toList();
    }

    @Scheduled(fixedRate = 1000)
    public void sendEvents() {
        List<MessageConfiguration> unreadMessagesCopy = messages
                .stream()
                .filter(messageConfig -> !messageConfig.getDelivered())
                .sorted(Comparator.comparing(MessageConfiguration::getCreatedAt))
                .toList();

        for (SseEmitterIdentifier emitter : emitters) {
            try {
                for (MessageConfiguration message : unreadMessagesCopy) {
                    emitter.getSseEmitter().send(message);
                    message.setDelivered(true);
                }
            } catch (IOException e) {
                emitter.getSseEmitter().complete();
                emitters.remove(emitter);
            }
        }
    }
}
