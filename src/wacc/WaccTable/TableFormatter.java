package wacc.WaccTable;

import java.util.HashMap;
import java.util.Set;

public class TableFormatter {

    public static final int INDENT_ONCE = 1;
    public static final int INDENT_TWICE = 2;

    public static final String DEPTH_UNIT = "   ";

    public static String generateSymbolTableRepresentation(
        int depthCount, int numberOfChildren, HashMap<String, Symbol> variables,
        String childRepresentation, int childToVisitNumber,
        int totalOffset, int currentOffset) {

        String depth = getDepthRepresentation(depthCount);

        StringBuilder sb = new StringBuilder();

        sb.append("\n" + depth + "SymbolTable" + " {");

        // Print the table's state
        sb.append("\n" + DEPTH_UNIT + depth
            + "NumberOfChildren: " + numberOfChildren);
        sb.append("\n" + DEPTH_UNIT + depth
            + "ChildToVisitNumber: " + childToVisitNumber);
        sb.append("\n" + DEPTH_UNIT + depth
            + "TotalOffset: " + totalOffset);
        sb.append("\n" + DEPTH_UNIT + depth
            + "CurrentOffset: " + currentOffset);

        // Print the table's variables
        sb.append("\n");
        sb.append("\n" + DEPTH_UNIT + depth + "VARIABLES:");
        sb.append("\n" + generateVariablesRepresentation(depth, variables));

        // Print the children representation
        sb.append("\n");
        sb.append("\n" + DEPTH_UNIT + depth + "CHILDREN REPRESENTATION:");
        sb.append("\n" + DEPTH_UNIT + childRepresentation);

        sb.append("\n");
        sb.append(depth + "}");

        return sb.toString();
    }

    private static String getDepthRepresentation(int depth) {

        String depthRepresentation = "";

        for (int i = 0; i < depth; i++) {
            depthRepresentation += DEPTH_UNIT;
        }

        return depthRepresentation;
    }

    private static String generateVariablesRepresentation(
        String depth, HashMap<String, Symbol> variables) {

        Set<String> variableNames = variables.keySet();

        StringBuilder sb = new StringBuilder();

        for (String variableName : variableNames) {
            Symbol variable = variables.get(variableName);
            sb.append("\n" + DEPTH_UNIT + depth +
                variable.generateSymbolRepresentation());
        }

        return sb.toString();
    }
}
