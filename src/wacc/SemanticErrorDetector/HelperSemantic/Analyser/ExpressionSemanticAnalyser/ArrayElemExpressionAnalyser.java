package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ArrayElementContext;
import antlr.WaccParser.ExpressionContext;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.ArrayType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class ArrayElemExpressionAnalyser {

    // Check if an array elem has the same type as the one given in argument
    public static boolean isArrayElemSameAsExpectedType(
        ArrayElementContext arrayElementContext, Type expectedType) {

        Type actualType = getActualType(arrayElementContext);
        return actualType != null && actualType.equals(expectedType);
    }

    // Check if an array elem has the same type as the one given in argument
    public static boolean isArrayElemSameAsExpectedType(
        ArrayElementContext arrayElementContext, WaccType expectedType) {

        Type actualType = getActualType(arrayElementContext);
        return actualType != null && actualType.getWaccType() == expectedType;
    }

    // Check if an array elem has type array
    public static boolean isArrayElemOfTypeArray(
        ArrayElementContext arrayElementContext) {

        Type actualType = getActualType(arrayElementContext);
        return actualType != null && actualType.getWaccType() == WaccType.ARRAY;
    }

    public static Type getActualType(ArrayElementContext arrayElementContext) {

        SymbolTable s = MainWaccTable.getInstance().getSymbolTable();
        String arrayIdentification
            = arrayElementContext.IDENTIFICATION().getText();
        Symbol symbol = s.get(arrayIdentification);

        // No matching array identification was found in the program
        if (symbol == null) {
            return null;
        }

        Type actual = symbol.getType();

        int i = 0;

        while (arrayElementContext.expression(i) != null) {

            if (!IntExpressionAnalyser.isIntExpression(
                arrayElementContext.expression(i))) {
                return null;
            }

            if (!(actual instanceof ArrayType)) {
                return null;
            }

            ArrayType arrayType = (ArrayType) actual;
            actual = arrayType.getNestedType();

            i++;
        }

        return actual;
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isArrayElem(ExpressionContext expContext) {

        return expContext.arrayElement() != null;
    }
}
