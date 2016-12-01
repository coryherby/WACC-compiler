package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser;
import antlr.WaccParser.ArrayElementContext;
import antlr.WaccParser.AssignLhsContext;
import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.StatementContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser.AssignementAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers.ExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ArrayElemExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.PairExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.HelperAssignLhsSemantic.ArrayElementHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelperAssignLhsSemantic.PairElementHelper;
import wacc.SemanticErrorDetector.WaccTypes.PairType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;

public class AssignmentHelper {

    public static void checkIsUndeclaredVariable(
        StatementContext statementContext) {

        Symbol symbol;
        if(ArrayElementHelper.isArrayElement(statementContext.assignLhs())) {

            symbol = MainWaccTable.getInstance().getSymbolTable().get(
                statementContext.assignLhs().arrayElement()
                    .IDENTIFICATION().getText());

        } else if(PairElementHelper.isPairElement
            (statementContext.assignLhs())) {

            symbol = MainWaccTable.getInstance().getSymbolTable().get(
                statementContext.assignLhs().pairElement().expression()
                    .IDENTIFICATION().getText());

        } else {

            symbol = MainWaccTable.getInstance().getSymbolTable().get(
                statementContext.assignLhs().IDENTIFICATION().getText());
        }

        if(symbol == null) {
            ErrorReporter.reportUndeclaredVariableAssignment(statementContext);
        }

    }

    public static void checkAssignement(StatementContext statementContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();

        String identification;
        AssignLhsContext assignLhsContext = statementContext.assignLhs();

        if (ArrayElementHelper.isArrayElement(assignLhsContext)) {
        	
            identification = assignLhsContext.arrayElement()
                .IDENTIFICATION().getText();

        } else if (PairElementHelper.isPairElement(assignLhsContext)) {
        	
            ExpressionContext expressionContext
                = assignLhsContext.pairElement().expression();

            if (expressionContext.IDENTIFICATION() == null) {
                return;
            }

            identification = expressionContext.IDENTIFICATION().getText();

        } else {
        	
            identification = assignLhsContext.IDENTIFICATION().getText();
        }

        Symbol symbol = symbolTable.get(identification);

        if (symbol == null) {
            return;
        }

        if (ArrayElementHelper.isArrayElement(assignLhsContext)) {
        	
            ArrayElementContext arrayElementContext
                = assignLhsContext.arrayElement();

            Type t = ArrayElemExpressionAnalyser
                .getActualType(arrayElementContext);

            if (t == null) {
                return;
            }

            AssignementAnalyser
                .checkAssignType(new Symbol(arrayElementContext.getText(), t),
                    statementContext.assignRhs());

        } else if (PairElementHelper.isPairElement(assignLhsContext)) {

            Type type = PairExpressionAnalyser.getActualType(
                assignLhsContext.pairElement().expression());

            if (!(type instanceof PairType)) {
                return;
            }

            Type expectedType;

            if (assignLhsContext.pairElement().FIRST() != null) {           	
                expectedType = ((PairType) type).getLeftType();
            } else {
                expectedType = ((PairType) type).getRightType();
            }

            AssignementAnalyser.checkAssignType(
                new Symbol(assignLhsContext.pairElement().getText(),
                    expectedType), statementContext.assignRhs());

        } else {
        	
            AssignementAnalyser
                .checkAssignType(symbol, statementContext.assignRhs());
        }
    }

    public static void optimizeConstant(StatementContext statementContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        TerminalNode identificationNode
            = statementContext.assignLhs().IDENTIFICATION();

        // Try to optimize code if can be simplified
        if(identificationNode != null) {

            String value = null;

            Symbol symbol = symbolTable.get(identificationNode.getText());

            if (symbol == null) {
                return;
            }

            if (isAssignmentExpression(statementContext)
                && ErrorReporter.hasNoError()) {

                value = ExpressionAnalyser.evaluateExpression(
                    statementContext.assignRhs().expression(0));
            }

            symbol.setValue(value);
        }
    }

    public static boolean isAssignmentStatement
        (StatementContext statementContext) {

        return statementContext.assignLhs() != null
            && statementContext.EQUAL_ASSIGNEMENT() != null;
    }

    private static boolean isAssignmentExpression(
        StatementContext statementContext) {

        WaccParser.AssignRhsContext assignRhsContext
            = statementContext.assignRhs();

        return assignRhsContext.expression() != null
            && assignRhsContext.expression(0) != null
            && assignRhsContext.NEWPAIR() == null
            && assignRhsContext.pairElement() == null;
    }
}
