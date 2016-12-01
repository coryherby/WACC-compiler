package wacc.SemanticErrorDetector.ErrorManager;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

class ErrorPrintHelper {

    public static void printInitialMessage (Token errorToken) {
        System.err.print("\n");
        System.err.print("Semantic Error at line " + errorToken.getLine()
            + ":" + (errorToken.getCharPositionInLine() + 1) + " -- ");
    }

    public static void printErrorDescription (String typeOfError,
                                              String errorDescription) {
    	
        System.err.print("<" + typeOfError+ "> " + errorDescription);
    }

    public static void printErrorLocation (Token errorToken, int startIndex,
                                           int stopIndex, int startErrorIndex,
                                           int stopErrorIndex) {

        System.out.print("\n");
        System.err.println("-----------------------YOUR CODE" +
            "-----------------------");
        printCode(errorToken, startIndex, stopIndex
            , startErrorIndex, stopErrorIndex);
        System.err.print("\n-------------------------" +
            "------------------------------");
    }

    public static void printFinalMessage () {
        System.out.print("\n");
    }

    private static void printCode(Token errorToken,
                                  int startIndex, int stopIndex,
                                  int startErrorIndex, int stopErrorIndex) {

        int numberOfLine = 0;
        CharStream charStream = errorToken.getInputStream();
        boolean errorTokenLine = false;
        int numberOfSpaceAtBeginningOfLine = 0;
        boolean LastLineError = true;
        int lineSize = 0;

        System.err.print((errorToken.getLine() + numberOfLine) + "  ");
        
        for(int i = startIndex; i <= stopIndex; i++) {
        	
            String currentString = charStream.getText(new Interval(i, i));
            
            if(i == startErrorIndex) {
            	
                errorTokenLine = true;
                LastLineError = true;
                System.err.print(currentString);
                
            } else if(currentString.equals("\n")) {
            	
                LastLineError = false;
                numberOfLine++;
                numberOfSpaceAtBeginningOfLine =
                    getNumberOfSpaceAtBeginningOfLine(
                        charStream, startIndex);
                
                if(errorTokenLine) {
                    if(i > stopErrorIndex) {
                        putUnderline(errorToken, lineSize,
                                startErrorIndex, stopErrorIndex);
                        errorTokenLine = false;
                    } else {
                        putUnderline(errorToken, lineSize,
                                startErrorIndex, i - 1);
                    }
                }
                
                lineSize = 0;
                System.err.print("\n" + (errorToken.getLine() + numberOfLine)
                    + "  ");
                
            } else if (currentString.equals(" ")) {
            	
                if(numberOfSpaceAtBeginningOfLine == 0) {
              
                    System.err.print(currentString);
                    
                    if(!errorTokenLine) {
                        lineSize++;
                    }
                    
                } else {
                    numberOfSpaceAtBeginningOfLine--;
                }
                
            } else {
            	
                numberOfSpaceAtBeginningOfLine = 0;
                
                if(!errorTokenLine) {
                    lineSize++;
                }
                
                System.err.print(currentString);
            }
        }
        
        if(LastLineError) {
            if(errorTokenLine) {
                putUnderline(errorToken, lineSize,
                        startErrorIndex, stopErrorIndex);
            }
        }

    }

    private static int getNumberOfSpaceAtBeginningOfLine(
        CharStream charStream, int startIndex) {

        int result = 0;
        
        while(!(charStream.getText(new Interval(startIndex, startIndex))
            .equals("\n")) && charStream.getText(new Interval(startIndex,
                startIndex)) != null) {
        	
            if(charStream.getText(new Interval(startIndex, startIndex))
                .equals(" ")) {
            	
                result++;
                
            } else {
            	
                result = 0;
            }
            startIndex--;
        }
        return result;
    }

    private static void putUnderline(
        Token errorToken, int lineSize,
            int startErrorIndex, int stopErrorIndex) {

        System.err.print("\n");
        System.err.print("  ");
        
        for(int i = errorToken.getLine(); i != 0; i = i / 10) {
            System.err.print(" ");
        }
        
        for(int i = 0; i < lineSize; i++) {
            System.err.print(" ");
        }
        
        for(int i = 0; i < stopErrorIndex - startErrorIndex + 1; i++) {
            System.err.print("^");
        }
    }

    public static String generateErrorMessageForTypeError(
        String expectedType, String actualType) {

        String result = "Type mismatch"
            + "\n"
            + "Expected type : " + expectedType
            + "\n"
            + "Actual type : " + actualType;
        return result;
    }

    public static void printError(int startIndex, 
    							  int stopIndex,
                                  Token errorTokenStart, 
                                  String typeOfError,
                                  String errorDescription) {

        generalPrintError(startIndex, stopIndex, errorTokenStart
            , errorTokenStart.getStartIndex()
            , errorTokenStart.getStopIndex(), typeOfError
            , errorDescription);
    }

    public static void printError(int startIndex, 
    							  int stopIndex,
                                  ParserRuleContext parserRuleContext,
                                  String typeOfError, 
                                  String errorDescription) {

        generalPrintError(startIndex, stopIndex, parserRuleContext.getStart()
            , parserRuleContext.getStart().getStartIndex()
            , parserRuleContext.getStop().getStopIndex(), typeOfError
            , errorDescription);
    }

    public static void printError(int startIndex, 
    							  int stopIndex,
                                  Token errorTokenStart, 
                                  int startErrorIndex,
                                  int stopErrorIndex,
                                  String typeOfError,
                                  String errorDescription) {

        generalPrintError(startIndex, stopIndex, errorTokenStart,
            startErrorIndex, stopErrorIndex, typeOfError, errorDescription);
    }

    private static void generalPrintError(int startIndex, 
    									  int stopIndex,
                                          Token errorTokenStart, 
                                          int startErrorIndex,
                                          int stopErrorIndex,
                                          String typeOfError,
                                          String errorDescription) {

        ErrorPrintHelper.printInitialMessage(errorTokenStart);
        ErrorPrintHelper.printErrorDescription(typeOfError
            , errorDescription);
        ErrorPrintHelper.printErrorLocation(
            errorTokenStart, startIndex, stopIndex,
                startErrorIndex, stopErrorIndex);
        ErrorPrintHelper.printFinalMessage();
    }

}