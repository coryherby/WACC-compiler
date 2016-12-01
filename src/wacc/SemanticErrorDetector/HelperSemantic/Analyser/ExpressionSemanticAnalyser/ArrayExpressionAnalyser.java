package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class ArrayExpressionAnalyser {

    // Checks if a given expression is a valid ARRAY expression
    public static boolean isArrayExpression(ExpressionContext expContext) {

        while (BracketExpressionAnalyser.hasBracket(expContext)) {
            expContext = BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext);
        }

        // Check the 2 cases, where array is an array elem or an identification
        if (expContext.arrayElement() != null) {

            return ArrayElemExpressionAnalyser
                .isArrayElemOfTypeArray(expContext.arrayElement());

        } else if (
            IdentificationExpressionAnalyser.hasIdentification(expContext)) {

            Type actualType = getActualType(expContext);
            return actualType != null
                    && actualType.getWaccType() == WaccType.ARRAY;
        }

        return false;
    }

    public static Type getActualType(ExpressionContext expContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        String arrayIdentification = expContext.IDENTIFICATION().getText();
        Symbol symbol = symbolTable.get(arrayIdentification);

        if (symbol == null) {
            return null;
        }

        return symbol.getType();
    }

}
