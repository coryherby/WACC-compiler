package wacc.SemanticErrorDetector.HelperSemantic.HelperAssignLhsSemantic;

import antlr.WaccParser.AssignLhsContext;

public class PairElementHelper {

    public static boolean isPairElement(AssignLhsContext assignLhsContext) {

        return assignLhsContext.pairElement() != null;
    }
}
