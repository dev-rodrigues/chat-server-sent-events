package br.ufrj.coppetec.servicechat.core.apis;

import br.ufrj.coppetec.servicechat.core.services.EmitterServices;
import br.ufrj.coppetec.servicechat.domain.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@CrossOrigin(maxAge = 3600)
public class ChatApi {

    private final EmitterServices service;

    public ChatApi(EmitterServices emitterServices) {
        this.service = emitterServices;
    }

    @PostMapping(path = "/create/channel")
    @ApiOperation(value = "Cria um novo canal", notes = "Cria um novo canal com base nos dados fornecidos no corpo da solicitação.")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel body) {
        var response = service.createChannel(body);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/channel/{channelId}/close")
    @ApiOperation(
            value = "Fecha um canal",
            notes = "Fecha o canal com o ID especificado.",
            response = Void.class
    )
    public ResponseEntity<Void> closeChannel(@PathVariable String channelId) {
        service.closeChannel(channelId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/channel/{userId}/verify")
    public ResponseEntity<Void> verifyUserConnected(@PathVariable String userId) {
        service.alreadyConnected(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/channels")
    @ApiOperation(
            value = "Obtém a lista de canais",
            notes = "Retorna uma lista de todos os canais disponíveis.",
            response = Channel.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<Channel>> getChannels() {
        var response = service.getChannels();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/channel/{chanelId}/connected")
    @ApiOperation(
            value = "Obtém informações do canal conectado",
            notes = "Retorna informações sobre o canal conectado com o ID especificado.",
            response = ChannelInfo.class
    )
    public ResponseEntity<ChannelInfo> getChannelConnected(@PathVariable String chanelId) {
        var response = service.getChannelInfo(chanelId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/sse/{channelId}/user/{userId}", produces = TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(
            value = "Assine eventos SSE do canal para o usuário",
            notes = "Permite que um usuário assine eventos SSE para o canal especificado.",
            response = SseEmitter.class
    )
    public SseEmitter subscribe(@PathVariable String channelId, @PathVariable String userId) {
        if (isNull(channelId) || channelId.isEmpty() || channelId.equals("undefined") || userId.equals("undefined")) {
            throw new IllegalArgumentException("Channel id is required");
        }

        var emitter = new SseEmitter(Long.MAX_VALUE);
        service.connectChannel(emitter, channelId, userId);
        return emitter;
    }

    @PutMapping(path = "/sse/{channelId}/user/{userId}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable String channelId, @PathVariable String userId) {
        if (isNull(channelId) || channelId.isEmpty() || channelId.equals("undefined") || userId.equals("undefined")) {
            throw new IllegalArgumentException("Channel id is required");
        }

        service.unsubscribe(channelId, userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/message/{channelId}/send")
    @ApiOperation(
            value = "Envia uma mensagem para um canal",
            notes = "Envia uma mensagem para o canal especificado com base nos dados fornecidos no corpo da solicitação.",
            response = Void.class
    )
    public ResponseEntity<Void> sendMessage(@RequestBody Message message, @PathVariable String channelId) {
        service.sendMessage(
                new MessageConfiguration(message, LocalDateTime.now().format(ISO_LOCAL_DATE_TIME), false),
                channelId
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/messages/{channelId}")
    @ApiOperation(
            value = "Obtém mensagens de um canal específico",
            notes = "Retorna uma lista de mensagens do canal com o ID especificado.",
            response = MessageConfiguration.class,
            responseContainer = "List"
    )
    public ResponseEntity<List<MessageConfiguration>> getMessages(@PathVariable String channelId) {
        return ResponseEntity.ok(service.getMessages(channelId));
    }

    @PostMapping(path = "/user/{userId}/ban")
    public ResponseEntity<Void> banUser(@PathVariable String userId) {
        service.banUser(userId);
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/user/bans")
    public ResponseEntity<List<String>> getUserBans() {
        return ResponseEntity.ok(service.getBannedUsers());
    }
}
