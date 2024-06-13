import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.apache.log4j.Logger;


import javax.swing.*;
import java.io.File;
import java.io.ObjectInputFilter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;




public class Demo {
//    private static final Logger logger = Logger.getLogger(Demo.class);


    public static void main(String[] args) {

        EmbeddedKafkaZKBroker embeddedKafkaZKBroker = new EmbeddedKafkaZKBroker(1)
                .kafkaPorts(9092);
//        embeddedKafkaZKBroker.brokerProperty("metricRecordingLevel", "ERROR");

        embeddedKafkaZKBroker.afterPropertiesSet();
//        String metricRecordingLevel = embeddedKafkaZKBroker.getKafkaServers().get(0).config().metricRecordingLevel();

//        MessageProducer.sendMessage(new ProducerRecord<>("chat","Hello World!"));
//
//        new MessageConsumer("chat","Wiktor");

        SwingUtilities.invokeLater(Chat::new);
        SwingUtilities.invokeLater(Chat::new);

    }
}
class MessageProducer{
    private static KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(
            Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()
            )
    );
    public static void sendMessage(ProducerRecord<String, String> record){
        kafkaProducer.send(record);
    }
}
class MessageConsumer{

    KafkaConsumer<String,String> kafkaConsumer;

    public MessageConsumer(String topic, String id) {
        kafkaConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092",
                        ConsumerConfig.GROUP_ID_CONFIG,id,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                )
        );
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS)).forEach(record -> {
            System.out.println(id+ " "+ record.value());
        });

    }
}
