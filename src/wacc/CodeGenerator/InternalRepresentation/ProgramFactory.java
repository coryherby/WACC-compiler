package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.ProgramContext;
import wacc.CodeGenerator.InternalRepresentation.FunctionRepresentation.Function;
import wacc.CodeGenerator.InternalRepresentation.ProgramRepresentation.Program;
import wacc.CodeGenerator.InternalRepresentation.StatementRepresentation.Statement;

import java.util.List;
import java.util.stream.Collectors;

public class ProgramFactory {

    public static Program buildProgramFromProgramContext(
        ProgramContext programContext) {

        Statement mainStatement = StatementFactory
            .buildStatementFromStatementContext(programContext.statement());
        List<Function> programFunctions = null;

        if (programContext.function() != null) {

            programFunctions = programContext
                .function()
                .stream()
                .map(FunctionFactory::buildFunctionFromFunctionContext)
                .collect(Collectors.toList());
        }

        return new Program(programFunctions, mainStatement);
    }
}
