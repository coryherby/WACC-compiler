package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.ParameterContext;
import antlr.WaccParser.ParameterListContext;
import wacc.CodeGenerator.InternalRepresentation.FunctionParameterRepresentation.FunctionParameter;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionParameterFactory {

    public static List<FunctionParameter>
        buildFunctionParametersFromParameterListContext(
            ParameterListContext parameterListContext) {

        List<ParameterContext> parameterContexts
            = parameterListContext.parameter();

        List<FunctionParameter> functionParameters
            = parameterContexts
                .stream()
                .map(parameterContext ->
                    new FunctionParameter(
                        TypeBuilder.buildTypeFromAntlr(parameterContext.type()),
                        parameterContext.IDENTIFICATION().getText()))
                .collect(Collectors.toList());

        return functionParameters;
    }
}
