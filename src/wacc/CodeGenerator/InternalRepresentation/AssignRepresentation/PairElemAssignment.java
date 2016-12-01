package wacc.CodeGenerator.InternalRepresentation.AssignRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Identification;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.PairType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;
import wacc.WaccTable.MainWaccTable;

import java.util.ArrayList;
import java.util.List;

public class PairElemAssignment implements AssignRhs, AssignLhs {

    public enum PairElemPosition {
        FIRST,
        SECOND
    }

    private PairElemPosition position;
    private Identification pairExpression;

    public PairElemAssignment(PairElemPosition position,
                              Expression pairExpression) {

        this.position = position;
        // pairExpression is always an Identification Expression
        this.pairExpression = (Identification) pairExpression;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();

        // generates and add the commands of the expression into the list of
        // commands
        commands.addAll(pairExpression.generateCommandsForExpression());

        // gets the expression register
        NormalRegister exprReg = hardwareManager.getStorageRegister();
        hardwareManager.addStorageRegister(exprReg);
        NormalRegister register0 = hardwareManager.getRegister0();

        // Mov expr register into r0
        MovCommand movRegInR0
            = new MovCommand(ConditionCode.AL, register0, exprReg);
        commands.add(movRegInR0);

        // checks is its a null pointer
        BlCommand checkNull
            = new BlCommand(ConditionCode.AL,
                PredefinedFunctionLabels.CHECK_NULL_POINTER.toString());
        commands.add(checkNull);

        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();

        codeGenHelper.addPredefinedFunction(PredefinedFunctionLabels.
            CHECK_NULL_POINTER);

        // load the correct value into the expression reg corresponding to
        // the position requested
        switch (position) {

            case FIRST:
                LdrCommand ldrValueReg = new LdrCommand(ConditionCode.AL,
                    DataSize.W, exprReg, exprReg);
                commands.add(ldrValueReg);
                break;
            case SECOND:
                ldrValueReg = new LdrCommand(ConditionCode.AL, DataSize.W,
                    exprReg, exprReg, "4", true, false);
                commands.add(ldrValueReg);
                break;

            default:
                break;
        }

        // gets the type of the pair
        String identifier = pairExpression.getIdentifier();
        PairType pair = (PairType) hardwareManager.getVariableType(identifier);

        int offset = 0;

        switch (position) {

            case FIRST:
                // gets the type of the first element of the pair
                Type pairElementType = pair.getLeftType().getPairElementType();

                offset = getOffset(pairElementType);
                break;

            case SECOND:
                // gets the type of the second element of the pair
                pairElementType = pair.getRightType().getPairElementType();

                offset = getOffset(pairElementType);
                break;

            default:
                break;
        }

        // generate the correct load command depending on the offset
        if (offset == MainWaccTable.STACK_SIZE_1) {

            LdrCommand ldrValueReg = new LdrCommand(ConditionCode.SAL,
                DataSize.B, exprReg, exprReg);
            commands.add(ldrValueReg);

        } else {

            LdrCommand ldrValueReg = new LdrCommand(ConditionCode.AL,
                DataSize.W, exprReg, exprReg);
            commands.add(ldrValueReg);
        }

        return commands;
    }

    private int getOffset(Type pairElementType) {
        int offset;

        if (pairElementType != null){

            WaccType type = pairElementType.getWaccType();

            // set the offset depending on the type of the element
            offset = getTypeOffset(type);

        } else {

            offset = MainWaccTable.STACK_SIZE_4;
        }
        return offset;
    }

    private int getTypeOffset(WaccType type) {

        int offset;
        if (type == WaccType.BOOL || type == WaccType.CHAR){
            offset = MainWaccTable.STACK_SIZE_1;
        } else {
            offset = MainWaccTable.STACK_SIZE_4;
        }
        return offset;
    }

    @Override
    public List<Command> generateCommandsForAssignLhs() {
        List<Command> commands = new ArrayList<>();

        commands.addAll(pairExpression.generateCommandsForAssignRhs());

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister exprReg = hardwareManager.getStorageRegister();
        hardwareManager.addStorageRegister(exprReg);
        NormalRegister register0 = hardwareManager.getRegister0();

        // Mov expr register into r0
        MovCommand movRegInR0
            = new MovCommand(ConditionCode.AL, register0, exprReg);
        commands.add(movRegInR0);

        // checks is its a null pointer
        BlCommand checkNull
            = new BlCommand(ConditionCode.AL,
                PredefinedFunctionLabels.CHECK_NULL_POINTER.toString());
        commands.add(checkNull);

        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();

        codeGenHelper.addPredefinedFunction(
            PredefinedFunctionLabels.CHECK_NULL_POINTER);

        // load the correct value into the expression reg corresponding to
        // the position requested
        switch (position) {

            case FIRST:
                LdrCommand ldrValueReg = new LdrCommand(ConditionCode.AL,
                    DataSize.W, exprReg, exprReg);
                commands.add(ldrValueReg);
                break;
            case SECOND:
                ldrValueReg = new LdrCommand(ConditionCode.AL, DataSize.W,
                    exprReg, exprReg, "4", true, false);
                commands.add(ldrValueReg);
                break;

            default:
                break;
        }

        return commands;
    }

    @Override
    public String getIdentifier() {
        return pairExpression.getIdentifier();
    }

    public PairElemPosition getPosition() {
        return position;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "PairElem Assignment");
    }

    @Override
    public String generateLhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "PairElem Assigned");
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
                "PairElem Position",
                "Pair Expression"},
            new String[]{
                indentDepth + position.toString(),
                pairExpression.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
