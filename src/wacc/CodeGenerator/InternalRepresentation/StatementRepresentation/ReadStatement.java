package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.AddCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignLhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.PairElemAssignment;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.ArrayElem;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Identification;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.*;

import java.util.ArrayList;
import java.util.List;

public class ReadStatement implements Statement {

    private AssignLhs readVariable;

    public ReadStatement(AssignLhs readVariable) {
        this.readVariable = readVariable;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        ArrayList<Command> commands = new ArrayList<>();

        // Puts the right variable address in r0
        commands.addAll(readVariable.generateCommandsForAssignLhs());

        HardwareManager hardwareManager = HardwareManager.getInstance();

        ConditionCode always = ConditionCode.AL;
        NormalRegister r0 = hardwareManager.getRegister0();

        String identifier = readVariable.getIdentifier();

        NormalRegister register = hardwareManager.getFreeRegister();
        int offset = hardwareManager.getVariableStackOffset(identifier);
        String offsetString = String.valueOf(offset);

        // Putting address of variable into a new register
        AddCommand addCommand = new AddCommand(ConditionCode.AL, register,
            SpecialRegister.SP, offsetString);
        commands.add(addCommand);

        // Putting address in r0
        MovCommand movInR0 = new MovCommand(ConditionCode.AL, r0, register);
        commands.add(movInR0);

        // Calling the right read function
        String readFunction = null;
        PredefinedFunctionGenerator functionGenerator
            = PredefinedFunctionGenerator.getInstance();

        if (readVariable instanceof ArrayElem) {

            Type type = hardwareManager.getVariableType(identifier);

            while (((ArrayType) type).getNestedType()
                instanceof ArrayType) {

                type = ((ArrayType) type).getNestedArrayType();
            }

            type = ((ArrayType) type).getNestedType();

            if (type.equals(new BaseType(WaccType.CHAR))) {

                readFunction
                    = PredefinedFunctionLabels.READ_CHAR_FUNCTION.toString();
                functionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.READ_CHAR_FUNCTION);

            } else if (type.equals(new BaseType(WaccType.INT))) {

                readFunction
                    = PredefinedFunctionLabels.READ_INT_FUNCTION.toString();
                functionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.READ_INT_FUNCTION);
            }

        } else if (readVariable instanceof PairElemAssignment) {

            Type type = null;

            PairType pairType
                = (PairType) hardwareManager.getVariableType(identifier);
            PairElemType pairElemType = null;

            if (((PairElemAssignment) readVariable).getPosition().equals(
                PairElemAssignment.PairElemPosition.FIRST)) {

                pairElemType = pairType.getLeftType();

            } else if (((PairElemAssignment) readVariable).getPosition().equals(
                PairElemAssignment.PairElemPosition.SECOND)) {

                pairElemType = pairType.getRightType();

            }

            if (pairElemType != null) {

                while (pairElemType.isPairElemOfTypePair()) {
                    pairElemType
                        = (PairElemType) pairElemType.getPairElementType();
                }

                type = pairElemType;

            } else {
                throw new RuntimeException("Trying to read invalid variable");
            }


            if (type.equals(new BaseType(WaccType.CHAR))) {

                readFunction
                    = PredefinedFunctionLabels.READ_CHAR_FUNCTION.toString();
                functionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.READ_CHAR_FUNCTION);

            } else if (type.equals(new BaseType(WaccType.INT))) {

                readFunction
                    = PredefinedFunctionLabels.READ_INT_FUNCTION.toString();
                functionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.READ_INT_FUNCTION);

            }

        } else if (readVariable instanceof Identification) {

            Type type = hardwareManager.getVariableType(identifier);

            if (type.equals(new BaseType(WaccType.CHAR))) {

                readFunction
                    = PredefinedFunctionLabels.READ_CHAR_FUNCTION.toString();
                functionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.READ_CHAR_FUNCTION);

            } else if (type.equals(new BaseType(WaccType.INT))) {

                readFunction
                    = PredefinedFunctionLabels.READ_INT_FUNCTION.toString();
                functionGenerator.addPredefinedFunction(
                    PredefinedFunctionLabels.READ_INT_FUNCTION);

            }

        }

        // Call method read (int or char depending on the type)
        BlCommand readMethodCall
            = new BlCommand(ConditionCode.AL, readFunction);
        commands.add(readMethodCall);

        hardwareManager.freeRegister(register);

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
            "ReadStatement",
            readVariable.generateLhsRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
