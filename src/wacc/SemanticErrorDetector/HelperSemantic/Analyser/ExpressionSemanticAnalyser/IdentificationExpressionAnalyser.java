package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class IdentificationExpressionAnalyser {

    public static boolean isIdentificationSameAsType(
        ExpressionContext expressionContext, WaccType t) {

        if (BracketExpressionAnalyser.hasBracket(expressionContext)) {
            return isIdentificationSameAsType(BracketExpressionAnalyser
                .getExpressionInsideBrackets(expressionContext), t);
        }

        // expression must be an identification
        if (expressionContext.IDENTIFICATION() == null) {
            return false;
        }

        String pairIdentification
            = expressionContext.IDENTIFICATION().getText();

        SymbolTable s = MainWaccTable.getInstance().getSymbolTable();
        Symbol symbol = s.get(pairIdentification);

        return symbol != null && symbol.getType().getWaccType() == t;
    }

    /*
      WaccTypes identification functions
    */

    public static boolean hasIdentification(ExpressionContext expContext) {
        return expContext.IDENTIFICATION() != null;
    }
}
