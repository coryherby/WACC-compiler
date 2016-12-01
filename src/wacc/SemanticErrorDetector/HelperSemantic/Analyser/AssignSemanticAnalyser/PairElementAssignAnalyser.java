package wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser;

import antlr.WaccParser;
import antlr.WaccParser.PairElementContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.*;

class PairElementAssignAnalyser {

    // Checks assignment for pairElements
    public static void checkAssignement(
        Symbol variable, PairElementContext pairElementContext) {

        Type expectedType = variable.getType();

        // Check that an identification exists
        if (pairElementContext.expression().IDENTIFICATION() == null) {
            ErrorReporter.reportPairElementAssignError(
                variable, pairElementContext, WaccType.PAIR);
            return;
        }

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();

        Symbol symbol = symbolTable.get(
            pairElementContext.expression().IDENTIFICATION().getText());

        if (symbol == null) {
            return;
        }

        Type identificationType = symbol.getType();

        // Check that the type corresponds to a PairType
        if (!(identificationType instanceof PairType)) {
            ErrorReporter.reportPairElementAssignError(
                variable, pairElementContext, WaccType.PAIR);
            return;
        }

        PairType pairType = (PairType) identificationType;

        // Check that the first or second part of pair has right type
        if (pairElementContext.FIRST() != null) {

            PairElemType leftPairElem = pairType.getLeftType();

            if (!expectedType.equals(leftPairElem)) {
                ErrorReporter.reportPairElementAssignError(
                    symbol, pairElementContext, leftPairElem);
            }

        } else if (pairElementContext.SECOND() != null) {

            PairElemType rightPairElem = pairType.getRightType();

            if (!expectedType.equals(rightPairElem)) {
                ErrorReporter.reportPairElementAssignError(
                    symbol, pairElementContext, rightPairElem);
            }
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isPairElement(
        WaccParser.AssignRhsContext assignRhsContext) {

        return assignRhsContext.pairElement() != null;
    }
}
