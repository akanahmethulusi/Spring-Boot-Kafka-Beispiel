package at.codefabrik.kafka.config;

import at.codefabrik.kafka.dto.KMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Configuration
public class KafkaConfiguration {

    @Value("${codefabrik.kafka.address}")
    private String kafkaAddress;

    @Value("${codefabrik.kafka.group.id}")
    private String groupId;

    /* Dies dient nur dazu, Client zu werden, Producer zu werden und Daten für den Producer bereitzustellen. */
    @Bean
    public KafkaTemplate <String, KMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory producerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        //config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS);
        config.put("spring.json.add.type.headers", false);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /*
     * Container, der die ConsumerFactory verwendet
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /*
     * Wir haben eine ConsumerFactory, die entsprechend einen Container
     * (ConcurrentKafkaListenerContainerFactory) verwendet.
     */
    @Bean
    public ConsumerFactory<String, KMessage> consumerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, KMessage.class);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config);
    }
}
