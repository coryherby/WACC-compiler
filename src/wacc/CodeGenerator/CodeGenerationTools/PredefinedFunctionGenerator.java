package wacc.CodeGenerator.CodeGenerationTools;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.*;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.LabelCommand.LabelCommand;

import java.util.*;

public class PredefinedFunctionGenerator {

    private static PredefinedFunctionGenerator predefinedFunctionGenerator
        = new PredefinedFunctionGenerator();

    private Deque<PredefinedFunctionLabels> predefinedFunctionLabels;

    private PredefinedFunctionGenerator() {
        predefinedFunctionLabels = new ArrayDeque<>();
    }

    public static PredefinedFunctionGenerator getInstance() {
        return predefinedFunctionGenerator;
    }

    public void addPredefinedFunction(PredefinedFunctionLabels function) {
        predefinedFunctionLabels.addLast(function);
    }

    public List<Command> generatePredefinedFunctions() {
        List<Command> commands = new ArrayList<>();

        Set<PredefinedFunctionLabels> predefinedFunctionLabelsSet
            = new HashSet<>();

        // Loops on all the predefined functions in the stack
        while (!predefinedFunctionLabels.isEmpty()) {

            PredefinedFunctionLabels function = predefinedFunctionLabels.pop();

            // If isn't in the set of functions already generated,
            // then generate the predefined function
            if (!predefinedFunctionLabelsSet.contains(function)) {

                predefinedFunctionLabelsSet.add(function);

                LabelCommand label = new LabelCommand(function.toString());
                commands.add(label);
                commands.addAll(generatePredefinedFunction(function));
            }
        }

        return commands;
    }

    private List<Command> generatePredefinedFunction(
        PredefinedFunctionLabels function) {

        List<Command> commands = new ArrayList<>();

        switch (function) {
            case READ_INT_FUNCTION:
                commands.addAll(ReadFunction.generateReadIntCommands());
                break;

            case READ_CHAR_FUNCTION:
                commands.addAll(ReadFunction.generateReadCharCommands());
                break;

            case CHECK_ARRAY_BOUNDS:
                commands.addAll(CheckArrayBoundsFunction
                    .generateCheckArrayBoundsCommands());
                break;

            case THROW_RUNTIME_ERROR:
                commands.addAll(ThrowRuntimeErrorFunction
                    .generateThrowRuntimeErrorCommands());
                break;

            case PRINT_STRING:
                commands.addAll(PrintStringFunction
                    .generatePrintStringCommands());
                break;

            case PRINT_INT:
                commands.addAll(PrintIntFunction.generatePrintIntCommands());
                break;

            case PRINT_BOOL:
                commands.addAll(PrintBoolFunction.generatePrintBoolCommands());
                break;

            case PRINT_REFERENCE:
                commands.addAll(PrintReferenceFunction
                    .generatePrintReferenceCommands());
                break;

            case DIVIDE_BY_ZERO:
                commands.addAll(CheckDividedByZeroFunction
                    .generateDivideByZeroCommands());
                break;

            case FREE_PAIR:
                commands.addAll(FreePairFunction.generateFreePairCommands());
                break;

            case PRINT_LN:
                commands.addAll(PrintLnFunction.generatePrintLnCommands());
                break;

            case CHECK_NULL_POINTER:
                commands.addAll(CheckNullPointerFunction
                    .generateCheckNullPointerCommands());
                break;

            case THROW_OVERFLOW_ERROR:
                commands.addAll(ThrowOverflowErrorFunction
                    .generateThrowOverflowErrorCommands());
                break;

            case TROW_RUNTIME_ERROR_FREE:
                commands.addAll(ThrowRuntimeErrorFreeFunction
                    .generateThrowRuntimeErrorFreeCommands());

            default:
                break;
        }

        return commands;
    }
}
