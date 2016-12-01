package wacc.SemanticErrorDetector.WaccTypes;

import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.TypeContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.*;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;

public class TypeBuilder {

    public static Type buildTypeFromAntlr(TypeContext typeContext) {

        if (typeContext.baseType() != null) {

            return BaseType.createBaseType(typeContext.baseType());

        } else if (typeContext.type() != null) {

            return ArrayType.createArrayType(typeContext.type());

        } else if (typeContext.pairType() != null) {

            return PairType.createPairType(typeContext.pairType());
        }

        return null;
    }

    public static Type buildTypeFromExpression(
        ExpressionContext expressionContext) {

        // Forgets all the brackets
        while (BracketExpressionAnalyser.hasBracket(expressionContext)) {
        	
            expressionContext = BracketExpressionAnalyser
                .getExpressionInsideBrackets(expressionContext);
        }

        if (BoolExpressionAnalyser.isBoolExpression(expressionContext)) {

            return new BaseType(BoolExpressionAnalyser.CLASS_TYPE);

        } else if (CharExpressionAnalyser.isCharExpression(expressionContext)) {

            return new BaseType(CharExpressionAnalyser.CLASS_TYPE);

        } else if (
            StringExpressionAnalyser.isStringExpression(expressionContext)) {

            return new BaseType(StringExpressionAnalyser.CLASS_TYPE);

        } else if (IntExpressionAnalyser.isIntExpression(expressionContext)) {

            return new BaseType(IntExpressionAnalyser.CLASS_TYPE);

        } else if (PairExpressionAnalyser.isPairExpression(expressionContext)) {

            return PairExpressionAnalyser.getActualType(expressionContext);

        } else if (ArrayElemExpressionAnalyser.isArrayElem(expressionContext)) {

            return ArrayElemExpressionAnalyser.getActualType(
                expressionContext.arrayElement());

        } else if (
            ArrayExpressionAnalyser.isArrayExpression(expressionContext)) {

            return ArrayExpressionAnalyser.getActualType(expressionContext);
        }

        return null;
    }
}
