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

public class IfStatement implements Statement {

    private Expression ifCondition;
    private Statement thenStatement;
    private Statement elseStatement;

    public IfStatement(Expression ifCondition,
                       Statement thenStatement,
                       Statement elseStatement) {

        this.ifCondition = ifCondition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
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

        // Generate commands for ifCondition
        List<Command> ifStatementCommands =
            ifCondition.generateCommandsForExpression();
        commands.addAll(ifStatementCommands);

        // Evaluate ifCondition and free register
        NormalRegister ifConditionReturnRegister =
            hardwareManager.getStorageRegister();
        CmpCommand cmpIfCondition =
            new CmpCommand(ConditionCode.AL, ifConditionReturnRegister, "0");
        commands.add(cmpIfCondition);
        hardwareManager.freeRegister(ifConditionReturnRegister);

        // Test if ifCondition is false
        BCommand jumpThen = new BCommand(ConditionCode.EQ, firstLabel);
        commands.add(jumpThen);

        // Move the stack pointer depending on total offSet
        // (variable initialization) if not equal to zero
        List<Command> stackSubtraction1 =
            ScopeOffsetGenerator.initializeScopeOffset(totalOffset);
        commands.addAll(stackSubtraction1);

        // Generate commands for thenStatement
        List<Command> thenStatementCommands =
            thenStatement.generateCommandsForStatement();
        commands.addAll(thenStatementCommands);

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition1 =
            ScopeOffsetGenerator.returnScopeOffset(totalOffset);
        commands.addAll(stackAddition1);

        // Test if ifCondition is true
        BCommand jumpIf = new BCommand(ConditionCode.AL, secondLabel);
        commands.add(jumpIf);

        // Moving to next scope and retrieving totalOffSet for new scope
        hardwareManager.moveToNextStackScope();
        hardwareManager.moveToNextStackScope();
        totalOffset = hardwareManager.getTotalOffsetForCurrentScope();

        // Create label for else
        LabelCommand labelElse = new LabelCommand(firstLabel);
        commands.add(labelElse);

        // Move the stack pointer depending on total offSet
        // (variable initialization) if not equal to zero
        List<Command> stackSubtraction2 =
            ScopeOffsetGenerator.initializeScopeOffset(totalOffset);
        commands.addAll(stackSubtraction2);

        // Generate commands for ifStatement
        List<Command> elseStatementCommands =
            elseStatement.generateCommandsForStatement();
        commands.addAll(elseStatementCommands);

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition2 =
            ScopeOffsetGenerator.returnScopeOffset(totalOffset);
        commands.addAll(stackAddition2);

        // Create label for rest of the code
        LabelCommand labelRestOfProgram = new LabelCommand(secondLabel);
        commands.add(labelRestOfProgram);

        // Returning to if parent scope
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
            "IfStatement",
            new String[] {
                "Condition" ,
                "ThenStatement",
                "ElseStatement"},
            new String[] {
                ifCondition.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE),
                thenStatement.generateStatementRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE),
                elseStatement.generateStatementRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
