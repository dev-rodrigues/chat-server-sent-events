package br.ufrj.coppetec.servicechat.core.listeners;

import br.ufrj.coppetec.servicechat.core.services.EmitterServices;
import br.ufrj.coppetec.servicechat.domain.Channel;
import br.ufrj.coppetec.servicechat.domain.KeepAlive;
import br.ufrj.coppetec.servicechat.domain.MessageConfiguration;
import br.ufrj.coppetec.servicechat.domain.SseEmitterIdentifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SentMessagesListeners {

    private final EmitterServices emitterServices;

    public SentMessagesListeners(EmitterServices emitterServices) {
        this.emitterServices = emitterServices;
    }

    @Scheduled(fixedRate = 1000)
    public void sendEvents() {

        var channels = emitterServices.getChannels();

        if (!channels.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(channels.size());

            for (Channel channel : channels) {
                executorService.submit(() -> {
                    List<MessageConfiguration> unreadMessagesCopy = channel
                            .getMessages()
                            .stream()
                            .filter(messageConfig -> !messageConfig.getDelivered())
                            .sorted(Comparator.comparing(MessageConfiguration::getCreatedAt))
                            .toList();

                    for (SseEmitterIdentifier emitter : channel.getEmitters()) {
                        try {
                            for (MessageConfiguration message : unreadMessagesCopy) {
                                emitter.getSseEmitter().send(new KeepAlive());
                                Thread.sleep(2000); // Espera por 2 segundos

                                if (LocalDateTime.now().isBefore(emitter.getLastKeepAlive().plusSeconds(10))) {
                                    emitter.getSseEmitter().send(message);
                                    message.setDelivered(true);
                                } else {
                                    // remove esse emitter da lista de emitters
                                    emitterServices.unsubscribe(channel.getChatCode(), emitter.getId());
                                }
                            }
                        } catch (IOException | InterruptedException e) {
                            emitter.getSseEmitter().complete();
                            channel.getEmitters().remove(emitter);
                        }
                    }
                });
            }

            executorService.shutdown();
        }
    }
}
