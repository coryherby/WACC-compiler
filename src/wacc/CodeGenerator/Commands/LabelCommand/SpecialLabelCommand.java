package wacc.CodeGenerator.Commands.LabelCommand;

import wacc.CodeGenerator.Commands.Command;

public class SpecialLabelCommand implements Command {

    private static final String INDENTATION = " ";

    public enum SpecialLabel {
        LTORG("ltorg"),
        DATA("data"),
        WORD("word"),
        ASCII("ascii"),
        TEXT("text"),
        GLOBAL_MAIN("global main");

        private String label;

        SpecialLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private SpecialLabel specialLabel;
    private String ascii;
    private int word;

    public SpecialLabelCommand(SpecialLabel specialLabel) {
        this.specialLabel = specialLabel;
        this.ascii = null;
        this.word = -1;

        switch (specialLabel) {
            case WORD:
            case ASCII:
                throw new RuntimeException("Wrong constructor used");
        }
    }

    public SpecialLabelCommand(String ascii) {
        this.specialLabel = SpecialLabel.ASCII;
        this.ascii = ascii;
        this.word = -1;
    }

    public SpecialLabelCommand(int word) {
        this.specialLabel = SpecialLabel.WORD;
        this.ascii = null;
        this.word = word;
    }

    public SpecialLabel getSpecialLabel() {
        return specialLabel;
    }

    @Override
    public String generateCommandRepresentation() {
        String representation = specialLabel.toString();

        switch (specialLabel) {
            case ASCII:
                representation += INDENTATION + ascii;
                break;
            case WORD:
                representation += INDENTATION + word;
                break;
        }

        return "." + representation;
    }
}
