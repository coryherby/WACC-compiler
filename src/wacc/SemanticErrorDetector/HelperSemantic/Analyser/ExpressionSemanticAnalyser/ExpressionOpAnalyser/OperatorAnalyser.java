package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser;

import antlr.WaccParser.ExpressionContext;

public class OperatorAnalyser {

    public static boolean areOperatorExpressionValid(
        ExpressionContext expContext) {

        // Forgets all the brackets
        while (BracketExpressionAnalyser.hasBracket(expContext)) {
            expContext = BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext);
        }

        // Check for binary operation that don't apply
        if (BinOpExpressionAnalyser.hasBinOp1or2(expContext)) {
            return BinOpExpressionAnalyser
            			.isBinOpPrecedence1or2ExpressionInt(expContext);

        } else if (BinOpExpressionAnalyser.hasBinOp3(expContext)) {
            return BinOpExpressionAnalyser
                        .isBinOpPrecedence3ExpressionBool(expContext);

        } else if (BinOpExpressionAnalyser.hasBinOp4(expContext)) {
            return BinOpExpressionAnalyser
                        .isBinOpPrecedence4ExpressionBool(expContext);

        } else if (BinOpExpressionAnalyser.hasBinOp5or6(expContext)) {
            return BinOpExpressionAnalyser
                        .isBinOpPrecedence5or6ExpressionBool(expContext);

        } else if (UnaryOpAnalyser.isUnaryOp(expContext)) {

            return UnaryOpAnalyser.isUnaryExpressionChar(expContext)
                || UnaryOpAnalyser.isUnaryExpressionInt(expContext)
                || UnaryOpAnalyser.isUnaryExpressionBool(expContext);
        }

        return true;
    }
}
