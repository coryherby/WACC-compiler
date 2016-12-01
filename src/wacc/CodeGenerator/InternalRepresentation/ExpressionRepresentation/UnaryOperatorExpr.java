package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.EorCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.RsbCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

import java.util.List;

public class UnaryOperatorExpr implements Expression, AssignRhs {

    private static final String ZERO = "0";
    private static final String ONE = "1";

    public enum UnaryOp {
        LOGICAL_NOT,
        NEGATION,
        ARRAY_LENGTH,
        ASCII_VALUE,
        CHAR_REPRESENTATION
    }

    private UnaryOp unaryOp;
    private Expression expression;

    public UnaryOperatorExpr(UnaryOp unaryOp,
                             Expression expression) {

        this.unaryOp = unaryOp;
        this.expression = expression;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        List<Command> commands = expression.generateCommandsForExpression();


        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register = hardwareManager.getStorageRegister();
        hardwareManager.addStorageRegister(register);

        // creates all variables needed in the commands
        ConditionCode sal = ConditionCode.SAL;
        ConditionCode al = ConditionCode.AL;
        DataSize b = DataSize.B;
        DataSize w = DataSize.W;
        SpecialRegister sp = SpecialRegister.SP;

        switch (unaryOp) {
            case LOGICAL_NOT:
                // if unaryOp if !expression
                EorCommand eorCommand
                    = new EorCommand(al, register, register, ONE);
                commands.add(eorCommand);
                break;

            case NEGATION:
                // if unaryOp if -expression
                RsbCommand notCommand
                    = new RsbCommand(sal, register, register, ZERO);
                commands.add(notCommand);

                // Check if throw overflow error
                commands.add(overflowErrorCheckingCommand());

                break;

            case ARRAY_LENGTH:

                // load the value of register in use in the memory
                LdrCommand ldrRegisterMemory
                    = new LdrCommand(al, w, register, register);

                commands.add(ldrRegisterMemory);

                break;

            case ASCII_VALUE:

            case CHAR_REPRESENTATION:

            default:
                break;
        }

        return commands;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {

        return generateCommandsForExpression();

    }

    private Command overflowErrorCheckingCommand() {

        PredefinedFunctionGenerator predefinedFunctionGenerator
            = PredefinedFunctionGenerator.getInstance();

        // Check if no overflow
        BlCommand overflowBl = new BlCommand(ConditionCode.VS,
            PredefinedFunctionLabels.THROW_OVERFLOW_ERROR.toString());

        // Generated the function in case of overflow
        predefinedFunctionGenerator.addPredefinedFunction(
            PredefinedFunctionLabels.THROW_OVERFLOW_ERROR);

        return overflowBl;
    }

    public WaccType getType() {
        switch (unaryOp) {
            case LOGICAL_NOT:
                return WaccType.BOOL;
            case NEGATION:
            case ARRAY_LENGTH:
            case ASCII_VALUE:
                return WaccType.INT;
            case CHAR_REPRESENTATION:
                return WaccType.CHAR;
            default:
                return WaccType.NULL;
        }
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "UnaryOperator Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "UnaryOperator Expression");
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
                "UnaryOperator",
                "Expression"},
            new String[]{
                indentDepth + unaryOp.toString(),
                expression.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
