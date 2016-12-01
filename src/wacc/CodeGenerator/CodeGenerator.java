package wacc.CodeGenerator;

import antlr.WaccParser.ProgramContext;
import antlr.WaccParserBaseVisitor;
import wacc.CodeGenerator.AssemblyFileGenerator.FileCreator;
import wacc.CodeGenerator.AssemblyFileGenerator.FileWriter;
import wacc.CodeGenerator.CodeGenerationOptimizer.CodeOptimizer;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.InternalRepresentation.ProgramFactory;
import wacc.CodeGenerator.InternalRepresentation.ProgramRepresentation.Program;

import java.util.List;


public class CodeGenerator extends WaccParserBaseVisitor<Void> {

    private FileWriter fileWriter;

    public CodeGenerator(String fileName) {
        // Creates the file writer using a fileCreator which opens the file
        fileWriter = new FileWriter(new FileCreator(fileName));
    }

    @Override
    public Void visitProgram(ProgramContext ctx) {

        // Generate the program representation
        Program program = ProgramFactory.buildProgramFromProgramContext(ctx);

        // Generates the command for the program representation
        List<Command> generatedCommands = program.generateCommandsForProgram();

        // Optimise the generated commands
        List<Command> optimizeGeneratedCommands
            = CodeOptimizer.optimiseCommands(generatedCommands);

        // Write the list of commands
        writeToFile(optimizeGeneratedCommands);

        return null;
    }

    private void writeToFile(List<Command> commands){
        fileWriter.writeToFile(commands);
    }

}
