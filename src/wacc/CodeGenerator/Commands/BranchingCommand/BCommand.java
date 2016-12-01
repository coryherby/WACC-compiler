package wacc.CodeGenerator.Commands.BranchingCommand;

import wacc.CodeGenerator.Commands.ConditionCode;

public class BCommand extends BranchingCommand {

    public BCommand(ConditionCode conditionField, String label) {
        super(conditionField, label);
    }

    @Override
    protected String getCommandCode() {
        return "B";
    }
}
