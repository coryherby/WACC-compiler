package wacc.CodeGenerator.Commands.BranchingCommand;


import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;

public abstract class BranchingCommand implements Command {

    private ConditionCode conditionField;
    private String label;

    public BranchingCommand(ConditionCode conditionField, String label) {
        this.conditionField = conditionField;
        this.label = label;
    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);
        return getCommandCode() + conditionCode + " " + label;
    }

    protected abstract String getCommandCode();

    public String getLabel() {
        return label;
    }
}
