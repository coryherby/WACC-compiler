package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers;

import antlr.WaccParser.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic.ConditionStatementHelper;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;

public class IntegerExpressionAnalyser {

    public static int generateIntegerValueOf(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        TerminalNode charLiteral = expression.CHAR_LITERAL();
        TerminalNode identification = expression.IDENTIFICATION();
        IntLiteralContext intLiteral = expression.intLiteral();
        UnaryOperatorContext unaryOperator = expression.unaryOperator();
        BinOpPrecedence1Context binOpPrecedence1 =
            expression.binOpPrecedence1();
        BinOpPrecedence2Context binOpPrecedence2 =
            expression.binOpPrecedence2();
        TerminalNode bracketExpression = expression.OPEN_BRACKET();

        if(intLiteral != null) {

            return evaluateIntLiteral(intLiteral);
        } else if(identification != null
            && !ConditionStatementHelper.isInWhileLoop) {

            return evaluateIdentification(identification);
        } else if(binOpPrecedence1 != null) {

            return evaluateBinOpPrecedence1(expression, binOpPrecedence1);
        } else if(binOpPrecedence2 != null) {

            return evaluateBinopPrecedence2(expression, binOpPrecedence2);
        } else if(unaryOperator != null) {

            return evaluateUnaryOperator(expression, unaryOperator);
        } else if(bracketExpression != null) {

            return evaluateBracket(expression.expression(0));
        }

        throw new IncorrectExpressionFormat();
    }

    private static int evaluateIntLiteral(IntLiteralContext intLiteral) {

        return Integer.parseInt(intLiteral.getText());
    }

    private static int evaluateIdentification(TerminalNode identification)
        throws IncorrectExpressionFormat {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        Symbol symbol = symbolTable.get(identification.getText());

        if(symbol != null && symbol.getType().getWaccType() == WaccType.INT) {
            String value = symbol.getValue();

            if(value != null) {

                return Integer.valueOf(value);
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static int evaluateUnaryOperator(
        ExpressionContext expression, UnaryOperatorContext unaryOperator)
        throws IncorrectExpressionFormat {

        if(unaryOperator.MINUS() != null) {

            long newValue =
                - (long) generateIntegerValueOf(expression.expression(0));

            // Check for integer overflow / underflow id occurs
            if (newValue <= Integer.MAX_VALUE && newValue >= Integer.MIN_VALUE){
                return (int) newValue;
            }

        } else if (unaryOperator.ORD_OPERATOR() != null) {
            return (int) CharExpressionAnalyser
                .generateCharValueOf(expression.expression(0)).charAt(1);
        }

        throw new IncorrectExpressionFormat();
    }

    private static int evaluateBinOpPrecedence1(
        ExpressionContext expression, BinOpPrecedence1Context binOpPrecedence1)
        throws IncorrectExpressionFormat {

        if(binOpPrecedence1.DIVIDE() != null) {

            if(generateIntegerValueOf(expression.expression(1)) != 0) {

                return generateIntegerValueOf(expression.expression(0))
                    / generateIntegerValueOf(expression.expression(1));
            }
        } else if(binOpPrecedence1.MULTIPLY() != null) {

            long newValue =
                (long) generateIntegerValueOf(expression.expression(0))
                    * (long) generateIntegerValueOf(expression.expression(1));

            // Check for integer overflow / underflow id occurs
            if (newValue <= Integer.MAX_VALUE && newValue >= Integer.MIN_VALUE){
                return (int) newValue;
            }

        } else if(binOpPrecedence1.MODULUS() != null) {
            if(generateIntegerValueOf(expression.expression(1)) != 0) {

                return generateIntegerValueOf(expression.expression(0))
                    % generateIntegerValueOf(expression.expression(1));
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static int evaluateBinopPrecedence2(
        ExpressionContext expression, BinOpPrecedence2Context binOpPrecedence2)
        throws IncorrectExpressionFormat {

        if(binOpPrecedence2.PLUS() != null) {

            long newValue =
                (long) generateIntegerValueOf(expression.expression(0))
                    + (long) generateIntegerValueOf(expression.expression(1));

            // Check for integer overflow / underflow id occurs
            if (newValue <= Integer.MAX_VALUE && newValue >= Integer.MIN_VALUE){
                return (int) newValue;
            }

        } else if(binOpPrecedence2.MINUS() != null) {

            long newValue =
                (long) generateIntegerValueOf(expression.expression(0))
                    - (long) generateIntegerValueOf(expression.expression(1));

            // Check for integer overflow / underflow id occurs
            if (newValue <= Integer.MAX_VALUE && newValue >= Integer.MIN_VALUE){
                return (int) newValue;
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static int evaluateBracket(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        return generateIntegerValueOf(expression);
    }
}
