package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.FunctionContext;
import wacc.CodeGenerator.InternalRepresentation.FunctionParameterRepresentation.FunctionParameter;
import wacc.CodeGenerator.InternalRepresentation.FunctionRepresentation.Function;
import wacc.CodeGenerator.InternalRepresentation.StatementRepresentation.Statement;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

import java.util.ArrayList;
import java.util.List;

public class FunctionFactory {

    public static Function buildFunctionFromFunctionContext(
        FunctionContext functionContext) {

        Type functionType
            = TypeBuilder.buildTypeFromAntlr(functionContext.type());
        String identification
            = functionContext.IDENTIFICATION().getText();
        Statement functionStatement = StatementFactory
            .buildStatementFromStatementContext(functionContext.statement());

        List<FunctionParameter> functionParameters = new ArrayList<>();

        if (functionContext.parameterList() != null) {

            functionParameters
                = FunctionParameterFactory
                    .buildFunctionParametersFromParameterListContext(
                        functionContext.parameterList());
        }

        return new Function(functionType, identification,
            functionParameters, functionStatement);
    }
}
