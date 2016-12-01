package wacc.CodeGenerator.AssemblyFileGenerator;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.LabelCommand.LabelCommand;
import wacc.CodeGenerator.Commands.LabelCommand.SpecialLabelCommand;

public class FileFormatter {

    private static final String TAB = "\t";
    private static final String NEW_LINE = "\n";

    // Formats the command putting tab and end of line where needed
    public static String formatCommand(Command command) {

        StringBuilder commandRepresentation = new StringBuilder();

        if (command instanceof SpecialLabelCommand) {
            SpecialLabelCommand specialLabelCommand
                = (SpecialLabelCommand) command;

            // Add a tab and new line in front of specific labels
            switch (specialLabelCommand.getSpecialLabel()) {
                case WORD:
                case ASCII:
                case LTORG:
                    commandRepresentation.append(TAB);
                    break;
                case DATA:
                case TEXT:
                    commandRepresentation.append(NEW_LINE);
                    break;
            }

            // Add a tab a the beginning od every commands
            commandRepresentation.append(TAB);

            commandRepresentation.append(
                command.generateCommandRepresentation());

            // Add a new line at the end of .data and .text
            switch (specialLabelCommand.getSpecialLabel()) {
                case DATA:
                case TEXT:
                    commandRepresentation.append(NEW_LINE);
                    break;
            }

        } else {

            // Add a tab a the beginning od every commands
            commandRepresentation.append(TAB);

            // Don't indent if label
            if (!(command instanceof LabelCommand)) {
                commandRepresentation.append(TAB);
            }

            commandRepresentation.append(
                command.generateCommandRepresentation());
        }

        return commandRepresentation.toString();
    }
}
