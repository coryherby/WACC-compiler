package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser.StatementContext;
import wacc.SemanticErrorDetector.HelperSemantic.GeneralHelper;

public class NewScopeHelper {

    public static boolean isNewScopeStatement(
    	StatementContext statementContext) {

        return statementContext.BEGIN() != null;
    }
}