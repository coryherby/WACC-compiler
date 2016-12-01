package wacc.CodeGenerator.Commands.LabelCommand;

import wacc.CodeGenerator.Commands.Command;

public class LabelCommand implements Command {

    private String label;

    public LabelCommand(String label) {
        this.label = label;
    }

    @Override
    public String generateCommandRepresentation() {
        return label + ":";
    }
}
