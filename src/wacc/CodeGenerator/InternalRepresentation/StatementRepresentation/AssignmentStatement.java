package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.StrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignLhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.PairElemAssignment;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.PairElemAssignment.PairElemPosition;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.ArrayElem;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Identification;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.*;
import wacc.WaccTable.MainWaccTable;

import java.util.List;

public class AssignmentStatement implements Statement {

    private AssignLhs variable;
    private AssignRhs assignment;

    public AssignmentStatement(AssignLhs variable,
                               AssignRhs assignment) {

        this.variable = variable;
        this.assignment = assignment;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        List<Command> commands = assignment.generateCommandsForAssignRhs();
        HardwareManager hardwareManager = HardwareManager.getInstance();

        commands.addAll(variable.generateCommandsForAssignLhs());

        String identifier = variable.getIdentifier();
        int offset = hardwareManager.getVariableStackOffset(identifier);
        String offsetString = String.valueOf(offset);

        StrCommand strAssignment = null;

        if (variable instanceof PairElemAssignment) {

            NormalRegister rhsRegister = hardwareManager.getStorageRegister();
            hardwareManager.freeRegister(rhsRegister);

            NormalRegister strRegister = hardwareManager.getStorageRegister();
            hardwareManager.freeRegister(strRegister);

            // pair the type of the pair
            PairType pair
                = (PairType) hardwareManager.getVariableType(identifier);

            // get the position requested in the in the assignment
            PairElemPosition position
                = ((PairElemAssignment) variable).getPosition();

            int elemRequestedSize = 0;

            Type pairElementType;
            WaccType type;
            if (position == PairElemPosition.FIRST) {

                // case if the requested position is the first element of the
                // pair
                // find the type of the first element of the pair
                pairElementType = pair.getLeftType().getPairElementType();

                if (pairElementType != null) {

                    type = pairElementType.getWaccType();

                    // gets the correct size of the element
                    if (type == WaccType.BOOL || type == WaccType.CHAR) {

                        elemRequestedSize = MainWaccTable.STACK_SIZE_1;

                    } else {

                        elemRequestedSize = MainWaccTable.STACK_SIZE_4;

                    }
                } else {
                    elemRequestedSize = MainWaccTable.STACK_SIZE_4;
                }

            } else if (position == PairElemPosition.SECOND) {
                // find the type of the first element of the pair
                pairElementType = pair.getRightType().getPairElementType();

                if (pairElementType != null) {

                    type = pairElementType.getWaccType();

                    // gets the correct size of the element depending on the
                    // type of the element
                    if (type == WaccType.BOOL || type == WaccType.CHAR) {

                        elemRequestedSize = MainWaccTable.STACK_SIZE_1;

                    } else {

                        elemRequestedSize = MainWaccTable.STACK_SIZE_4;
                    }

                } else {
                    elemRequestedSize = MainWaccTable.STACK_SIZE_4;
                }

            }

            // generate the appropriate store command corresponding to
            // element size
            if (elemRequestedSize == MainWaccTable.STACK_SIZE_1) {

                strAssignment = new StrCommand(ConditionCode.AL, DataSize.B,
                    strRegister, rhsRegister);

            } else if (elemRequestedSize == MainWaccTable.STACK_SIZE_4) {

                strAssignment = new StrCommand(ConditionCode.AL, DataSize.W,
                    strRegister, rhsRegister);

            }

        } else if (variable instanceof ArrayElem) {

            NormalRegister lhsRegister = hardwareManager.getStorageRegister();
            hardwareManager.freeRegister(lhsRegister);

            NormalRegister strRegister = hardwareManager.getStorageRegister();
            hardwareManager.freeRegister(strRegister);

            int variableSize = 0;

            Type type = hardwareManager.getVariableType(identifier);

            if (type instanceof ArrayType) {

                while (((ArrayType) type).getNestedType()
                    instanceof ArrayType) {

                    type = ((ArrayType) type).getNestedArrayType();
                }

                type = ((ArrayType) type).getNestedType();

                if (type.equals(
                    new BaseType(WaccType.BOOL)) || type.equals(
                    new BaseType(WaccType.CHAR))) {

                    variableSize = MainWaccTable.STACK_SIZE_1;

                } else {
                    variableSize = MainWaccTable.STACK_SIZE_4;
                }

            } else if (type.equals(new BaseType(WaccType.STRING))) {
                variableSize = MainWaccTable.STACK_SIZE_1;
            }

            if (variableSize == MainWaccTable.STACK_SIZE_1) {

                // Store Byte with no register offset
                strAssignment = new StrCommand(ConditionCode.AL, DataSize.B,
                    strRegister, lhsRegister);

            } else if (variableSize == MainWaccTable.STACK_SIZE_4) {

                // Store Word with no register offset
                strAssignment = new StrCommand(ConditionCode.AL, DataSize.W,
                        strRegister, lhsRegister);

            }


        } else if (variable instanceof Identification)

        {

            NormalRegister strRegister = hardwareManager.getStorageRegister();
            hardwareManager.freeRegister(strRegister);

            int variableSize = hardwareManager.
                getVariableMemorySize(identifier);

            if (variableSize == MainWaccTable.STACK_SIZE_1 && offset == 0) {

                // Store Byte with no register offset
                strAssignment = new StrCommand(ConditionCode.AL, DataSize.B,
                    strRegister, SpecialRegister.SP);

            } else if (variableSize == MainWaccTable.STACK_SIZE_4
                && offset == 0) {

                // Store Word with no register offset
                strAssignment = new StrCommand(ConditionCode.AL, DataSize.W,
                    strRegister, SpecialRegister.SP);

            } else if (variableSize == MainWaccTable.STACK_SIZE_1
                && offset != 0) {

                // Store Byte with a register offset
                strAssignment = new StrCommand(ConditionCode.AL, DataSize.B,
                    strRegister, SpecialRegister.SP, offsetString, true, false);

            } else if (variableSize == MainWaccTable.STACK_SIZE_4
                && offset != 0) {

                // Store Word with a register offset
                strAssignment = new StrCommand(ConditionCode.AL, DataSize.W,
                    strRegister, SpecialRegister.SP, offsetString, true, false);
            }
        }

        commands.add(strAssignment);

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
            "AssignmentStatement",
            new String[]{
                "Variable",
                "Assignement"},
            new String[]{
                variable.generateLhsRepresentation(
                    depthCount
                        + RepresentationFormatter.INDENT_TWICE),
                assignment.generateAssignRhsRepresentation(
                    depthCount
                        + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
