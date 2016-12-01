package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.*;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.*;

import java.util.ArrayList;
import java.util.List;

public class PrintStatement implements Statement {

    private static final String PRINT_CHAR = "putchar";

    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();

        commands.addAll(expression.generateCommandsForExpression());

        NormalRegister exprReg = hardwareManager.getStorageRegister();
        hardwareManager.freeRegister(exprReg);

        ConditionCode always = ConditionCode.AL;
        NormalRegister r0 = hardwareManager.getRegister0();

        // Moving expression into r0
        MovCommand exprToR0 = new MovCommand(always, r0, exprReg);
        commands.add(exprToR0);

        Expression expression = this.expression;

        String printingFunction = choosePrintingFunction(expression);

        // Call Right Print function
        BlCommand callRightPrint = new BlCommand(always, printingFunction);
        commands.add(callRightPrint);

        return commands;
    }

    private String choosePrintingFunction(Expression expression) {

        HardwareManager hardwareManager = HardwareManager.getInstance();
        String printingFunction = null;
        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();

        if (expression instanceof IntLiteral) {
            printingFunction = PredefinedFunctionLabels.PRINT_INT.toString();
            codeGenHelper.addPredefinedFunction(
                PredefinedFunctionLabels.PRINT_INT);

        } else if (expression instanceof BoolLiteral) {
            printingFunction = PredefinedFunctionLabels.PRINT_BOOL.toString();
            codeGenHelper.addPredefinedFunction(
                PredefinedFunctionLabels.PRINT_BOOL);

        } else if (expression instanceof CharLiteral) {
            printingFunction = PRINT_CHAR;

        } else if (expression instanceof StringLiteral) {
            printingFunction = PredefinedFunctionLabels.PRINT_STRING.toString();
            codeGenHelper.addPredefinedFunction(
                PredefinedFunctionLabels.PRINT_STRING);

        } else if (expression instanceof PairLiteral) {
            printingFunction
                = PredefinedFunctionLabels.PRINT_REFERENCE.toString();
            codeGenHelper.addPredefinedFunction(
                PredefinedFunctionLabels.PRINT_REFERENCE);

        } else if (expression instanceof Identification) {

            String identifier = ((Identification) expression).getIdentifier();
            Type type = hardwareManager.getVariableType(identifier);

            if (type instanceof ArrayType) {

                WaccType waccType
                    = ((ArrayType) type).getNestedType().getWaccType();

                if (waccType == WaccType.CHAR){
                    printingFunction
                        = PredefinedFunctionLabels.PRINT_STRING.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_STRING);
                } else {

                    printingFunction
                        = PredefinedFunctionLabels.PRINT_REFERENCE.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_REFERENCE);
                }

            } else if (type instanceof BaseType) {

                if (type.equals(
                    new BaseType(WaccType.INT))) {

                    printingFunction
                        = PredefinedFunctionLabels.PRINT_INT.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_INT);

                } else if (type.equals(
                    new BaseType(WaccType.BOOL))) {

                    printingFunction
                        = PredefinedFunctionLabels.PRINT_BOOL.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_BOOL);

                } else if (type.equals(
                    new BaseType(WaccType.CHAR))) {

                    printingFunction = PRINT_CHAR;

                } else if (type.equals(
                    new BaseType(WaccType.STRING))) {

                    printingFunction
                        = PredefinedFunctionLabels.PRINT_STRING.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_STRING);
                }

            } else if (type instanceof NullType) {

                printingFunction
                    = PredefinedFunctionLabels.PRINT_REFERENCE.toString();
                codeGenHelper.addPredefinedFunction(
                    PredefinedFunctionLabels.PRINT_REFERENCE);

            } else if (type instanceof PairElemType) {

                throw new RuntimeException(
                    "It is not possible to print a pair element.");

            } else if (type instanceof PairType){

                printingFunction
                    = PredefinedFunctionLabels.PRINT_REFERENCE.toString();
                codeGenHelper.addPredefinedFunction(
                    PredefinedFunctionLabels.PRINT_REFERENCE);
            }


        } else if (expression instanceof ArrayElem) {
            String identifier = ((ArrayElem) expression).getIdentifier();
            Type type = hardwareManager.getVariableType(identifier);

            while(((ArrayType) type).getNestedType() instanceof ArrayType) {
                type = ((ArrayType) type).getNestedArrayType();
            }

            type = ((ArrayType) type).getNestedType();

            if (type.equals(
                new BaseType(WaccType.INT))) {

                printingFunction
                    = PredefinedFunctionLabels.PRINT_INT.toString();
                codeGenHelper.addPredefinedFunction(
                    PredefinedFunctionLabels.PRINT_INT);

            } else if (type.equals(
                new BaseType(WaccType.BOOL))) {

                printingFunction
                    = PredefinedFunctionLabels.PRINT_BOOL.toString();
                codeGenHelper.addPredefinedFunction(
                    PredefinedFunctionLabels.PRINT_BOOL);

            } else if (type.equals(
                new BaseType(WaccType.CHAR))) {

                printingFunction = PRINT_CHAR;

            } else if (type.equals(
                new BaseType(WaccType.STRING))) {

                printingFunction
                    = PredefinedFunctionLabels.PRINT_STRING.toString();
                codeGenHelper.addPredefinedFunction(
                    PredefinedFunctionLabels.PRINT_STRING);
            }


        } else if (expression instanceof UnaryOperatorExpr) {
            WaccType type = ((UnaryOperatorExpr) expression).getType();

            switch(type) {

                case INT:
                    printingFunction
                        = PredefinedFunctionLabels.PRINT_INT.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_INT);
                    break;

                case CHAR:
                    printingFunction = PRINT_CHAR;
                    break;

                case BOOL:
                    printingFunction
                        = PredefinedFunctionLabels.PRINT_BOOL.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_BOOL);
                    break;

                default:
                    break;
            }

        } else if (expression instanceof BracketExpr) {

            Expression normalExpression
                = ((BracketExpr) expression).getInsideBracketExpression();
            return choosePrintingFunction(normalExpression);

        } else if (expression instanceof BinaryOperatorExpr) {
            WaccType type = ((BinaryOperatorExpr) expression).getType();

            switch(type) {

                case INT:
                    printingFunction
                        = PredefinedFunctionLabels.PRINT_INT.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_INT);
                    break;

                case BOOL:
                    printingFunction
                        = PredefinedFunctionLabels.PRINT_BOOL.toString();
                    codeGenHelper.addPredefinedFunction(
                        PredefinedFunctionLabels.PRINT_BOOL);
                    break;

                default:
                    break;
            }
        }
        return printingFunction;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateStatementRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            "PrintStatement",
            expression.generateExpressionRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
