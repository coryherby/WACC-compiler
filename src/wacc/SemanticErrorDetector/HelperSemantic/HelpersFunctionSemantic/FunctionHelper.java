package wacc.SemanticErrorDetector.HelperSemantic.HelpersFunctionSemantic;

import antlr.WaccParser;
import antlr.WaccParser.FunctionContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

import java.util.LinkedList;
import java.util.List;

public class FunctionHelper {

    public static void addFunctionParameterToSymbolTable(
        FunctionContext functionContext) {

        if(functionContext.parameterList() == null) {
            return;
        }

        List<WaccParser.ParameterContext> parameterContext
            = functionContext.parameterList().parameter();

        if (parameterContext == null) {
            return;
        }

        int parameterContextSize
            = functionContext.parameterList().parameter().size();

        for(int i = parameterContextSize - 1; i >= 0; i--) {

            String identification = functionContext.parameterList()
                .parameter(i).IDENTIFICATION().getText();

            Type t = TypeBuilder.buildTypeFromAntlr(
                functionContext.parameterList().parameter(i).type());

            MainWaccTable.getInstance().getSymbolTable().put(identification, t);
        }
    }

    private static List<Type> createParameterList(
        FunctionContext functionContext) {

        List<Type> parameterList = new LinkedList<Type>();
        if(functionContext.parameterList() != null) {

            for (int i = 0; functionContext.parameterList().parameter(i) != null
                ; i++) {

                parameterList.add(TypeBuilder.buildTypeFromAntlr(
                        functionContext.parameterList().parameter(i).type()));
            }
        }

        return parameterList;
    }

    public static boolean checkIsNotAlreadyInitializedFunction(
        FunctionContext functionContext) {

        if(MainWaccTable.getInstance().getFunctionTable()
            .get(functionContext.IDENTIFICATION().getText()) != null) {

            ErrorReporter.reportMultipleFunctionDeclaration(functionContext);
            return false;
        }

        return true;
    }

    public static void initializeFunction(FunctionContext functionContext) {

        Type type = TypeBuilder.buildTypeFromAntlr(functionContext.type());
        List<Type> parameterTypeList
            = createParameterList(functionContext);

        MainWaccTable.getInstance().getFunctionTable().put(functionContext
            .IDENTIFICATION().getText(), type, parameterTypeList);
    }
}

