package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignLhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.WaccTable.MainWaccTable;

import java.util.ArrayList;
import java.util.List;

public class Identification implements Expression, AssignRhs, AssignLhs {

    private String identifier;

    public Identification(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(register);

        ArrayList<Command> commands = new ArrayList<>();

        // Case where free statement is used, checking for double free
        if(hardwareManager.isMemoryAddressBeingFreed()) {
            if(hardwareManager.hasAlreadyBeenFreed(identifier)) {
                PredefinedFunctionGenerator codeGenHelper
                    = PredefinedFunctionGenerator.getInstance();

                codeGenHelper.addPredefinedFunction(
                    PredefinedFunctionLabels.TROW_RUNTIME_ERROR_FREE);

                // Throw runtime error
                BCommand freeRuntimeError =
                    new BCommand(ConditionCode.AL, PredefinedFunctionLabels
                        .TROW_RUNTIME_ERROR_FREE.toString());
                commands.add(freeRuntimeError);
            } else {
                hardwareManager.addToFreedReference(identifier);
                hardwareManager.setMemoryAddressBeingFreed(false);
            }
        }

        ConditionCode al = ConditionCode.AL;

        DataSize w = DataSize.W;
        SpecialRegister sp = SpecialRegister.SP;
        int variableSize = hardwareManager.getVariableMemorySize(identifier);
        int offset = hardwareManager.getVariableStackOffset(identifier)
            + hardwareManager.getTemporaryScopeOffset();
        String offsetString = String.valueOf(offset);

        LdrCommand loadVariable = null;

        if (variableSize == MainWaccTable.STACK_SIZE_1 && offset == 0) {
            loadVariable
                = new LdrCommand(al, DataSize.SB, register, sp);

        } else if (variableSize == MainWaccTable.STACK_SIZE_4 && offset == 0) {
            loadVariable
                = new LdrCommand(al, w, register, sp);

        } else if (variableSize == MainWaccTable.STACK_SIZE_1 && offset != 0) {
            loadVariable
                = new LdrCommand(al, DataSize.SB, register, sp,
                offsetString, true, false);

        } else if (variableSize == MainWaccTable.STACK_SIZE_4 && offset != 0) {
            loadVariable
                = new LdrCommand(al, w, register, sp,
                offsetString, true, false);
        }


        commands.add(loadVariable);

        return commands;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {

        return generateCommandsForExpression();
    }

    @Override
    public List<Command> generateCommandsForAssignLhs() {

        return new ArrayList<>();
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateLhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "Identification Assigned");
    }

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "Identification Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "Identification Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            indentDepth + identifier
        );
    }

}
