import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;


import javax.swing.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class Demo {


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
    public static void deleteConsumerGroup(String consumerGroupId) throws InterruptedException, ExecutionException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        AdminClient adminClient = AdminClient.create(properties);

       adminClient.deleteConsumerGroups(Arrays.asList(consumerGroupId));
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
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()
//                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false,
//                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                        )
        );
        kafkaConsumer.subscribe(Collections.singletonList(topic));




    }

}
