package wacc.SyntaxErrorDetector;

import org.antlr.v4.runtime.*;

public class WaccParserSyntaxErrorListener extends BaseErrorListener {

    private boolean errorAlreadyDetected;

    private final int INT_TO_GET_FIRST_LINE_TO_PRINT = 2;

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, 
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {

        if (!errorAlreadyDetected){
            System.err.println("Syntax errors detected during compilation! " +
                "Exit code 100 returned. \n");
            errorAlreadyDetected = true;
        }

        System.err.println("Syntax Error detected at " + line + ":" +
            charPositionInLine + " -- " + msg);

        printProgram(recognizer, (Token) offendingSymbol, line,
            charPositionInLine);

    }

    // Print the program
    private void printProgram(Recognizer<?, ?> recognizer,
                              Token offendingToken,
                              int line, 
                              int charPositionInLine) {

        String[] lines = getLines(recognizer);

        int lineNumberOfError = getNumberDigitInLine(line);

        System.err.println("-----------------------" +
            "YOUR CODE-----------------------");

        for (int j = line - INT_TO_GET_FIRST_LINE_TO_PRINT; j < line + 1; j++) {
        	
            int lineNumberOfLine = getNumberDigitInLine(j+1);
            
            if (j == lines.length){
            	
                break;
                
            } else if (!(lines[j].equals("")) && (j < lines.length - 1)){

                if (j == line - 1) {

                    underlineError(recognizer, offendingToken,
                        line, charPositionInLine);

                } else {

                    printLineNumber(j + 1);

                    if (lineNumberOfError != lineNumberOfLine) {
                        printNumberEmptySpace(lineNumberOfLine - 1);
                    }

                    lines[j] = replaceTabsBySpaces(lines[j]);
                    System.err.println(lines[j]);
                }
                
                if (lines[j].endsWith("end")) {
                    break;
                }
            }
        }
        System.err.println("--------------------------" +
            "-----------------------------");
        System.err.println();

    }

    // Error report with underlined syntax error
    private void underlineError(Recognizer<?, ?> recognizer,
                                Token offendingToken,
                                int line, 
                                int charPositionInLine) {
    	
        String[] lines = getLines(recognizer);
        String errorLine = lines[line - 1];
        boolean containedTab = errorLine.contains("\t");

        printLineNumber(line);
        errorLine = replaceTabsBySpaces(errorLine);
        System.err.println(errorLine);

        int numberDigitInLine = getNumberDigitInLine(line);
        
        if (containedTab) {
            System.err.print(" ");
        }

        if (!errorLine.isEmpty()) {

            printNumberEmptySpace(charPositionInLine + numberDigitInLine + 1);
            int start = offendingToken.getStartIndex();
            int stop = offendingToken.getStopIndex();

            if (start >= 0 && stop >= 0) {

                // underlines the error
                for (int i = start; i <= stop; i++) {
                    System.err.print("^");
                }
                System.err.println();

            }

        }
    }


    private void printNumberEmptySpace(int numberOfSpaces) {
        for (int i = 0; i < numberOfSpaces; i++) {
            System.err.print(" ");
        }
    }

    private String replaceTabsBySpaces(String line) {
        if (line.contains("\t")) {
            return line.replace("\t", "  ");
        }
        return line;
    }

    private void printLineNumber(int j) {
        System.err.print(j + " ");
    }

    private int getNumberDigitInLine(int j) {
        return (int)(Math.log10(j)+1);
    }

    private String[] getLines(Recognizer<?, ?> recognizer) {
        CommonTokenStream tokens
            = (CommonTokenStream) recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        return input.split("\n");
    }

}