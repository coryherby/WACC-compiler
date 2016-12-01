package wacc.CodeGenerator.AssemblyFileGenerator;

import wacc.CodeGenerator.Commands.Command;

import java.io.PrintWriter;
import java.util.List;

public class FileWriter {

    private final PrintWriter file;

    public FileWriter(FileCreator fileCreator) {
        this.file = fileCreator.getFile();
    }

    public void writeToFile(List<Command> commands) {

        if (commands == null) {
            // No commands so exit and close file
            file.close();
            return;
        }

        // Print all commands with correct indentation
        for (Command command : commands) {
            String commandRepresentation = FileFormatter.formatCommand(command);
            file.println(commandRepresentation);
        }

        file.close();
    }

}
