package wacc.CodeGenerator.InternalRepresentation;

public class RepresentationFormatter {

    public static final int INDENT_ONCE = 1;
    public static final int INDENT_TWICE = 2;

    public static final String DEPTH_UNIT = "   ";

    public static String generateRepresentation(
        String depth, String mainName, String[]secondaryNames,
        String[] secondaryRepresentations) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n" + depth + mainName + " {");

        for (int i = 0; i < secondaryNames.length; i++) {
            sb.append("\n");
            sb.append("\n" + DEPTH_UNIT + depth + secondaryNames[i] + " {");
            sb.append("\n" + DEPTH_UNIT + secondaryRepresentations[i]);
            sb.append("\n" + DEPTH_UNIT + depth + "}");
        }

        sb.append("\n");

        sb.append(depth + "}");

        return sb.toString();
    }

    public static String generateRepresentation(
        String depth, String mainName, String mainRepresentation) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n" + depth + mainName + " {");
        sb.append("\n" + mainRepresentation);
        sb.append("\n" + depth + "}");

        return sb.toString();
    }

    public static String getDepthRepresentation(int depth) {

        String depthRepresentation = "";

        for (int i = 0; i < depth; i++) {
            depthRepresentation += DEPTH_UNIT;
        }

        return depthRepresentation;
    }
}
