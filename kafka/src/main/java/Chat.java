import org.apache.kafka.clients.producer.ProducerRecord;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;

public class Chat extends JFrame {
    private JTextArea chatViev;
    private JPanel mainPanel;
    private JButton sendButton;
    private JTextField message;
    private JButton loginButton;
    private JTextField loginField;
    private JList users;
    private JTextField TopicField;

    private JScrollPane chatScroll;

    private MessageConsumer messageConsumer;

    private String topic;
    private String id;

    public Chat() {

        Chat.this.sendButton.setEnabled(false);


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setVisible(true);
        this.setTitle(id);
        this.setLocationRelativeTo(null);
        this.pack();


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                MessageProducer.sendMessage(new ProducerRecord<>(topic, LocalDateTime.now().format(formatter) + " " + id + ": " + message.getText()));
                message.setText("");
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topic = TopicField.getText();
                id = loginField.getText();
                if(topic!=null && id!=null) {
                    Chat.this.setTitle(id);
                    Chat.this.loginButton.setEnabled(false);
                    Chat.this.sendButton.setEnabled(true);
                    messageConsumer = new MessageConsumer(topic, id);
                    Executors.newSingleThreadExecutor().submit(() -> {
                        while (true) {
                            messageConsumer.kafkaConsumer.poll(Duration.of(1, ChronoUnit.SECONDS)).forEach((message) -> {
                                chatViev.append(message.value() + System.lineSeparator());
                            });
                        }
                    });
                }

            }
        });
    }
}
