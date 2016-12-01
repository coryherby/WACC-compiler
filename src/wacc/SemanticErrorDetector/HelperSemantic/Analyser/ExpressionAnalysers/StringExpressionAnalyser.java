package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers;

import antlr.WaccParser.ExpressionContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic.ConditionStatementHelper;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;

public class StringExpressionAnalyser {

    public static String generateStringValueOf(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        TerminalNode stringLiteral = expression.STRING_LITERAL();
        TerminalNode bracketExpression = expression.OPEN_BRACKET();

        if (stringLiteral != null) {
            return evaluateStringLiteral(stringLiteral);

        } else if (bracketExpression != null) {
            return evaluateBracket(expression.expression(0));
        }

        throw new IncorrectExpressionFormat();
    }

    public static String generateStringValueOfForEqualOperators(
        ExpressionContext expression) throws IncorrectExpressionFormat {

        TerminalNode stringLiteral = expression.STRING_LITERAL();
        TerminalNode identification = expression.IDENTIFICATION();
        TerminalNode bracketExpression = expression.OPEN_BRACKET();

        if(stringLiteral != null) {
            return evaluateStringLiteral(stringLiteral);

        } else if (identification != null
            && !ConditionStatementHelper.isInWhileLoop) {
            return evaluateIdentification(identification);

        } else if(bracketExpression != null) {
            return evaluateBracket(expression.expression(0));
        }

        throw new IncorrectExpressionFormat();
    }

    private static String evaluateStringLiteral(TerminalNode stringLiteral) {

        return stringLiteral.getText();
    }

    private static String evaluateIdentification(TerminalNode identification)
        throws IncorrectExpressionFormat {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        Symbol symbol = symbolTable.get(identification.getText());

        if(symbol != null
            && symbol.getType().getWaccType() == WaccType.STRING) {
            String value = symbol.getValue();

            if(value != null) {

                return value;
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static String evaluateBracket(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        return generateStringValueOf(expression);
    }
}