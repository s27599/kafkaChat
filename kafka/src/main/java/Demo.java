import org.springframework.kafka.test.EmbeddedKafkaBroker;

public class Main {
    public static void main(String[] args) {

        EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1);
        embeddedKafkaBroker.afterPropertiesSet();
    }
}

