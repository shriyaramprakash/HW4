package application;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TP3_MessagesT {

    private PrivateMessagesLogic logic;

    @Before
    public void setUp() {
        logic = new PrivateMessagesLogic();
    }

    @Test
    public void testSendMessageStoresCorrectly() {
        logic.sendMessage("alice", "Hello Alice!");
        List<String> messages = logic.getFormattedMessages("alice");

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Hello Alice!"));
        assertTrue(messages.get(0).contains("Sent on:"));
    }

    @Test
    public void testSendMessageWithEmptyReviewer() {
        logic.sendMessage("", "Should not store this");
        List<String> messages = logic.getFormattedMessages("");

        assertEquals(0, messages.size());
    }

    @Test
    public void testSendMessageWithEmptyMessage() {
        logic.sendMessage("bob", "");
        List<String> messages = logic.getFormattedMessages("bob");

        assertEquals(0, messages.size());
    }

    @Test
    public void testGetMessagesWhenNoneExist() {
        List<String> messages = logic.getFormattedMessages("ghost");

        assertTrue(messages.isEmpty());
    }

    @Test
    public void testMultipleMessages() {
        logic.sendMessage("charlie", "First");
        logic.sendMessage("charlie", "Second");
        List<String> messages = logic.getFormattedMessages("charlie");

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("First"));
        assertTrue(messages.get(1).contains("Second"));
    }
}
