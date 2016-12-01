package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser.ArrayElementContext;
import antlr.WaccParser.AssignLhsContext;
import antlr.WaccParser.StatementContext;
import antlr.WaccParser.PairElementContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ArrayElemExpressionAnalyser;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.HelperSemantic.HelperAssignLhsSemantic.ArrayElementHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelperAssignLhsSemantic.PairElementHelper;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.*;

public class ReadHelper {

    public static void checkReadCorrectType(StatementContext statementContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        AssignLhsContext assignLhsContext = statementContext.assignLhs();

        boolean isValid;

        // Check the 3 cases (read in array elem, pair elem and in variable)
        if (ArrayElementHelper.isArrayElement(assignLhsContext)) {

            ArrayElementContext arrayElementContext
                = assignLhsContext.arrayElement();

            isValid = ArrayElemExpressionAnalyser.isArrayElemSameAsExpectedType(
                arrayElementContext, WaccType.CHAR)
                    || ArrayElemExpressionAnalyser.
                        isArrayElemSameAsExpectedType(
                            arrayElementContext, WaccType.INT);

            if (!isValid) {
            	
                ErrorReporter.reportReadWrongType(statementContext,
                    ArrayElemExpressionAnalyser.getActualType(
                        arrayElementContext));
            }

        } else if (PairElementHelper.isPairElement(assignLhsContext)) {

            PairElementContext pairElementContext
                = assignLhsContext.pairElement();

            if (pairElementContext.expression().IDENTIFICATION() == null) {
                return;
            }

            String identification = pairElementContext.expression()
                .IDENTIFICATION().getText();

            Symbol symbol = symbolTable.get(identification);

            if (symbol == null) {
                return;
            }

            if (!(symbol.getType() instanceof PairType)) {
                return;
            }

            PairType pairType = (PairType) symbol.getType();
            PairElemType elemType;

            if (pairElementContext.FIRST() != null) {
            	
                elemType = pairType.getLeftType();

            } else {
            	
                elemType = pairType.getRightType();
            }

            isValid = elemType != null
                && (elemType.equals(new BaseType(WaccType.CHAR))
                    || elemType.equals(new BaseType(WaccType.INT)));

            if (!isValid) {
                ErrorReporter.reportReadWrongType(statementContext, elemType);
            }

        } else {

            String identification = assignLhsContext
                .IDENTIFICATION().getText();

            Symbol symbol = symbolTable.get(identification);

            if (symbol == null) {
                return;
            }

            Type readType = symbol.getType();

            isValid = readType.getWaccType() == WaccType.INT
                || readType.getWaccType() == WaccType.CHAR;

            if (!isValid) {
                ErrorReporter.reportReadWrongType(statementContext, readType);
            }
        }
    }

    // Put the variable value to NULL to tell the compiler that the value is
    // unknown because of the Read statement
    public static void updateVariableValue(StatementContext statementContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        AssignLhsContext assignLhsContext = statementContext.assignLhs();

        if (ErrorReporter.hasNoError()
            && assignLhsContext.IDENTIFICATION() != null) {

            String identifier = assignLhsContext.IDENTIFICATION().getText();
            Symbol variable = symbolTable.get(identifier);

            variable.setValue(null);
        }
    }

    public static boolean readStatement(StatementContext statementContext) {

        return statementContext.READ() != null;
    }
}
