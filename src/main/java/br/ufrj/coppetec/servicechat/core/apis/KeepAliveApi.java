package br.ufrj.coppetec.servicechat.core.apis;

import br.ufrj.coppetec.servicechat.core.services.EmitterServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class KeepAliveApi {

    private final EmitterServices service;

    public KeepAliveApi(EmitterServices service) {
        this.service = service;
    }

    @GetMapping("/keep-alive/channel/{channelId}/user/{userId}")
    public ResponseEntity<Void> keepAlive(@PathVariable String channelId, @PathVariable String userId) {
        service.keepAlive(channelId, userId);
        return ResponseEntity.ok().build();
    }

}
