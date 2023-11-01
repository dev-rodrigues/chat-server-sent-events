package br.ufrj.coppetec.servicechat.core.listeners;

import br.ufrj.coppetec.servicechat.core.services.EmitterServices;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SentMessagesListeners {

    private final EmitterServices emitterServices;

    public SentMessagesListeners(EmitterServices emitterServices) {
        this.emitterServices = emitterServices;
    }

    @Scheduled(fixedRate = 1000)
    public void sendEvents() {
//        List<MessageConfiguration> unreadMessagesCopy = emitterServices.getMessages()
//                .stream()
//                .filter(messageConfig -> !messageConfig.getDelivered())
//                .sorted(Comparator.comparing(MessageConfiguration::getCreatedAt))
//                .toList();

//        for (SseEmitterIdentifier emitter : emitterServices.getUsers()) {
//            try {
//                for (MessageConfiguration message : unreadMessagesCopy) {
//                    emitter.getSseEmitter().send(message);
//                    message.setDelivered(true);
//                }
//            } catch (IOException e) {
//                emitter.getSseEmitter().complete();
//                emitterServices.getUsers().remove(emitter);
//            }
//        }
    }
}
