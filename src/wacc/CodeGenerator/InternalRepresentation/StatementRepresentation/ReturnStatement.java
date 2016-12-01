package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.ScopeOffsetGenerator;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PopCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.Register;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReturnStatement implements Statement {

    private Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<Command> generateCommandsForStatement() {
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister normalRegister = hardwareManager.getRegister0();

        List<Command> commands = expression.generateCommandsForExpression();

        NormalRegister storageRegister = hardwareManager.getStorageRegister();

        // Mov result to return register (register 0)
        MovCommand movCommand = new MovCommand(
            ConditionCode.AL, normalRegister, storageRegister);
        commands.add(movCommand);

        int totalOffset = hardwareManager.getTotalOffsetForFunctionScope();

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition =
            ScopeOffsetGenerator.returnScopeOffset(totalOffset);
        commands.addAll(stackAddition);

        // Pop the 1 to pc
        List<Register> registersPop
            = new ArrayList<>(Collections.singletonList(SpecialRegister.PC));
        Command popPc1 = new PopCommand(ConditionCode.AL, registersPop);
        commands.add(popPc1);

        hardwareManager.freeRegister(storageRegister);

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
            "ReturnStatement",
            expression.generateExpressionRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
