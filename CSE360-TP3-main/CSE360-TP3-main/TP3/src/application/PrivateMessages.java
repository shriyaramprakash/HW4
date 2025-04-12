package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class PrivateMessages {
    private final Map<String, List<Message>> privateMessages = new HashMap<>();

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        TextField reviewerField = new TextField();
        TextArea messageArea = new TextArea();
        ListView<String> messageListView = new ListView<>();
        Button sendMessageButton = new Button("Send Message");

        sendMessageButton.setOnAction(a -> sendMessage(reviewerField.getText(), messageArea.getText(), messageListView));

        layout.getChildren().addAll(new Label("Reviewer Username"), reviewerField,
                new Label("Message"), messageArea, sendMessageButton,
                new Label("Private Messages"), messageListView);

        primaryStage.setScene(new Scene(layout, 600, 400));
        primaryStage.setTitle("Private Messages");
    }

    public void sendMessage(String reviewer, String message, ListView<String> messageListView) {
        if (reviewer == null || reviewer.isEmpty() || message == null || message.isEmpty()) {
            return;
        }
        
        privateMessages.putIfAbsent(reviewer, new ArrayList<>());
        privateMessages.get(reviewer).add(new Message(message));
        
        updateMessages(reviewer, messageListView);
    }

    public void updateMessages(String reviewer, ListView<String> messageListView) {
        List<String> messages = new ArrayList<>();
        if (privateMessages.containsKey(reviewer)) {
            for (Message msg : privateMessages.get(reviewer)) {
                messages.add(msg.getText() + " (Sent on: " + msg.getTimestamp() + ")");
            }
        }
        messageListView.getItems().setAll(messages);
    }

    static class Message {
        private final String text;
        private final Date timestamp;

        public Message(String text) {
            this.text = text;
            this.timestamp = new Date();
        }

        public String getText() {
            return text;
        }

        public Date getTimestamp() {
            return timestamp;
        }
    }
}
