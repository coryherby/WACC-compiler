package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.StrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.WaccTable.MainWaccTable;

import java.util.List;

public class InitialisationStatement implements Statement {

    private Type variableType;
    private String identifier;
    private AssignRhs assignement;

    public InitialisationStatement(Type variableType,
                                   String identifier,
                                   AssignRhs assignement) {

        this.variableType = variableType;
        this.identifier = identifier;
        this.assignement = assignement;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        List<Command> commands = assignement.generateCommandsForAssignRhs();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister strRegister = hardwareManager.getStorageRegister();
        hardwareManager.freeRegister(strRegister);

        // Set this variable as being initialised for expression
        hardwareManager.removeFreedReference(identifier);
        hardwareManager.registerInitialisationIdentifier(identifier);

        int offset = hardwareManager.getVariableStackOffset(identifier);
        String offsetString = String.valueOf(offset);

        int variableSize = hardwareManager.
            getVariableMemorySize(identifier);

        StrCommand strAssignment = null;

        if (variableSize == MainWaccTable.STACK_SIZE_1 && offset == 0) {

            // Store Byte with no register offset
            strAssignment = new StrCommand(ConditionCode.AL, DataSize.B,
                strRegister, SpecialRegister.SP);

        } else if (variableSize == MainWaccTable.STACK_SIZE_4 && offset == 0) {

            // Store Word with no register offset
            strAssignment = new StrCommand(ConditionCode.AL, DataSize.W,
                strRegister, SpecialRegister.SP);

        } else if (variableSize == MainWaccTable.STACK_SIZE_1 && offset != 0) {

            // Store Byte with a register offset
            strAssignment = new StrCommand(ConditionCode.AL, DataSize.B,
                strRegister, SpecialRegister.SP, offsetString, true, false);

        } else if (variableSize == MainWaccTable.STACK_SIZE_4 && offset != 0) {

            // Store Word with a register offset
            strAssignment = new StrCommand(ConditionCode.AL, DataSize.W,
                strRegister, SpecialRegister.SP, offsetString, true, false);
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
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            "InitialisationStatement",
            new String[] {
                "Variable Type",
                "Identifier",
                "Assignement"},
            new String[] {
                indentDepth + variableType.toString(),
                indentDepth + identifier,
                assignement.generateAssignRhsRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
