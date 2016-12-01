package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class PrintLnStatement implements Statement {

    private static final String PRINT_CHAR = "putchar";

    private Expression expression;

    public PrintLnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        HardwareManager hardwareManager = HardwareManager.getInstance();

        List<Command> commands = new ArrayList<>();

        PrintStatement printStatement = new PrintStatement(expression);

        commands.addAll(printStatement.generateCommandsForStatement());

        // Call PrintLn
        BlCommand callPrintln = new BlCommand(
            ConditionCode.AL, PredefinedFunctionLabels.PRINT_LN.toString());
        commands.add(callPrintln);

        PredefinedFunctionGenerator functionGenerator
            = PredefinedFunctionGenerator.getInstance();

        functionGenerator.addPredefinedFunction(
            PredefinedFunctionLabels.PRINT_LN);


        return commands;
    }

    /*
      Representation for debugging
    */

    public String generateStatementRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            "PrintLnStatement",
            expression.generateExpressionRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
