package wacc.SemanticErrorDetector.HelperSemantic.HelperAssignLhsSemantic;

import antlr.WaccParser.AssignLhsContext;


public class ArrayElementHelper {

    public static boolean isArrayElement
            (AssignLhsContext assignLhsContext) {

        return assignLhsContext.arrayElement() != null;
    }
}
