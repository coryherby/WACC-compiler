package wacc.CodeGenerator.InternalRepresentation.ProgramRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessagesGenerator;
import wacc.CodeGenerator.CodeGenerationTools.ScopeOffsetGenerator;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.LabelCommand.LabelCommand;
import wacc.CodeGenerator.Commands.LabelCommand.SpecialLabelCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PopCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PushCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.Register;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.FunctionRepresentation.Function;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.CodeGenerator.InternalRepresentation.StatementRepresentation.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program {

    private List<Function> programFunctions;
    private Statement mainStatement;

    public Program(List<Function> programFunctions,
                   Statement mainStatement) {

        this.programFunctions = programFunctions;
        this.mainStatement = mainStatement;
    }

    public List<Command> generateCommandsForProgram() {
        HardwareManager hardwareManager = HardwareManager.getInstance();

        List<Command> commandsWithoutData = new ArrayList<>();

        // .text special label
        Command textCommand = new SpecialLabelCommand(
            SpecialLabelCommand.SpecialLabel.TEXT);
        commandsWithoutData.add(textCommand);

        // .global main special label
        Command globalMainCommand = new SpecialLabelCommand(
            SpecialLabelCommand.SpecialLabel.GLOBAL_MAIN);
        commandsWithoutData.add(globalMainCommand);

        for (Function f : programFunctions) {
            hardwareManager.moveToNextStackScopeEnteringFunction();
            commandsWithoutData.addAll(f.generateCommandsForFunction());
        }

        hardwareManager.moveToNextStackScope();

        int totalOffset = hardwareManager.getTotalOffsetForCurrentScope();

        // Label for main
        Command main = new LabelCommand("main");
        commandsWithoutData.add(main);

        // Push the link register
        List<Register> registersPush
            = new ArrayList<>(Collections.singletonList(SpecialRegister.LR));
        Command pushLr = new PushCommand(ConditionCode.AL, registersPush);
        commandsWithoutData.add(pushLr);

        // Move the stack pointer depending on total offSet
        // (variable initialization) if not equal to zero
        List<Command> stackSubtraction =
            ScopeOffsetGenerator.initializeScopeOffset(totalOffset);
        commandsWithoutData.addAll(stackSubtraction);

        // Add commands from the main function
        commandsWithoutData.addAll(
            mainStatement.generateCommandsForStatement());

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition =
            ScopeOffsetGenerator.returnScopeOffset(totalOffset);
        commandsWithoutData.addAll(stackAddition);

        // Load exit code 0 in register 0
        Command ldrInR0 = new LdrCommand(ConditionCode.AL, DataSize.W,
            HardwareManager.getInstance().getRegister0(), "0");
        commandsWithoutData.add(ldrInR0);

        // Pop the pc
        List<Register> registersPop
            = new ArrayList<>(Collections.singletonList(SpecialRegister.PC));
        Command popPc = new PopCommand(ConditionCode.AL, registersPop);
        commandsWithoutData.add(popPc);

        // .ltorg special label
        Command ltorgCommand = new SpecialLabelCommand(
            SpecialLabelCommand.SpecialLabel.LTORG);
        commandsWithoutData.add(ltorgCommand);

        // Add all the predefinedFunctions to the list
        List<Command> predefinedFunctions
            = PredefinedFunctionGenerator.getInstance()
            .generatePredefinedFunctions();
        commandsWithoutData.addAll(predefinedFunctions);

        // Create a final list where we put first the data and then the
        // commands generated earlier
        List<Command> commandsWithData = new ArrayList<>();

        // Add the data (messages) first here
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();
        commandsWithData.addAll(
            predefinedMessagesGenerator.generatePredefinedMessages());

        // Add all the functions generated earlier in a second time
        commandsWithData.addAll(commandsWithoutData);

        return commandsWithData;
    }

    /*
      Representation for debugging
    */

    public String generateRepresentation() {

        StringBuilder programFunctionRepresentation = new StringBuilder();

        for (Function function : programFunctions) {
            programFunctionRepresentation.append(
                function.generateRepresentation(
                    RepresentationFormatter.INDENT_TWICE));
            programFunctionRepresentation.append("\n");
        }

        return RepresentationFormatter.generateRepresentation(
            "",
            "Program",
            new String[] {
                "Program functions",
                "Main Statement"},
            new String[] {
                programFunctionRepresentation.toString(),
                mainStatement.generateStatementRepresentation(
                    RepresentationFormatter.INDENT_TWICE)}
        );
    }
}
