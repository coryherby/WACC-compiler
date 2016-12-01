package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.HardwareFunctions.HardwareFunctions;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ComparisonCommand.CmpCommand;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.AddCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.AndCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.OrrCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.SubCommand;
import wacc.CodeGenerator.Commands.MutliplyCommand.SMullCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PopCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PushCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryOperatorExpr implements Expression, AssignRhs {

    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String DIVIDE_FUNCTION = "__aeabi_idiv";

    public enum BinOp {
        MULTIPLY,
        DIVIDE,
        MODULUS,
        PLUS,
        MINUS,
        GREATER,
        GREATER_OR_EQUAL,
        SMALLER,
        SMALLER_OR_EQUAL,
        EQUAL,
        NOT_EQUAL,
        AND,
        OR
    }

    private BinOp binOp;
    private Expression leftExpression;
    private Expression rightExpression;

    public BinaryOperatorExpr(BinOp binOp,
                              Expression leftExpression,
                              Expression rightExpression) {

        this.binOp = binOp;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        HardwareManager hardwareManager = HardwareManager.getInstance();
        PredefinedFunctionGenerator predefinedFunctionGenerator
            = PredefinedFunctionGenerator.getInstance();

        // generate the commands of the left expression
        List<Command> commands = leftExpression.generateCommandsForExpression();

        NormalRegister r1 = hardwareManager.getStorageRegister();

        // If we run out of registers, push r10 on the stack and free it in the
        // hardware manager as it has been push and can be used again
        if (r1 == NormalRegister.R10) {
            PushCommand pushCommand = new PushCommand(ConditionCode.AL,
                new ArrayList<>(Collections.singletonList(r1)));
            commands.add(pushCommand);

            hardwareManager.freeRegister(r1);
        }

        hardwareManager.addStorageRegister(r1);

        NormalRegister r2 = r1;

        // generate commands for the right expression
        List<Command> rightExpressionCommands
            = rightExpression.generateCommandsForExpression();

        // get the storage register of right expression
        NormalRegister r3 = hardwareManager.getStorageRegister();

        // frees register r3
        hardwareManager.freeRegister(r3);

        // add all commands of the right expression in the list of commands
        commands.addAll(rightExpressionCommands);

        // If we had run out of registers, then pop back the result from the
        // stack in r11 to perform operations
        if (r3 == NormalRegister.R10) {
            // Perform check to see if we really ran our of registers
            NormalRegister nextStorageRegister
                = hardwareManager.getStorageRegister();

            if (nextStorageRegister == NormalRegister.R10) {
                // Pop in register r11
                r3 = NormalRegister.R11;
                PopCommand popCommand = new PopCommand(ConditionCode.AL,
                    new ArrayList<>(Collections.singletonList(r3)));
                commands.add(popCommand);

                // switch registers for operations
                r2 = r3;
                r3 = r1;
            }

            // Put back the register taken to check if we ran out of registers
            hardwareManager.addStorageRegister(nextStorageRegister);
        }

        NormalRegister R0 = hardwareManager.getRegister0();
        NormalRegister R1 = hardwareManager.getRegister1();

        // creations of the variables that are used in commands
        ConditionCode al = ConditionCode.AL;
        ConditionCode ne = ConditionCode.NE;
        ConditionCode eq = ConditionCode.EQ;
        ConditionCode ge = ConditionCode.GE;
        ConditionCode lt = ConditionCode.LT;
        ConditionCode gt = ConditionCode.GT;
        ConditionCode conditionField = ConditionCode.LE;

        switch (binOp) {

            case MULTIPLY:
                // multiply r1 and r3
                SMullCommand smullr1r3
                    = new SMullCommand(ConditionCode.AL, r1, r3, r1, r3);
                commands.add(smullr1r3);

                CmpCommand asrCompare = new CmpCommand(
                    ConditionCode.AL, r3, r1, "ASR #31");
                commands.add(asrCompare);

                // Check if no overflow command
                commands.add(overflowErrorCheckingCommand(
                    predefinedFunctionGenerator, ConditionCode.NE));

                break;

            case DIVIDE:
                // mov the two value of the register into R1 and R0 to put them
                // as parameters for the different checks
                MovCommand movR0r1 = new MovCommand(al, R0, r1);
                MovCommand movR1r3 = new MovCommand(al, R1, r3);

                commands.add(movR0r1);
                commands.add(movR1r3);

                // checks if is being divide by zero
                BlCommand checkDivideZero
                    = new BlCommand(al,
                        PredefinedFunctionLabels.DIVIDE_BY_ZERO.toString());

                // Generated the function in case of expression divided by 0
                predefinedFunctionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.DIVIDE_BY_ZERO);

                BlCommand aeabi_idiv = new BlCommand(al,
                    HardwareFunctions.DIVIDE_FUNCTION.toString());

                commands.add(checkDivideZero);
                commands.add(aeabi_idiv);

                // gets the result of the checks
                MovCommand movr1R0 = new MovCommand(al, r1, R0);

                // stores value on stack

                commands.add(movr1R0);
                break;


            case MODULUS:
                // mov the two value of the register into R1 and R0 to put them
                // as parameters for the different checks
                movR0r1
                    = new MovCommand(al, R0, r1);
                movR1r3
                    = new MovCommand(al, R1, r3);

                commands.add(movR0r1);
                commands.add(movR1r3);

                // checks if is being divide by zero
                checkDivideZero
                    = new BlCommand(al,
                        PredefinedFunctionLabels.DIVIDE_BY_ZERO.toString());

                // Generated the function in case of expression divided by 0
                predefinedFunctionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.DIVIDE_BY_ZERO);

                BlCommand aeabi_idivmod = new BlCommand(al,
                    HardwareFunctions.DIVIDE_MOD_FUNCTION.toString());

                commands.add(checkDivideZero);
                commands.add(aeabi_idivmod);

                // gets the result of the checks
                Command movr1R1 = new MovCommand(al, r1, R1);
                // stores value on stack

                commands.add(movr1R1);

                break;

            case PLUS:
                // add r1 r3
                AddCommand addr1r2 = new AddCommand(
                    ConditionCode.SAL, r1, r2, r3);
                commands.add(addr1r2);

                // Check if no overflow command
                commands.add(overflowErrorCheckingCommand(
                    predefinedFunctionGenerator, ConditionCode.VS));

                break;

            case MINUS:
                // substract r1 r3
                SubCommand subr1r2 = new SubCommand(
                    ConditionCode.SAL, r1, r2, r3);
                commands.add(subr1r2);

                // Check if no overflow command
                commands.add(overflowErrorCheckingCommand(
                    predefinedFunctionGenerator, ConditionCode.VS));

                break;

            case SMALLER:
                // checks if r1 < r3
                CmpCommand cmpr1r2 = new CmpCommand(al, r1, r3);
                commands.add(cmpr1r2);

                // if true then r1 becomes 1 else becomes 0
                MovCommand isSmaller
                    = new MovCommand(lt, r1, ONE);
                MovCommand isNotSmaller
                    = new MovCommand(ge, r1, ZERO);

                commands.add(isSmaller);
                commands.add(isNotSmaller);
                break;

            case SMALLER_OR_EQUAL:
                // checks if r1 <= r3
                CmpCommand cmp = new CmpCommand(al, r1, r3);
                commands.add(cmp);

                // if true then r1 becomes 1 else becomes 0
                MovCommand isSmallerOrEqual
                    = new MovCommand(conditionField, r1, ONE);
                MovCommand isGreater
                    = new MovCommand(gt, r1, ZERO);

                commands.add(isSmallerOrEqual);
                commands.add(isGreater);
                break;

            case GREATER:
                // checks if r1 > r3
                cmp = new CmpCommand(al, r1, r3);
                commands.add(cmp);

                // if true then r1 becomes 1 else becomes 0
                isGreater
                    = new MovCommand(gt, r1, ONE);
                isSmallerOrEqual
                    = new MovCommand(conditionField, r1, ZERO);

                commands.add(isGreater);
                commands.add(isSmallerOrEqual);
                break;

            case GREATER_OR_EQUAL:
                // checks if r1 >= r3
                cmp = new CmpCommand(al, r1, r3);
                commands.add(cmp);

                // if true then r1 becomes 1 else becomes 0
                MovCommand isGreaterOrEqual
                    = new MovCommand(ge, r1, ONE);
                commands.add(isGreaterOrEqual);

                isSmaller
                    = new MovCommand(lt, r1, ZERO);

                commands.add(isSmaller);
                break;

            case EQUAL:
                // checks if r1 == r3
                cmp = new CmpCommand(al, r1, r3);
                commands.add(cmp);

                // if true then r1 becomes 1 else becomes 0
                MovCommand isEqual
                    = new MovCommand(eq, r1, ONE);
                MovCommand isNotEqual
                    = new MovCommand(ne, r1, ZERO);

                commands.add(isEqual);
                commands.add(isNotEqual);
                break;

            case NOT_EQUAL:
                // checks if r1 != r3
                cmp = new CmpCommand(al, r1, r3);
                commands.add(cmp);

                // if true then r1 becomes 1 else becomes 0
                isNotEqual
                    = new MovCommand(ne, r1, ONE);
                isEqual
                    = new MovCommand(eq, r1, ZERO);

                commands.add(isNotEqual);
                commands.add(isEqual);
                break;

            case AND:
                // creates the and command
                AndCommand and
                    = new AndCommand(al, r1, r2, r3);

                commands.add(and);
                break;

            case OR:
                // creates the or command
                OrrCommand or
                    = new OrrCommand(al, r1, r2, r3);
                commands.add(or);

                break;

            default:
                break;
        }

        return commands;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {
        return generateCommandsForExpression();
    }

    public WaccType getType() {

        switch (binOp) {

            case MULTIPLY:
            case DIVIDE:
            case MINUS:
            case PLUS:
            case MODULUS:
                return WaccType.INT;

            case GREATER:
            case GREATER_OR_EQUAL:
            case SMALLER:
            case SMALLER_OR_EQUAL:
            case EQUAL:
            case NOT_EQUAL:
            case AND:
            case OR:
                return WaccType.BOOL;

            default:
                return WaccType.NULL;
        }
    }

    private Command overflowErrorCheckingCommand(
        PredefinedFunctionGenerator predefinedFunctionGenerator,
        ConditionCode conditionCode) {

        // Check if no overflow
        BlCommand overflowBl = new BlCommand(conditionCode,
            PredefinedFunctionLabels.THROW_OVERFLOW_ERROR.toString());

        // Generated the function in case of overflow
        predefinedFunctionGenerator.addPredefinedFunction(
            PredefinedFunctionLabels.THROW_OVERFLOW_ERROR);

        return overflowBl;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "BinaryOperator Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "BinaryOperator Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            new String[]{
                "Binary Operator",
                "LeftExpression",
                "RightExpression"},
            new String[]{
                indentDepth + binOp.toString(),
                leftExpression.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE),
                rightExpression.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
