package wacc.CodeGenerator.InternalRepresentation.AssignRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.HardwareFunctions.HardwareFunctions;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.StrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.BoolLiteral;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.CharLiteral;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.WaccTable.MainWaccTable;

import java.util.ArrayList;
import java.util.List;

public class NewpairAssignment implements AssignRhs {

    private Expression leftExpression;
    private Expression rightExpression;

    public NewpairAssignment(Expression leftExpression,
                             Expression rightExpression) {

        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {


        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister R0 = hardwareManager.getRegister0();

        // creates all variable needed in commands
        ConditionCode al = ConditionCode.AL;
        DataSize w = DataSize.W;

        ArrayList<Command> commands = new ArrayList<>();

        // load the value 8 in R0
        LdrCommand ldr8InR0 = new LdrCommand(al, w, R0, "8");
        commands.add(ldr8InR0);

        // allocate the first elem of newpair
        BlCommand malloc = new BlCommand(al,
            HardwareFunctions.MALLOC_FUNCTION.toString());
        commands.add(malloc);

        NormalRegister exprRegister = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(exprRegister);

        //mov exprRegister in R0
        MovCommand movRegisterR0
            = new MovCommand(al, exprRegister, R0);
        commands.add(movRegisterR0);

        // generates command for the first elem
        commands.addAll(leftExpression.generateCommandsForExpression());

        // left expression register
        NormalRegister leftExprRegister = hardwareManager.getStorageRegister();
        hardwareManager.freeRegister(leftExprRegister);

        int offset;


        // load the correct value in R0
        if (leftExpression instanceof CharLiteral
            || leftExpression instanceof BoolLiteral) {

            LdrCommand ldr1InR0 = new LdrCommand(al, w, R0, "1");
            commands.add(ldr1InR0);
            offset = MainWaccTable.STACK_SIZE_1;

        } else {

            LdrCommand ldr4InR0 = new LdrCommand(al, w, R0, "4");
            commands.add(ldr4InR0);
            offset = MainWaccTable.STACK_SIZE_4;
        }

        // allocate the second elem of newpair
        commands.add(malloc);

        // store last register in R0
        StrCommand strLeftRegInR0;

        if (offset == MainWaccTable.STACK_SIZE_1) {
            strLeftRegInR0
                = new StrCommand(al, DataSize.B, leftExprRegister, R0);
        } else {
            strLeftRegInR0 = new StrCommand(al, w, leftExprRegister, R0);
        }
        commands.add(strLeftRegInR0);

        // store R0 in memory
        StrCommand strR0InExprReg
            = new StrCommand(al, w, R0, exprRegister);
        commands.add(strR0InExprReg);


        // generates command for second expression
        commands.addAll(rightExpression.generateCommandsForExpression());
        NormalRegister rightExprRegister = hardwareManager.getStorageRegister();
        hardwareManager.freeRegister(rightExprRegister);

        // load the correct value in R0
        if (rightExpression instanceof CharLiteral
            || rightExpression instanceof BoolLiteral) {

            LdrCommand ldr1InR0 = new LdrCommand(al, w, R0, "1");
            commands.add(ldr1InR0);
            offset = MainWaccTable.STACK_SIZE_1;

        } else {

            LdrCommand ldr4InR0 = new LdrCommand(al, w, R0, "4");
            commands.add(ldr4InR0);
            offset = MainWaccTable.STACK_SIZE_4;
        }
        // allocate the new pair
        commands.add(malloc);

        // store last register in R0

        StrCommand strRightRegInR0;
        if (offset == MainWaccTable.STACK_SIZE_1) {

            strRightRegInR0
                = new StrCommand(al, DataSize.B, rightExprRegister, R0);

        } else {

            strRightRegInR0 = new StrCommand(al, w, rightExprRegister, R0);
        }
        commands.add(strRightRegInR0);

        // store R0 in memory
        strR0InExprReg
            = new StrCommand(al, w, R0, exprRegister, "4", true, false);
        commands.add(strR0InExprReg);

        return commands;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            "NewpairAssignment",
            new String[]{
                "LeftPairExpression",
                "RightPairExpression"},
            new String[]{
                leftExpression.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE),
                rightExpression.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
