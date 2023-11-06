package br.ufrj.coppetec.servicechat.core.services;

import br.ufrj.coppetec.servicechat.domain.Channel;
import br.ufrj.coppetec.servicechat.domain.ChannelInfo;
import br.ufrj.coppetec.servicechat.domain.MessageConfiguration;
import br.ufrj.coppetec.servicechat.domain.SseEmitterIdentifier;
import br.ufrj.coppetec.servicechat.domain.exceptions.InfraStructureException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmitterServices {

    private final List<Channel> channels = new CopyOnWriteArrayList<>();
    private final List<String> bannedUsers = new CopyOnWriteArrayList<>();
    private final Logger logger = getLogger(this.getClass());

    public Channel createChannel(Channel body) {

        if (alreadyAChatWithCode(body.getChatCode())) {
            throw new InfraStructureException("Já existe um canal com esse código");
        }

        var channel = new Channel(body.getChatName(), body.getChatCode());
        this.channels.add(channel);
        return channel;
    }

    private Boolean alreadyAChatWithCode(String chatCode) {
        return channels
                .stream()
                .anyMatch(it -> it.getChatCode().equals(chatCode));
    }

    public ChannelInfo getChannelInfo(String channelId) {
        var channel = getChannel(channelId);

        return new ChannelInfo(channel.getChatName(), channel.getChatCode(), channel.getEmitters().size());
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

        if (!channel.getMessages().isEmpty()) {
            var messages = channel.getMessages();

            for (MessageConfiguration message : messages) {
                SseEmitterIdentifier emitterIdentifier = channel
                        .getEmitters()
                        .stream()
                        .filter(it -> it.getId().equals(userId))
                        .findFirst()
                        .orElseThrow(() -> new InfraStructureException("Usuário não encontrado"));

                try {
                    emitterIdentifier.getSseEmitter().send(message);
                } catch (IOException ex) {
                    logger.error("Erro ao enviar mensagem para o usuário " + userId + " do canal " + channelId);
                    emitterIdentifier.getSseEmitter().complete();
                    channel.getEmitters().remove(emitter);
                }
            }
        }

        emitter.onCompletion(() -> channel.getEmitters().remove(emitter));
        emitter.onTimeout(() -> channel.getEmitters().remove(emitter));
    }

    public void unsubscribe(String channelId, String userId) {
        var channel = getChannel(channelId);

        channel.getEmitters().stream().filter(it -> it.getId().equals(userId)).findFirst().ifPresent(sseEmitterIdentifier -> {
            sseEmitterIdentifier.getSseEmitter().complete();
            channel.getEmitters().remove(sseEmitterIdentifier);
        });
    }

    public Channel getChannel(String channelId) {
        return channels
                .stream()
                .filter(c -> c.getChatCode().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new InfraStructureException("Canal não encontrado"));
    }

    public void sendMessage(MessageConfiguration messageConfiguration, String channelId) {
        if (bannedUsers.contains(messageConfiguration.getMessage().getUserCode())) {
            throw new InfraStructureException("Usuário banido");
        }

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
                .flatMap(it ->
                        it.getEmitters()
                                .stream()
                                .filter(emitter -> emitter.getId().equals(userId))
                                .findFirst())
                .ifPresent(sseEmitterIdentifier -> {
            throw new InfraStructureException("Usuário já conectado");
        });
    }

    public void banUser(String userId) {
        bannedUsers.add(userId);
    }

//    public boolean banUser(String channelId, String userId) {
//        var channel = getChannel(channelId);
//        return channel.getUsersBan().contains(userId);
//    }
}
