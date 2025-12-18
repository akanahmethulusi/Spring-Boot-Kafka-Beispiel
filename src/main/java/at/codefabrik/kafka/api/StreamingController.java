package at.codefabrik.kafka.api;

import at.codefabrik.kafka.dto.KMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/kafkaMessage")
@RequiredArgsConstructor
public class StreamingController {
    @Value("${codefabrik.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, KMessage> kafkaTemplate;

    @PostMapping
    public void sendMessage(@RequestBody KMessage message) {
        /* Topic-Key-ID*/
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
    }

}
