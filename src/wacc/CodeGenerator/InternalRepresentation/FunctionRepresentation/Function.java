package wacc.CodeGenerator.InternalRepresentation.FunctionRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.ScopeOffsetGenerator;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.LabelCommand.LabelCommand;
import wacc.CodeGenerator.Commands.LabelCommand.SpecialLabelCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PopCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PushCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.Register;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.FunctionParameterRepresentation.FunctionParameter;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.CodeGenerator.InternalRepresentation.StatementRepresentation.Statement;
import wacc.SemanticErrorDetector.WaccTypes.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Function {

    private Type functionType;
    private String identification;
    private List<FunctionParameter> parameterList;
    private Statement functionStatement;

    public Function(Type functionType,
                    String identification,
                    List<FunctionParameter> parameterList,
                    Statement functionStatement) {

        this.functionType = functionType;
        this.identification = identification;
        this.parameterList = parameterList;
        this.functionStatement = functionStatement;
    }

    public List<Command> generateCommandsForFunction() {

        HardwareManager hardwareManager = HardwareManager.getInstance();

        List<Command> commands = new ArrayList<>();

        // Initialise argument identifiers
        for (FunctionParameter aParameterList : parameterList) {
            aParameterList.generateCommandsForFunctionParameter();
        }

        // Moving to next scope and retrieving totalOffSet for new scope
        hardwareManager.moveToNextStackScope();

        int totalOffset = hardwareManager.getTotalOffsetForCurrentScope();

        // Create label for function
        LabelCommand labelFunction = new LabelCommand("f_" + identification);
        commands.add(labelFunction);

        // Push the link register
        List<Register> registersPush
            = new ArrayList<>(Collections.singletonList(SpecialRegister.LR));
        Command pushLr = new PushCommand(ConditionCode.AL, registersPush);
        commands.add(pushLr);

        // Move the stack pointer depending on total offSet
        // (variable initialization) if not equal to zero
        List<Command> stackSubtraction1 =
            ScopeOffsetGenerator.initializeScopeOffset(totalOffset);
        commands.addAll(stackSubtraction1);

        // Generate command for functionStatement
        List<Command> doStatementCommand =
            functionStatement.generateCommandsForStatement();
        commands.addAll(doStatementCommand);

        // Pop the 2 to pc
        List<Register> registersPop
            = new ArrayList<>(Collections.singletonList(SpecialRegister.PC));
        Command popPc2 = new PopCommand(ConditionCode.AL, registersPop);
        commands.add(popPc2);

        // .ltorg special label
        Command ltorgCommand = new SpecialLabelCommand(
            SpecialLabelCommand.SpecialLabel.LTORG);
        commands.add(ltorgCommand);

        // Leave the function statement scope
        hardwareManager.moveToNextStackScope();

        return commands;
    }

    /*
      Representation for debugging
    */

    public String generateRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        StringBuilder functionParametersRepresentation = new StringBuilder();

        for (FunctionParameter functionParameter : parameterList) {
            functionParametersRepresentation.append(
                functionParameter.generateRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE));
            functionParametersRepresentation.append("\n");
        }

        return RepresentationFormatter.generateRepresentation(
            depth,
            "Function",
            new String[] {
                "Type",
                "Identifier",
                "Function Parameters",
                "Function Statement"},
            new String[] {
                indentDepth + functionType.toString(),
                indentDepth + identification,
                functionParametersRepresentation.toString(),
                functionStatement.generateStatementRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)
            });
    }

}
