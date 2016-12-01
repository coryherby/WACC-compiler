package wacc.CodeGenerator.CodeGenerationTools;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessages.PredefinedMessage;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.LabelCommand.SpecialLabelCommand;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PredefinedMessagesGenerator {

    private static final String MESSAGE_LABEL = "msg_";

    private static final String EMPTY_CHAR = "\\0";
    private static final String EMPTY_CHAR_REPRESENTATION = "\0";
    private static final String DOUBLE_QUOTE_CHAR = "\"";
    private static final String DOUBLE_QUOTE_REPRESENTATION = "";
    private static final String NEW_LINE_CHAR = "\\n";
    private static final String NEW_LINE_REPRESENTATION = "\n";
    private static final String APOSTROPHE_CHAR = "\'";
    private static final String APOSTROPHE_REPRESENTATION = "";

    private static PredefinedMessagesGenerator predefinedMessagesGenerator
        = new PredefinedMessagesGenerator();

    private ArrayDeque<PredefinedMessage> predefinedMessages;
    private int messageCount;

    private PredefinedMessagesGenerator() {
        this.predefinedMessages = new ArrayDeque<>();
        this.messageCount = 0;
    }

    public static PredefinedMessagesGenerator getInstance() {
        return predefinedMessagesGenerator;
    }

    public String addPredefinedMessage(String ascii) {
        return generateAndAddPredefineMessage(ascii);
    }

    private String generateAndAddPredefineMessage(String ascii) {
        String messageLabel = MESSAGE_LABEL + messageCount;

        // Increment the messageCount
        messageCount++;

        // Calculate message length
        int messageLength = calculateMessageLength(ascii);

        // Create the predefined message based on given information
        PredefinedMessage predefinedMessage
            = new PredefinedMessage(messageLabel, messageLength, ascii);

        // Add the predefined message to the stack of messages
        predefinedMessages.addLast(predefinedMessage);

        // Send back the messageLabel generated
        return messageLabel;
    }

    public List<Command> generatePredefinedMessages() {
        ArrayList<Command> commands = new ArrayList<>();

        if (predefinedMessages.size() == 0) {
            return commands;
        }

        // Add the data label as there are predefined messages
        SpecialLabelCommand dataLabel = new SpecialLabelCommand(
            SpecialLabelCommand.SpecialLabel.DATA);
        commands.add(dataLabel);

        while (predefinedMessages.size() > 0) {
            PredefinedMessage predefinedMessage = predefinedMessages.pop();
            commands.addAll(predefinedMessage.generateCommandsForMessage());
        }

        return commands;
    }

    private int calculateMessageLength(String message) {
        int length = message.length();

        HashMap<String, String> specialCharsMapping = new HashMap<>();

        // Remove the unnecessary chars counted twice
        specialCharsMapping.put(EMPTY_CHAR, EMPTY_CHAR_REPRESENTATION);
        specialCharsMapping.put(DOUBLE_QUOTE_CHAR, DOUBLE_QUOTE_REPRESENTATION);
        specialCharsMapping.put(NEW_LINE_CHAR, NEW_LINE_REPRESENTATION);
        specialCharsMapping.put(APOSTROPHE_CHAR, APOSTROPHE_REPRESENTATION);

        for (String specialChar : specialCharsMapping.keySet()) {
            String specialCharRepresentation
                = specialCharsMapping.get(specialChar);
            length -= countOccurrences(
                message, specialChar, specialCharRepresentation);
        }

        return length;
    }

    private int countOccurrences(
        String message, String pattern, String replacePattern) {

        return message.length()
            - message.replace(pattern, replacePattern).length();
    }
}
