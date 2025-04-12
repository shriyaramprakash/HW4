package application;

import java.util.*;

public class ReviewReplyTracker {

    public static class MessageThread {
        private final int reviewId;
        private final String studentName;
        private final String message;
        private String reply;

        public MessageThread(int reviewId, String studentName, String message) {
            this.reviewId = reviewId;
            this.studentName = studentName;
            this.message = message;
            this.reply = null;
        }

        public void addReply(String replyContent) {
            this.reply = replyContent;
        }

        public boolean isReplied() {
            return reply != null;
        }

        public String getReply() {
            return reply;
        }

        public int getReviewId() {
            return reviewId;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getMessage() {
            return message;
        }
    }

    private final List<MessageThread> messageThreads = new ArrayList<>();

    // Student sends message to reviewer
    public void sendMessage(int reviewId, String studentName, String message) {
        messageThreads.add(new MessageThread(reviewId, studentName, message));
    }

    // Reviewer replies to a student's message
    public boolean replyToMessage(int reviewId, String studentName, String replyContent) {
        for (MessageThread thread : messageThreads) {
            if (thread.getReviewId() == reviewId && thread.getStudentName().equals(studentName)) {
                thread.addReply(replyContent);
                return true;
            }
        }
        return false;
    }

    // Check if a reply exists for the student's message
    public boolean hasReply(int reviewId, String studentName) {
        for (MessageThread thread : messageThreads) {
            if (thread.getReviewId() == reviewId && thread.getStudentName().equals(studentName)) {
                return thread.isReplied();
            }
        }
        return false;
    }

    // Optionally get full thread
    public List<MessageThread> getMessagesForStudent(String studentName) {
        List<MessageThread> result = new ArrayList<>();
        for (MessageThread thread : messageThreads) {
            if (thread.getStudentName().equals(studentName)) {
                result.add(thread);
            }
        }
        return result;
    }
}
