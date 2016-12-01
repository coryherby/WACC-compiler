package wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser;

import antlr.WaccParser.AssignRhsContext;
import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.Symbol;
import wacc.SemanticErrorDetector.WaccTypes.PairElemType;
import wacc.SemanticErrorDetector.WaccTypes.PairType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

class NewpairAssignAnalyser {

    // Check assignment for Newpair
    public static void checkAssignement(
        Symbol variable, ExpressionContext leftExpressionContext,
        ExpressionContext rightExpressionContext) {

        Type expectedType = variable.getType();

        // Type has to be PairType
        if (!(expectedType instanceof PairType)) {
            ErrorReporter.reportNewpairAssignError(
                variable, leftExpressionContext, rightExpressionContext);
            return;
        }

        PairType expectedPairType = (PairType) expectedType;

        PairElemType expectedLeftPairElemType
                = expectedPairType.getLeftType();
        PairElemType expectedRightPairElemType
                = expectedPairType.getRightType();

        Type actualLeftPairElemType
            = TypeBuilder.buildTypeFromExpression(leftExpressionContext);
        Type actualRightPairElemType
            = TypeBuilder.buildTypeFromExpression(rightExpressionContext);

        if (!(expectedLeftPairElemType.equals(actualLeftPairElemType)
            && expectedRightPairElemType.equals(actualRightPairElemType))) {

            ErrorReporter.reportNewpairAssignError(
                variable, leftExpressionContext, rightExpressionContext);
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isNewpair(AssignRhsContext assignRhsContext) {
        return assignRhsContext.NEWPAIR() != null;
    }
}
