package wacc.CodeGenerator.CodeGenerationTools.PredefinedMessages;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.LabelCommand.LabelCommand;
import wacc.CodeGenerator.Commands.LabelCommand.SpecialLabelCommand;

import java.util.ArrayList;
import java.util.List;

public class PredefinedMessage {

    private String label;
    private int word;
    private String ascii;

    public PredefinedMessage(String label, int word, String ascii) {
        this.label = label;
        this.word = word;
        this.ascii = ascii;
    }

    public List<Command> generateCommandsForMessage() {
        ArrayList<Command> commands = new ArrayList<>();

        // Generate the message label
        LabelCommand messageLabel = new LabelCommand(label);
        commands.add(messageLabel);

        // Generate the .word special label (number of chars)
        SpecialLabelCommand wordLabel = new SpecialLabelCommand(word);
        commands.add(wordLabel);

        // Generate the .ascii special label (message)
        SpecialLabelCommand asciiLabel = new SpecialLabelCommand(ascii);
        commands.add(asciiLabel);

        return commands;
    }
}
