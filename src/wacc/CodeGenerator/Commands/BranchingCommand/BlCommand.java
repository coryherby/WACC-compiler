package wacc.CodeGenerator.Commands.BranchingCommand;

import wacc.CodeGenerator.Commands.ConditionCode;

public class BlCommand extends BranchingCommand {

    public BlCommand(ConditionCode conditionField, String label) {
        super(conditionField, label);
    }

    @Override
    protected String getCommandCode() {
        return "BL";
    }

}
