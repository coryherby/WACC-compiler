package wacc.CodeGenerator.Commands.MutliplyCommand;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public abstract class MultiplyCommand implements Command {

    private ConditionCode conditionField;
    private Register destinationRegisterMSB;
    private Register destinationRegisterLSB;
    private Register sourceRegister1;
    private Register sourceRegister2;

    public MultiplyCommand(ConditionCode conditionField,
                           Register destinationRegisterMSB,
                           Register destinationRegisterLSB,
                           Register sourceRegister1,
                           Register sourceRegister2) {

        this.conditionField = conditionField;
        this.destinationRegisterMSB = destinationRegisterMSB;
        this.destinationRegisterLSB = destinationRegisterLSB;
        this.sourceRegister1 = sourceRegister1;
        this.sourceRegister2 = sourceRegister2;
    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);

        return getCommandCode() + conditionCode + " " + destinationRegisterMSB
            + ", " + destinationRegisterLSB + ", " + sourceRegister1 + ", "
            + sourceRegister2;
    }

    protected abstract String getCommandCode();
}
