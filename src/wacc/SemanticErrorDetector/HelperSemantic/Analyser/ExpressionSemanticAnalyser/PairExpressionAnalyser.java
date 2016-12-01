package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.NullType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class PairExpressionAnalyser {

    public static final WaccType CLASS_TYPE = WaccType.PAIR;

    // Checks if a given expression is a valid PAIR expression
    public static boolean isPairExpression(ExpressionContext expContext) {

        // Check the 2 cases, where pair is either null or an identification
        if (BracketExpressionAnalyser.hasBracket(expContext)) {

            return isPairExpression(BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext));

        } if (expContext.PAIR_LITERAL() != null) {
            return true;

        } else if (expContext.IDENTIFICATION() != null) {

            return IdentificationExpressionAnalyser.isIdentificationSameAsType(
                expContext, CLASS_TYPE);
        }

        return false;
    }

    public static Type getActualType(ExpressionContext expressionContext) {

        if (expressionContext.PAIR_LITERAL() != null) {
            return new NullType();
        }

        // Pair element must be an identification
        if (expressionContext.IDENTIFICATION() == null) {
            return null;
        }

        String pairIdentification
            = expressionContext.IDENTIFICATION().getText();

        SymbolTable s = MainWaccTable.getInstance().getSymbolTable();
        Symbol symbol = s.get(pairIdentification);

        if (symbol == null) {
            return null;
        }

        return symbol.getType();
    }
}
