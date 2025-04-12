package application;

import java.util.*;

public class PrivateMessagesLogic {
    private final Map<String, List<Message>> privateMessages = new HashMap<>();

    public void sendMessage(String reviewer, String message) {
        if (reviewer == null || reviewer.isEmpty() || message == null || message.isEmpty()) {
            return;
        }

        privateMessages.putIfAbsent(reviewer, new ArrayList<>());
        privateMessages.get(reviewer).add(new Message(message));
    }

    public List<String> getFormattedMessages(String reviewer) {
        List<String> formattedMessages = new ArrayList<>();
        if (privateMessages.containsKey(reviewer)) {
            for (Message msg : privateMessages.get(reviewer)) {
                formattedMessages.add(msg.getText() + " (Sent on: " + msg.getTimestamp() + ")");
            }
        }
        return formattedMessages;
    }

    public static class Message {
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
