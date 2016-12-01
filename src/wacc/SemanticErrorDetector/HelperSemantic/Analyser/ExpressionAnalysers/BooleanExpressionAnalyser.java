package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers;

import antlr.WaccParser.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic.ConditionStatementHelper;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;

public class BooleanExpressionAnalyser {

    public static boolean generateBooleanValueOf(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        TerminalNode identification = expression.IDENTIFICATION();
        UnaryOperatorContext unaryOperator = expression
            .unaryOperator();
        BinOpPrecedence3Context binOpPrecedence3Context =
            expression.binOpPrecedence3();
        BinOpPrecedence4Context binOpPrecedence4Context =
            expression.binOpPrecedence4();
        BinOpPrecedence5Context binOpPrecedence5Context =
            expression.binOpPrecedence5();
        BinOpPrecedence6Context binOpPrecedence6Context =
            expression.binOpPrecedence6();
        TerminalNode bracketExpression = expression.OPEN_BRACKET();

        if(expression.boolLiteral() != null) {

            return evaluateBoolLiteral(expression.boolLiteral());
        } else if(identification != null
            && !ConditionStatementHelper.isInWhileLoop) {

            return evaluateIdentification(identification);
        } else if(unaryOperator != null) {

            return evaluateUnaryOperator(expression, unaryOperator);
        } else if(binOpPrecedence3Context != null) {

            return evaluateBinopPrecedence3(
                expression, binOpPrecedence3Context);
        } else if(binOpPrecedence4Context != null) {

            return evaluateBinopPrecedence4(
                expression, binOpPrecedence4Context);
        } else if(binOpPrecedence5Context != null) {

            return evaluateBinopPrecedence5(
                expression, binOpPrecedence5Context);
        } else if(binOpPrecedence6Context != null) {

            return evaluateBinopPrecedence6(
                expression, binOpPrecedence6Context);
        } else if(bracketExpression != null) {

            return evaluateBracket(expression.expression(0));
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateBoolLiteral(BoolLiteralContext boolLiteral)
        throws IncorrectExpressionFormat {

        if(boolLiteral != null) {
            if(boolLiteral.TRUE() != null) {
                return true;
            } else if(boolLiteral.FALSE() != null) {
                return false;
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateIdentification(TerminalNode identification)
        throws IncorrectExpressionFormat {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        Symbol symbol = symbolTable.get(identification.getText());

        if(symbol != null && symbol.getType().getWaccType() == WaccType.BOOL) {
            String value = symbol.getValue();

            if(value != null) {

                return Boolean.valueOf(value);
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateUnaryOperator(
        ExpressionContext expression, UnaryOperatorContext unaryOperator)
        throws IncorrectExpressionFormat {

        if(unaryOperator.NOT_OPERATOR() != null) {

            return !generateBooleanValueOf(expression.expression(0));
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateBinopPrecedence3(
        ExpressionContext expression, BinOpPrecedence3Context binOpPrecedence3)
        throws IncorrectExpressionFormat {

        try {
            int value1 = IntegerExpressionAnalyser
                .generateIntegerValueOf(expression.expression(0));
            int value2 = IntegerExpressionAnalyser
                .generateIntegerValueOf(expression.expression(1));

            if (binOpPrecedence3.GREATER_THAN() != null) {

                return value1 > value2;
            } else if (binOpPrecedence3.GREATER_OR_EQUAL_THAN() != null) {

                return value1 >= value2;
            } else if (binOpPrecedence3.SMALLER_THAN() != null) {

                return value1 < value2;
            } else if (binOpPrecedence3.SMALLER_OR_EQUAL_THAN() != null) {

                return value1 <= value2;
            }

        } catch (IncorrectExpressionFormat ignored) {}

        try {
            String value1 = CharExpressionAnalyser
                .generateCharValueOf(expression.expression(0));
            String value2 = CharExpressionAnalyser
                .generateCharValueOf(expression.expression(1));

            if (binOpPrecedence3.GREATER_THAN() != null) {

                return ((int) value1.charAt(1) - (int) value2.charAt(1)) > 0;
            } else if (binOpPrecedence3.GREATER_OR_EQUAL_THAN() != null) {

                return ((int) value1.charAt(1) - (int) value2.charAt(1)) >= 0;
            } else if (binOpPrecedence3.SMALLER_THAN() != null) {

                return ((int) value1.charAt(1) - (int) value2.charAt(1)) < 0;
            } else if (binOpPrecedence3.SMALLER_OR_EQUAL_THAN() != null) {

                return ((int) value1.charAt(1) - (int) value2.charAt(1)) <= 0;
            }

        } catch (IncorrectExpressionFormat ignored) {}

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateBinopPrecedence4(
        ExpressionContext expression, BinOpPrecedence4Context binOpPrecedence4)
        throws IncorrectExpressionFormat {

        try {
            int value1 = IntegerExpressionAnalyser
                .generateIntegerValueOf(expression.expression(0));
            int value2 = IntegerExpressionAnalyser
                .generateIntegerValueOf(expression.expression(1));

            if (binOpPrecedence4.IS_EQUAL_TO() != null) {

                return value1 == value2;
            } else if (binOpPrecedence4.IS_NOT_EQUAL_TO() != null) {

                return value1 != value2;
            }

        } catch (IncorrectExpressionFormat ignored) {}

        try {
            boolean value1 = BooleanExpressionAnalyser
                .generateBooleanValueOf(expression.expression(0));
            boolean value2 = BooleanExpressionAnalyser
                .generateBooleanValueOf(expression.expression(1));

            if (binOpPrecedence4.IS_EQUAL_TO() != null) {

                return value1 == value2;
            } else if (binOpPrecedence4.IS_NOT_EQUAL_TO() != null) {

                return value1 != value2;
            }

        } catch (IncorrectExpressionFormat ignored) {}

        try {
            String value1 = CharExpressionAnalyser
                .generateCharValueOf(expression.expression(0));
            String value2 = CharExpressionAnalyser
                .generateCharValueOf(expression.expression(1));

            if (binOpPrecedence4.IS_EQUAL_TO() != null) {

                return value1.equals(value2);
            } else if (binOpPrecedence4.IS_NOT_EQUAL_TO() != null) {

                return !value1.equals(value2);
            }

        } catch (IncorrectExpressionFormat ignored) {}


        if (expression.expression(0).IDENTIFICATION() != null
            && expression.expression(1).IDENTIFICATION() != null) {

            if (expression.expression(0).IDENTIFICATION().getText()
                .equals(expression.expression(1).IDENTIFICATION().getText())) {
                return true;
            }
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateBinopPrecedence5(
        ExpressionContext expression, BinOpPrecedence5Context binOpPrecedence5)
        throws IncorrectExpressionFormat {

        if(binOpPrecedence5.AND() != null) {

            return generateBooleanValueOf(expression.expression(0))
                && generateBooleanValueOf(expression.expression(1));
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateBinopPrecedence6(
        ExpressionContext expression, BinOpPrecedence6Context binOpPrecedence6)
        throws IncorrectExpressionFormat {

        if(binOpPrecedence6.OR() != null) {
            return generateBooleanValueOf(expression.expression(0))
                || generateBooleanValueOf(expression.expression(1));
        }

        throw new IncorrectExpressionFormat();
    }

    private static boolean evaluateBracket(ExpressionContext expression)
        throws IncorrectExpressionFormat {

        return generateBooleanValueOf(expression);
    }
}
