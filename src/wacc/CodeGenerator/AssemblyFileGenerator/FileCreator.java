package wacc.CodeGenerator.AssemblyFileGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileCreator {

    private final static String DOT = "\\.";
    private final static String SLASH = "/";
    private final static String DOT_S = ".s";

    private PrintWriter file;

    public FileCreator(String fileName) {

        try {
            this.file = new PrintWriter(
                FileUtils.generateAssemblyFile(fileName),
                FileUtils.UTF_8_ENCODING);

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error creating file");
        }

    }

    public PrintWriter getFile() {
        return this.file;
    }

}

