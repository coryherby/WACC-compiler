package wacc;

import antlr.WaccLexer;
import antlr.WaccParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import wacc.CodeGenerator.CodeGenerator;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.WaccParserSemanticErrorVisitor;
import wacc.SyntaxErrorDetector.WaccParserSyntaxErrorListener;
import wacc.SyntaxErrorDetector.WaccParserSyntaxErrorStrategy;
import wacc.SyntaxErrorDetector.WaccParserSyntaxErrorVisitor;

public class WaccCompiler {
	
	public final static int SUCCESS_EXIT_CODE = 0;
    public final static int SYNTAX_ERROR_EXIT_CODE = 100;
    public final static int SEMANTIC_ERROR_EXIT_CODE = 200;

    public static void main(String[] args) throws Exception {

        String filename = args[0];

        // Create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(System.in);

        // Create a lexer that feeds off of input CharStream
        WaccLexer lexer = new WaccLexer(input);

        // Create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create a parser that feeds off the tokens buffer
        WaccParser parser = new WaccParser(tokens);

        // Set new Syntax Error Strategy to configure syntax error messages
        parser.setErrorHandler(new WaccParserSyntaxErrorStrategy());

        // Remove original listeners
        parser.removeErrorListeners();

        // Add new created Error Listener
        parser.addErrorListener(new WaccParserSyntaxErrorListener());

        printCompiling();

        // Begin parsing at init rule
        ParseTree tree = parser.program();

        // Syntax Error Visitor checking additional syntax errors
        new WaccParserSyntaxErrorVisitor(parser).visit(tree);

        reportSyntaxErrors(parser);

        // Semantic Visitor checking semantic errors
        new WaccParserSemanticErrorVisitor().visit(tree);

        reportSemanticErrors();

        // Code Generation using codeGenerator
        new CodeGenerator(filename).visit(tree);

        reportSuccess();
    }

    private static void printCompiling() {
        System.out.println("\n" + "--- COMPILING ---");
    }

	private static void reportSuccess() {
		System.out.println("Compilation successful!");
        System.exit(SUCCESS_EXIT_CODE);
	}

	private static void reportSemanticErrors() {
		if (ErrorReporter.getSemanticErrorCounter() != 0) {
            System.err.println("\n" + ErrorReporter.getSemanticErrorCounter() +
                " semantic error(s) detected, no further" +
                    " compilation attempted." + "\n");
            System.exit(SEMANTIC_ERROR_EXIT_CODE);
        }
	}

	private static void reportSyntaxErrors(WaccParser parser) {
		if (parser.getNumberOfSyntaxErrors() != 0){
            System.err.println(parser.getNumberOfSyntaxErrors() +
                " syntax error(s) detected, no further" +
                    " compilation attempted.");
            System.exit(SYNTAX_ERROR_EXIT_CODE);
        }
	}
    
}