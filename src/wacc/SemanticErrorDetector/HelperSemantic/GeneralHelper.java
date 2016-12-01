package wacc.SemanticErrorDetector.HelperSemantic;

import antlr.WaccParser;
import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.StatementContext;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers.ExpressionAnalyser;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.SymbolTable;

import java.util.LinkedList;
import java.util.List;

public class GeneralHelper {

    public static <E extends ParserRuleContext, T extends ParserRuleContext>
    T contextFromInitialContext(E initial, Class<T> returned) {

        ParserRuleContext resultContext = initial;
        while (!(returned.isInstance(resultContext))) {
            resultContext = resultContext.getParent();
            if(resultContext == null) {
                return null;
            }
        }
        return returned.cast(resultContext);
    }

    public static boolean isAssignmentStatement(
        ParserRuleContext parserRuleContext) {

        return StatementContext.class.isInstance(parserRuleContext);
    }

    public static void addNewScope() {

        SymbolTable child = new SymbolTable(
            MainWaccTable.getInstance().getSymbolTable(), new LinkedList<>());

        MainWaccTable.getInstance().getSymbolTable().addChildren(child);

        MainWaccTable.getInstance().setSymbolTable(child);
    }

    public static void returnToParentScope() {

        SymbolTable parent = MainWaccTable.getInstance().getSymbolTable()
            .getParentScope();

        MainWaccTable.getInstance().setSymbolTable(parent);
    }


    public static void codeGeneratorAddToLinkedReferenceForInitialisation(
        StatementContext ctx) {

        WaccParser.ExpressionContext expression = ctx.assignRhs().expression(0);
        TerminalNode newPairAssignment = ctx.assignRhs().NEWPAIR();
        ctx.assignRhs().NEWPAIR();
        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();

        if(expression != null && newPairAssignment == null) {
            if(expression.IDENTIFICATION() != null) {
                String variable1 = ctx.IDENTIFICATION().getText();
                String variable2 = expression.IDENTIFICATION().getText();
                symbolTable.addLinkedReference(variable1, variable2);
            }
        }
    }

    public static void codeGeneratorAddToLinkedReferenceForAssignement(
        StatementContext ctx) {

        WaccParser.ExpressionContext expression = ctx.assignRhs().expression(0);
        TerminalNode newPairAssignment = ctx.assignRhs().NEWPAIR();
        ctx.assignRhs().NEWPAIR();
        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        WaccParser.AssignLhsContext assignLhs = ctx.assignLhs();

        if(expression != null && newPairAssignment == null) {
            if(expression.IDENTIFICATION() != null
                && assignLhs.IDENTIFICATION() != null) {
                String variable1 = assignLhs.IDENTIFICATION().getText();
                String variable2 = expression.IDENTIFICATION().getText();
                symbolTable
                    .addLinkedReferenceForAssignement(variable1, variable2);
            }
        }
    }

    /*
      Recreate node for constant flow optimisation
    */

    public static ExpressionContext constantpropagationAnalysis(
        ExpressionContext oldExpression) {

        String updatedValue =
            ExpressionAnalyser.evaluateExpression(oldExpression);

        if (updatedValue == null) {
            return oldExpression;
        }

        ExpressionContext newExpressionContext
            = new ExpressionContext(oldExpression.getParent(), 0);

        Type type = TypeBuilder.buildTypeFromExpression(oldExpression);

        switch (type.getWaccType()) {
            case INT:
                WaccParser.IntLiteralContext intLiteralContext
                    = new WaccParser.IntLiteralContext(newExpressionContext, 0);

                CommonToken intToken = new CommonToken(WaccParser.INTEGER);
                intToken.setText(updatedValue);

                intLiteralContext.addChild(intToken);
                newExpressionContext.addChild(intLiteralContext);

                break;

            case BOOL:
                WaccParser.BoolLiteralContext boolLiteralContext
                    = new WaccParser.BoolLiteralContext(
                    newExpressionContext, 0);

                CommonToken boolToken;
                if(Boolean.valueOf(updatedValue)) {
                    boolToken = new CommonToken(WaccParser.TRUE);
                } else {
                    boolToken = new CommonToken(WaccParser.FALSE);
                }
                boolToken.setText(updatedValue);

                boolLiteralContext.addChild(boolToken);
                newExpressionContext.addChild(boolLiteralContext);

                break;

            case CHAR:
                CommonToken charToken
                    = new CommonToken(WaccParser.CHAR_LITERAL);
                charToken.setText(updatedValue);

                newExpressionContext.addChild(charToken);

                break;

            case STRING:
                CommonToken stringToken
                    = new CommonToken(WaccParser.STRING_LITERAL);
                stringToken.setText(updatedValue);

                newExpressionContext.addChild(stringToken);

                break;

            default:
                return oldExpression;
        }

        return replaceExpressionContext(oldExpression, newExpressionContext);
    }

    private static ExpressionContext replaceExpressionContext(
        ExpressionContext oldExpression, ExpressionContext newExpression){

        List<ParseTree> parentChildren = oldExpression.getParent().children;

        int index = parentChildren.indexOf(oldExpression);
        parentChildren.remove(index);
        newExpression.parent = oldExpression.getParent();
        parentChildren.add(index, newExpression);

        return newExpression;
    }
}