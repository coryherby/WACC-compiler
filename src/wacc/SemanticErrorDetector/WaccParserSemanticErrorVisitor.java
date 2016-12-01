package wacc.SemanticErrorDetector;

import antlr.WaccParser;
import antlr.WaccParser.StatementContext;
import antlr.WaccParserBaseVisitor;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.HelperSemantic.GeneralHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersAssignRhsSemantic.FunctionCallHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersExpressionSemantic.OperatorHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersExpressionSemantic.VariableHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersFunctionSemantic.FunctionHelper;
import wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic.*;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.SymbolTable;

import java.util.LinkedList;
import java.util.List;

public class WaccParserSemanticErrorVisitor extends WaccParserBaseVisitor<Void>{

    @Override
    public Void visitProgram(WaccParser.ProgramContext ctx) {

        for(int i = 0; ctx.function(i) != null; i++) {

            WaccParser.FunctionContext function = ctx.function(i);

            if(FunctionHelper.checkIsNotAlreadyInitializedFunction(function)) {
                FunctionHelper.initializeFunction(ctx.function(i));
            }
        }

        Void visitChildren = visitChildren(ctx);

        // Rearrange tree for code generation
        rearrangeSymbolTableTree(ctx);

        return visitChildren;
    }

    @Override
    public Void visitFunction(WaccParser.FunctionContext ctx) {

        GeneralHelper.addNewScope();
        FunctionHelper.addFunctionParameterToSymbolTable(ctx);
        GeneralHelper.addNewScope();

        Void visitChildren = visitChildren(ctx);

        GeneralHelper.returnToParentScope();
        GeneralHelper.returnToParentScope();

        return visitChildren;
    }

    @Override
    public Void visitStatement(WaccParser.StatementContext ctx) {

        /*
          Control flow analysis extension
        */

        if (ConditionStatementHelper.isIfStatement(ctx)) {
            ctx = ConditionStatementHelper.controlFlowAnalysisForIf(ctx);

        } else if (ConditionStatementHelper.isWhileStatement(ctx)) {
            ctx = ConditionStatementHelper.controlFlowAnalysisForWhile(ctx);
        }


        if (ConditionStatementHelper.isIfStatement(ctx)) {
            ConditionStatementHelper.checkConditionIfIsBool(ctx);

        } else if (ConditionStatementHelper.isWhileStatement(ctx)) {
            ConditionStatementHelper.checkConditionWhileIsBool(ctx);
            ConditionStatementHelper.isInWhileLoop = true;
            GeneralHelper.addNewScope();

        } else if (ReturnHelper.isReturnStatement(ctx)) {

            if(ReturnHelper.checkIsNotCalledInMain(ctx)) {
                ReturnHelper.checkHasCorrectType(ctx);
            }

        } else if (ExitHelper.isExitStatement(ctx)) {
            ExitHelper.checkExit(ctx);

        } else if (FreeHelper.isFreeStatement(ctx)) {
            FreeHelper.checkFreeStatement(ctx);

        } else if(ReadHelper.readStatement(ctx)) {
            ReadHelper.checkReadCorrectType(ctx);
            ReadHelper.updateVariableValue(ctx);

        } else if (NewScopeHelper.isNewScopeStatement(ctx)) {
            GeneralHelper.addNewScope();

        }

        if(ctx.getParent() instanceof WaccParser.StatementContext
            && ConditionStatementHelper.isIfStatement(
            (StatementContext) ctx.getParent())) {

            GeneralHelper.addNewScope();
        }

        if (InitializationHelper.isInitializationStatement(ctx)) {

            if(InitializationHelper.checkIsNotAlreadyInitializedVariable(ctx)) {
                InitializationHelper.initializeVariable(ctx);
                InitializationHelper.checkInitialize(ctx);
                // Store the initial value at initialisation in order to
                // optimize later the constant in future assignement
                InitializationHelper.optimizeConstant(ctx);

                GeneralHelper
                    .codeGeneratorAddToLinkedReferenceForInitialisation(ctx);
            }

        } else if (AssignmentHelper.isAssignmentStatement(ctx)) {
            AssignmentHelper.checkIsUndeclaredVariable(ctx);
            AssignmentHelper.checkAssignement(ctx);
            // Visit the assignement in case of call to self in expression
            visitAssignRhs(ctx.assignRhs());
            // Optimize the constant by giving it a new value if possible
            AssignmentHelper.optimizeConstant(ctx);

            GeneralHelper
                .codeGeneratorAddToLinkedReferenceForAssignement(ctx);
        }

        Void visitChildren = visitChildren(ctx);

        if (ConditionStatementHelper.isWhileStatement(ctx)) {
            GeneralHelper.returnToParentScope();
            ConditionStatementHelper.isInWhileLoop = false;

        } else if (NewScopeHelper.isNewScopeStatement(ctx)) {
            GeneralHelper.returnToParentScope();
        }

        if(ctx.getParent() instanceof  WaccParser.StatementContext
            && ConditionStatementHelper.isIfStatement(
            (StatementContext) ctx.getParent())) {

            GeneralHelper.returnToParentScope();
        }

        return visitChildren;
    }

    @Override
    public Void visitAssignRhs(WaccParser.AssignRhsContext ctx) {

        if (FunctionCallHelper.isFunctionAssignmentStatement(ctx)) {

            if (FunctionCallHelper.checkIsAlreadyInitializedFunction(ctx)) {

                FunctionCallHelper.checkCorrectNumberOfArgument(ctx);
                FunctionCallHelper.checkCorrectArgumentTypes(ctx);
            }
        }

        return visitChildren(ctx);
    }

    @Override
    public Void visitExpression(WaccParser.ExpressionContext ctx) {

        if (VariableHelper.isAVariable(ctx)) {
            VariableHelper.checkDeclaredVariableUse(ctx);
        }

        if (!OperatorHelper.checkOperatorExpressionValid(ctx)) {
            return null;
        }

        /*
          Constant propagation analysis extension
        */

        if (ErrorReporter.hasNoError()) {
            ctx = GeneralHelper.constantpropagationAnalysis(ctx);
        }

        return visitChildren(ctx);
    }

    /*
      Semantic Helper function
    */

    private void rearrangeSymbolTableTree(WaccParser.ProgramContext ctx) {

        MainWaccTable mainWaccTable = MainWaccTable.getInstance();

        SymbolTable actualTopSymbolTable = mainWaccTable.getSymbolTable();
        List<SymbolTable> children = actualTopSymbolTable.getChildren();

        SymbolTable newTopSymbolTable
            = new SymbolTable(null, new LinkedList<>());

        // Set children and parents for function nodes to newTopSymbolTable
        for (int i = 0; ctx.function(i) != null; i++) {
            SymbolTable child = children.get(i);
            child.setParent(newTopSymbolTable);
            newTopSymbolTable.addChildren(child);
        }

        // Remove old children function children of actualTopSymbolTable (main)
        for (int i = 0; ctx.function(i) != null; i++) {
            actualTopSymbolTable.removeChildren(0);
        }

        // Set the new Top symbol table to have the Main as its last child
        newTopSymbolTable.addChildren(actualTopSymbolTable);
        // Set the Main table to have the new Top symbol table as its parent
        actualTopSymbolTable.setParent(newTopSymbolTable);

        // Set the main wacc table to point to the newTopSymbolTable
        mainWaccTable.setSymbolTable(newTopSymbolTable);
    }
}