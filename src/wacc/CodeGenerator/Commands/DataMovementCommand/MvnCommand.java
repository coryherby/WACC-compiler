package wacc.CodeGenerator.Commands.DataMovementCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class MvnCommand extends DataMovementCommand {

    public MvnCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      String value) {

        super(conditionField, destinationRegister, value);
    }

    public MvnCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister) {

        super(conditionField, destinationRegister, sourceRegister);
    }

    public MvnCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister,
                      String registerShift) {

        super(conditionField, destinationRegister, sourceRegister,
            registerShift);
    }

    @Override
    protected String getCommandCode() {
        return "MVN";
    }

    public boolean canBeOptimisedForLogicalOrCmpCommands() {
        return super.canBeOptimisedForLogicalOrCmpCommands();
    }
}

