package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StudentMessagingPage extends Application {

    private TextArea chatLog = new TextArea();
    private TextField messageField = new TextField();

    @Override
    public void start(Stage primaryStage) {
        chatLog.setEditable(false);
        chatLog.setText("Chat with Reviewer:\n");

        messageField.setPromptText("Type your message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                chatLog.appendText("Student: " + message + "\n");
                messageField.clear();
            }
        });

        VBox layout = new VBox(10, chatLog, messageField, sendButton);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Messaging");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
