package wacc.CodeGenerator.Commands.MutliplyCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class SMullCommand extends MultiplyCommand {

    public SMullCommand(ConditionCode conditionField,
                        Register destinationRegisterMSB,
                        Register destinationRegisterLSB,
                        Register sourceRegister1,
                        Register sourceRegister2) {


        super(conditionField, destinationRegisterMSB, destinationRegisterLSB,
            sourceRegister1, sourceRegister2);
    }

    @Override
    protected String getCommandCode() {
        return "SMULL";
    }

}
