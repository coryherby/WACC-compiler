package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser;
import antlr.WaccParser.FunctionContext;
import antlr.WaccParser.ProgramContext;
import antlr.WaccParser.StatementContext;
import org.antlr.v4.runtime.ParserRuleContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser.AssignementAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers.ExpressionAnalyser;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.SymbolTable;

public class InitializationHelper {

    public static boolean isInitializationStatement
        (StatementContext statementContext) {

        return statementContext.IDENTIFICATION() != null;
    }

    public static boolean checkIsNotAlreadyInitializedVariable(
        StatementContext statementContext) {

        int counter = 0;

        for(ParserRuleContext nextStatement = statementContext;
            !ProgramContext.class.isInstance(nextStatement);
            nextStatement = nextStatement.getParent()) {

            if(StatementContext.class.isInstance(nextStatement)) {

                if(NewScopeHelper.isNewScopeStatement((StatementContext)
                    nextStatement)) {

                    if(MainWaccTable.getInstance().getSymbolTable().getForXScope
                        (statementContext.IDENTIFICATION()
                            .getText(), counter) != null) {

                        ErrorReporter.reportMultipleVariableDeclaration(
                            statementContext);
                        return false;

                    } else {

                        return true;

                    }

                } else if (ConditionStatementHelper.isIfStatement(
                    (StatementContext) nextStatement) ||
                    ConditionStatementHelper.isWhileStatement(
                        (StatementContext) nextStatement)) {

                    counter++;
                }

            } else if (FunctionContext.class.isInstance(nextStatement)) {

                if(MainWaccTable.getInstance().getSymbolTable().getForXScope
                    (statementContext.IDENTIFICATION()
                        .getText(), counter) != null) {

                    ErrorReporter.reportMultipleVariableDeclaration(
                        statementContext);
                    return false;

                } else {

                    return true;
                }
            }
        }

        if(MainWaccTable.getInstance().getSymbolTable().get
            (statementContext.IDENTIFICATION().getText()) != null) {

            ErrorReporter.reportMultipleVariableDeclaration(
                statementContext);
            return false;
        }

        return true;
    }

    public static void initializeVariable(StatementContext statementContext) {
        Type t = TypeBuilder.buildTypeFromAntlr(statementContext.type());
        String identification = statementContext.IDENTIFICATION().getText();
        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        symbolTable.put(identification, t);
    }

    public static void checkInitialize(StatementContext statementContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        String identification = statementContext.IDENTIFICATION().getText();
        Symbol symbol = symbolTable.get(identification);

        // Assignment to variable - checking if right type assigned
        AssignementAnalyser
            .checkAssignType(symbol, statementContext.assignRhs());
    }

    public static void optimizeConstant(StatementContext statementContext) {

        SymbolTable symbolTable = MainWaccTable.getInstance().getSymbolTable();
        String identification = statementContext.IDENTIFICATION().getText();
        Symbol symbol = symbolTable.get(identification);

        // Try to optimize code if can be simplified
        if (isAssignmentExpression(statementContext)
            && ErrorReporter.hasNoError()) {

            String initialValue = ExpressionAnalyser.evaluateExpression(
                statementContext.assignRhs().expression(0));

            // Give the initial value simplified
            symbol.setValue(initialValue);
        }
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