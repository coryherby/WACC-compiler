package wacc.CodeGenerator.CodeGenerationTools;

public class BranchLabelGenerator {

    private static final String LABEL_NAME = "L";

    private static int labelCount = 0;

    // Don't want to create instances of this class
    private BranchLabelGenerator() {}

    public static String getLabelAndIncrementCounter() {
        String label = LABEL_NAME + labelCount;
        labelCount++;
        return label;
    }
}
