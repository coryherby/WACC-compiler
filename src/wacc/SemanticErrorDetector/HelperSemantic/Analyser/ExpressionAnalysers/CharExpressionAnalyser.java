
package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers;

import antlr.WaccParser.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic.ConditionStatementHelper;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;

public class CharExpressionAnalyser {

    public static String generateCharValueOf(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        TerminalNode charLiteral = expression.CHAR_LITERAL();
        TerminalNode identification = expression.IDENTIFICATION();
        UnaryOperatorContext unaryOperator = expression.unaryOperator();
        TerminalNode bracketExpression = expression.OPEN_BRACKET();

        if(charLiteral != null) {

            return evaluateCharLiteral(charLiteral);
        } else if(identification != null
            && !ConditionStatementHelper.isInWhileLoop) {

            return evaluateIdentification(identification);
        } else if(unaryOperator != null) {

            TerminalNode chrOperator = unaryOperator.CHR_OPERATOR();
            if(chrOperator != null) {
                return evaluateUnaryOperator(expression, unaryOperator);
            }
        } else if(bracketExpression != null) {

            return evaluateBracket(expression.expression(0));
        }

        throw new IncorrectExpressionFormat();
    }

    private static String evaluateCharLiteral(TerminalNode charLiteral)
        throws IncorrectExpressionFormat {

        String character = charLiteral.getText();
        if(character.charAt(0) == '\'' && character.length() == 3
            && character.charAt(2) == '\'') {

            return character;
        }

        throw new IncorrectExpressionFormat();
    }

    private static String evaluateIdentification(TerminalNode identification)
        throws IncorrectExpressionFormat {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        Symbol symbol = symbolTable.get(identification.getText());

        if(symbol != null && symbol.getType().getWaccType() == WaccType.CHAR) {
            String value = symbol.getValue();

            if(value != null) {

                return value;
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static String evaluateUnaryOperator(
        ExpressionContext expression, UnaryOperatorContext unaryOperator)
        throws IncorrectExpressionFormat{

        if(unaryOperator.CHR_OPERATOR() != null) {

            int value = IntegerExpressionAnalyser
                    .generateIntegerValueOf(expression.expression(0));
            return "\'" + (char) value + '\'';
        }

        throw new IncorrectExpressionFormat();
    }

    private static String evaluateBracket(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        return generateCharValueOf(expression);
    }
}