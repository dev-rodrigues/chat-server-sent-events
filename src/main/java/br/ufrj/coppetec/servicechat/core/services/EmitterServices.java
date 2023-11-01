package br.ufrj.coppetec.servicechat.core.services;

import br.ufrj.coppetec.servicechat.domain.Channel;
import br.ufrj.coppetec.servicechat.domain.ChannelInfo;
import br.ufrj.coppetec.servicechat.domain.MessageConfiguration;
import br.ufrj.coppetec.servicechat.domain.SseEmitterIdentifier;
import br.ufrj.coppetec.servicechat.domain.exceptions.InfraStructureException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmitterServices {

    private final List<Channel> channels = new CopyOnWriteArrayList<>();
    private final Logger logger = getLogger(this.getClass());

    public Channel createChannel(Channel body) {
        var channel = new Channel(
                body.getChatName(),
                UUID.randomUUID().toString()
        );

        this.channels.add(channel);
        return channel;
    }

    public ChannelInfo getChannelInfo(String channelId) {
        var channel = getChannel(channelId);

        return new ChannelInfo(
                channel.getChatName(),
                channel.getChatCode(),
                channel.getEmitters().size()
        );
    }

    public void connectChannel(SseEmitter emitter, String channelId, String userId) {
        var channel = getChannel(channelId);

        // Verifica se o usuário já está conectado
        Optional<SseEmitterIdentifier> existingEmitter = channel
                .getEmitters()
                .stream()
                .filter(it -> it.getId().equals(userId))
                .findFirst();

        // Se estiver, remove o usuário da lista de emitters
        existingEmitter.ifPresent(sseEmitterIdentifier -> {
            logger.info("Removendo usuário " + userId + " do canal " + channelId);
            channel.getEmitters().remove(sseEmitterIdentifier);
        });


        // Conecta o usuário ao canal
        channel.getEmitters().add(new SseEmitterIdentifier(emitter, userId));

        emitter.onCompletion(() -> channel.getEmitters().remove(emitter));
        emitter.onTimeout(() -> channel.getEmitters().remove(emitter));
    }

    public Channel getChannel(String channelId) {
        return channels.stream()
                .filter(c -> c.getChatCode().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new InfraStructureException("Canal não encontrado"));
    }

    public void sendMessage(MessageConfiguration messageConfiguration, String channelId) {
        var channel = getChannel(channelId);
        channel.getMessages().add(messageConfiguration);
    }

    public List<MessageConfiguration> getMessages(String channelId) {
        var channel = getChannel(channelId);
        return channel.getMessages();
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void closeChannel(String channelId) {
        var channel = getChannel(channelId);
        channels.remove(channel);
    }

    public void alreadyConnected(String userId) {
        getChannels()
                .stream()
                .findFirst()
                .flatMap(it -> it
                        .getEmitters()
                        .stream()
                        .filter(emitter -> emitter.getId().equals(userId))
                        .findFirst())
                .ifPresent(sseEmitterIdentifier -> {
                    throw new InfraStructureException("Usuário já conectado");
                });
    }
}
