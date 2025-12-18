package at.codefabrik.kafka.service;


import at.codefabrik.kafka.dto.KMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ListenerService {
    @KafkaListener(
            topics = "${codefabrik.kafka.topic}",
            groupId = "${codefabrik.kafka.group.id}"
    )

    public void listen(@Payload KMessage message){
        log.info("Message received.. MessageID: {} Message: {} Date: {}",
                message.getId(),
                message.getMessage(),
                message.getMessageDate());
    }
}
