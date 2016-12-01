package wacc.CodeGenerator.CodeGenerationOptimizer;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandComparator;
import wacc.CodeGenerator.Commands.ComparisonCommand.ComparisonCommand;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.StrCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.LogicalCommand;

import java.util.List;

public class CodeOptimizer {

    public static List<Command> optimiseCommands(
        List<Command> generatedCommands) {

        generatedCommands = optimiseLoadAndMoveCommand(generatedCommands);
        generatedCommands = optimiseLogicalCommand(generatedCommands);
        generatedCommands = optimiseComparisonCommand(generatedCommands);
        generatedCommands = optimiseStoreLoadRedundancy(generatedCommands);

        return generatedCommands;
    }

    public static List<Command> optimiseStoreLoadRedundancy(
        List<Command> generatedCommands) {

        int i = 0;

        // Loops over commands to detect when a Str command is followed
        // by a Ldr command
        while (i < generatedCommands.size() - 1) {

            Command firstCommand = generatedCommands.get(i);
            Command secondCommand = generatedCommands.get(i + 1);

            if (firstCommand instanceof StrCommand
                && secondCommand instanceof LdrCommand) {

                StrCommand storeCommand = (StrCommand) firstCommand;
                LdrCommand loadCommand = (LdrCommand) secondCommand;

                // If the str and ldr have the same composition (loading of the
                // register just stored), then delete the ldr command
                boolean areSameDataTransferCommand =
                    CommandComparator.areStoreAndLoadEqual(
                        storeCommand, loadCommand);

                if (areSameDataTransferCommand) {
                    generatedCommands.remove(i + 1);
                }
            }
            i++;
        }

        return generatedCommands;
    }

    public static List<Command> optimiseLogicalCommand (
        List<Command> generatedCommands) {

        int i = 0;

        while (i < generatedCommands.size() - 1) {

            Command firstCommand = generatedCommands.get(i);
            Command secondCommand = generatedCommands.get(i + 1);

            if (firstCommand instanceof LdrCommand
                && secondCommand instanceof LogicalCommand) {

                LdrCommand ldrCommand = (LdrCommand) firstCommand;
                LogicalCommand logicalCommand = (LogicalCommand) secondCommand;

                if (ldrCommand.canBeOptimisedForLogicalOrCmpCommands()
                    && logicalCommand.canBeOptimised()
                    && CommandComparator.doLoadAndLogicalCommandShareRegister(
                        ldrCommand, logicalCommand)
                    && ldrCommand.isValueSmallEnough()) {

                    generatedCommands.remove(i);
                    generatedCommands.remove(i);

                    logicalCommand
                        = logicalCommand.optimiseLdrCommand(ldrCommand);
                    generatedCommands.add(i, logicalCommand);
                }
            }

            if (firstCommand instanceof MovCommand
                && secondCommand instanceof LogicalCommand) {

                MovCommand movCommand = (MovCommand) firstCommand;
                LogicalCommand logicalCommand = (LogicalCommand) secondCommand;

                if (movCommand.canBeOptimisedForLogicalOrCmpCommands()
                    && logicalCommand.canBeOptimised()
                    && CommandComparator.doMoveAndLogicalCommandShareRegister(
                        movCommand, logicalCommand)
                    && movCommand.isValueSmallEnough()) {

                    generatedCommands.remove(i);
                    generatedCommands.remove(i);

                    logicalCommand
                        = logicalCommand.optimiseMovCommand(movCommand);
                    generatedCommands.add(i, logicalCommand);
                }
            }
            
            i++;
        }

        return generatedCommands;
    }

    public static List<Command> optimiseComparisonCommand(
        List<Command> generatedCommands){

        int i = 0;

        while (i < generatedCommands.size() - 1) {

            Command firstCommand = generatedCommands.get(i);
            Command secondCommand = generatedCommands.get(i + 1);

            if (firstCommand instanceof LdrCommand
                && secondCommand instanceof ComparisonCommand) {

                LdrCommand ldrCommand = (LdrCommand) firstCommand;
                ComparisonCommand comparisonCommand
                    = (ComparisonCommand) secondCommand;

                if (ldrCommand.canBeOptimisedForLogicalOrCmpCommands()
                    && comparisonCommand.canBeOptimised()
                    && CommandComparator
                        .doLoadAndComparisonCommandShareRegister(
                            ldrCommand, comparisonCommand)) {

                    generatedCommands.remove(i);
                    generatedCommands.remove(i);

                    comparisonCommand
                        = comparisonCommand.optimiseLdrCommand(ldrCommand);
                    generatedCommands.add(i, comparisonCommand);
                }
            }

            if (firstCommand instanceof MovCommand
                && secondCommand instanceof ComparisonCommand) {

                MovCommand movCommand = (MovCommand) firstCommand;
                ComparisonCommand comparisonCommand
                    = (ComparisonCommand) secondCommand;

                if (movCommand.canBeOptimisedForLogicalOrCmpCommands()
                    && comparisonCommand.canBeOptimised()
                    && CommandComparator
                        .doMoveAndComparisonCommandShareRegister(
                            movCommand, comparisonCommand)) {

                    generatedCommands.remove(i);
                    generatedCommands.remove(i);

                    comparisonCommand
                        = comparisonCommand.optimiseMovCommand(movCommand);
                    generatedCommands.add(i, comparisonCommand);
                }
            }

            i++;
        }

        return generatedCommands;
    }

    // TODO: Correct this optimisation for pairs
    public static List<Command> optimiseLoadAndMoveCommand(
        List<Command> generatedCommands) {

        int i = 0;

        loop:
        while (i < generatedCommands.size() - 1) {

            Command firstCommand = generatedCommands.get(i);
            Command secondCommand = generatedCommands.get(i + 1);
            Command thirdCommand = null;
            if (i+2 < generatedCommands.size()) {
                thirdCommand = generatedCommands.get(i + 2);
            }

            if (firstCommand instanceof LdrCommand
                && secondCommand instanceof MovCommand) {

                LdrCommand ldrCommand = (LdrCommand) firstCommand;
                MovCommand movCommand
                    = (MovCommand) secondCommand;

                if (thirdCommand instanceof BlCommand) {

                    BlCommand blCommand = (BlCommand) thirdCommand;

                    if (blCommand.getLabel().equals(PredefinedFunctionLabels
                        .CHECK_NULL_POINTER.toString())) {

                        i++;
                        continue loop;
                    }
                }



                if (ldrCommand.canBeOptimisedForMovCommands()
                    && movCommand.canBeOptimisedForLdrCommands()
                    && CommandComparator
                    .doLoadAndMoveCommandShareRegister(
                        ldrCommand, movCommand)) {

                    generatedCommands.remove(i);
                    generatedCommands.remove(i);

                    ldrCommand
                        = ldrCommand.optimiseMovCommand(movCommand);
                    generatedCommands.add(i, ldrCommand);
                }
            }

            i++;
        }

        return generatedCommands;
    }

    
}
