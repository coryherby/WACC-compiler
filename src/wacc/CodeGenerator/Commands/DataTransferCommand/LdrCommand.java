package wacc.CodeGenerator.Commands.DataTransferCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.Register;

public class LdrCommand extends DataTransferCommand {

    public LdrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      String value) {

        super(conditionField, dataSize, destinationRegister, value);
    }

    public LdrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister) {

        super(conditionField, dataSize, destinationRegister, sourceRegister);
    }

    public LdrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister,
                      String value,
                      boolean isPreIndexAddressing,
                      boolean isDualUpdating) {

        super(conditionField, dataSize, destinationRegister, sourceRegister,
            value, isPreIndexAddressing, isDualUpdating);
    }

    public LdrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister,
                      Register sourceRegister2,
                      boolean isPreIndexAddressing,
                      boolean isDualUpdating) {

        super(conditionField, dataSize, destinationRegister, sourceRegister,
            sourceRegister2, isPreIndexAddressing, isDualUpdating);
    }

    public LdrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister,
                      Register sourceRegister2,
                      String value,
                      boolean isPreIndexAddressing,
                      boolean isDualUpdating) {

        super(conditionField, dataSize, destinationRegister, sourceRegister,
            sourceRegister2, value, isPreIndexAddressing, isDualUpdating);
    }

    public boolean canBeOptimisedForLogicalOrCmpCommands() {
        return super.canBeOptimisedForLogicalOrCmpCommands();
    }

    public boolean canBeOptimisedForMovCommands() {
        return super.canBeOptimisedForMovCommands();
    }

    @Override
    protected String getCommandCode() {
        return "LDR";
    }

    public LdrCommand optimiseMovCommand(MovCommand movCommand) {

        Register destinationRegister = movCommand.getDestinationRegister();

        this.setDestinationRegister(destinationRegister);

        return this;
    }

    public boolean isValueSmallEnough() {
        return super.isValueSmallEnough();
    }
}
