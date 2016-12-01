package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.BranchLabelGenerator;
import wacc.CodeGenerator.CodeGenerationTools.ScopeOffsetGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ComparisonCommand.CmpCommand;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.LabelCommand.LabelCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class WhileStatement implements Statement {

    private Expression whileCondition;
    private Statement doStatement;

    public WhileStatement(Expression whileCondition,
                          Statement doStatement) {

        this.whileCondition = whileCondition;
        this.doStatement = doStatement;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        HardwareManager hardwareManager = HardwareManager.getInstance();

        List<Command> commands = new ArrayList<>();

        String firstLabel = BranchLabelGenerator.getLabelAndIncrementCounter();
        String secondLabel = BranchLabelGenerator.getLabelAndIncrementCounter();

        // Moving to next scope and retrieving totalOffSet for new scope
        hardwareManager.moveToNextStackScope();
        int totalOffset = hardwareManager.getTotalOffsetForCurrentScope();

        // Jump to test if whileCondition is true
        BCommand whileConditionCommand =
            new BCommand(ConditionCode.AL, firstLabel);
        commands.add(whileConditionCommand);

        // Create label for loop
        LabelCommand labelLoop = new LabelCommand(secondLabel);
        commands.add(labelLoop);

        // Move the stack pointer depending on total offSet
        // (variable initialization) if not equal to zero
        List<Command> stackSubtraction1 =
            ScopeOffsetGenerator.initializeScopeOffset(totalOffset);
        commands.addAll(stackSubtraction1);

        // Generate command for doStatement
        List<Command> doStatementCommand =
            doStatement.generateCommandsForStatement();
        commands.addAll(doStatementCommand);

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition =
            ScopeOffsetGenerator.returnScopeOffset(totalOffset);
        commands.addAll(stackAddition);

        // Create label for test whileCondition and rest of the code
        LabelCommand labelRestOfProgram = new LabelCommand(firstLabel);
        commands.add(labelRestOfProgram);

        // Generate command for doStatement
        List<Command> whileStatementCommand =
            whileCondition.generateCommandsForExpression();
        commands.addAll(whileStatementCommand);

        // Evaluate WhileCondition
        NormalRegister whileConditionReturnRegister =
            hardwareManager.getStorageRegister();
        CmpCommand cmpIfCondition =
            new CmpCommand(ConditionCode.AL,
                whileConditionReturnRegister, "1");
        commands.add(cmpIfCondition);
        hardwareManager.freeRegister(whileConditionReturnRegister);

        // Test if whileCondition is false
        BCommand jumpWhile = new BCommand(ConditionCode.EQ, secondLabel);
        commands.add(jumpWhile);

        // Leave the while statement scope
        hardwareManager.moveToNextStackScope();

        return commands;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateStatementRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            "WhileStatement",
            new String[] {
                "Condition",
                "Do Statement"},
            new String[] {
                whileCondition.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE),
                doStatement.generateStatementRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
