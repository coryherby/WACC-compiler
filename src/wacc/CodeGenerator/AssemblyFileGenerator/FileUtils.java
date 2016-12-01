package wacc.CodeGenerator.AssemblyFileGenerator;

public class FileUtils {

    public final static String DOT = "\\.";
    public final static String SLASH = "/";

    public final static String DOT_S_EXTENSION = ".s";
    public final static String UTF_8_ENCODING = "UTF-8";

    public static String generateAssemblyFile(String filename) {
        return getFileBasename(filename) + DOT_S_EXTENSION;
    }

    private static String getFileBasename(String fileName) {
        // Remove the dot extension
        String[] tokens = fileName.split(DOT);

        // Remove the directories before
        tokens = tokens[0].split(SLASH);

        // Get the last split token containing the basename
        return tokens[tokens.length-1];
    }
}
