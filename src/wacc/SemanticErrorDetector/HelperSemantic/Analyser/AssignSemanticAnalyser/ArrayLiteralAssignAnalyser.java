package wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser;

import antlr.WaccParser;
import antlr.WaccParser.ArrayLiteralContext;
import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ArrayElemExpressionAnalyser;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.ArrayType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

class ArrayLiteralAssignAnalyser {

    // Checks assignment for arrayLiterals
    public static void checkAssignement(
        Symbol variable, ArrayLiteralContext arrayLiteralContext) {

        Type expectedType = variable.getType();

        if (!(expectedType instanceof ArrayType)) {
            ErrorReporter.reportArrayLiteralAssignError(
                variable, arrayLiteralContext);
            return;
        }

        ArrayType expectedArrayType = (ArrayType) expectedType;

        // ArrayLiteral contains nested array
        if (expectedArrayType.hasNestedArrayType()) {
            ArrayType arrayTypeExpectedForAllArrayElement
                = expectedArrayType.getNestedArrayType();

            checkAllArrayElementsType(
                variable,
                arrayTypeExpectedForAllArrayElement,
                arrayLiteralContext);
            return;
        }

        Type typeExpectedForAllArrayElement = expectedArrayType.getNestedType();

        // Array Literal does not have nested array
        checkAllArrayElementsType(
            variable, typeExpectedForAllArrayElement, arrayLiteralContext);
    }

    // Function checking that all the elements in the array have the type
    //  expected (not an array type)
    
    private static void checkAllArrayElementsType(
        Symbol variable, Type typeExpected,
        ArrayLiteralContext arrayLiteralContext) {

        int i = 0;

        while (arrayLiteralContext.expression(i) != null) {

            ExpressionContext expressionContext
                = arrayLiteralContext.expression(i);

            Type arrayElementType
                = TypeBuilder.buildTypeFromExpression(expressionContext);

            // Return false if array elem hasn't got right type
            if (!typeExpected.equals(arrayElementType)) {
                ErrorReporter.reportArrayLiteralAssignError(
                    variable, arrayElementType, expressionContext);
                return;
            }

            i++;
        }
    }

    // Function checking that all the elements in the array have the type
    // expected (array type)
    private static void checkAllArrayElementsType(
        Symbol variable, ArrayType arrayTypeExpected,
        ArrayLiteralContext arrayLiteralContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();

        int i = 0;

        while (arrayLiteralContext.expression(i) != null) {

            ExpressionContext expressionContext
                = arrayLiteralContext.expression(i);

            if (expressionContext.arrayElement() != null) {

                if (!ArrayElemExpressionAnalyser.isArrayElemSameAsExpectedType(
                    expressionContext.arrayElement(), arrayTypeExpected)) {

                    ErrorReporter.reportArrayLiteralAssignError(variable,
                        arrayTypeExpected, expressionContext);
                }

                return;
            }

            // Return if no identification exists
            if (expressionContext.IDENTIFICATION() == null) {
                return;
            }

            String elementIdentification
                = expressionContext.IDENTIFICATION().getText();
            Symbol arrayElement = symbolTable.get(elementIdentification);

            // Check if type is equal to expected type
            if (!arrayElement.getType().equals(arrayTypeExpected)) {
                ErrorReporter.reportArrayLiteralAssignError(
                    variable, arrayTypeExpected,
                    arrayElement.getType(), expressionContext);
                return;
            }

            i++;
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isArrayLiteral(
        WaccParser.AssignRhsContext assignRhsContext) {

        return assignRhsContext.arrayLiteral() != null;
    }
}
