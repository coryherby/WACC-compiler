package wacc.CodeGenerator.Commands;

import wacc.CodeGenerator.Commands.ComparisonCommand.ComparisonCommand;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataTransferCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.LogicalCommand;

public class CommandComparator {

    public static boolean areStoreAndLoadEqual(
        DataTransferCommand firstCommand, DataTransferCommand secondCommand) {

        boolean areConditionFieldsEqual = firstCommand.getConditionField()
            .equals(secondCommand.getConditionField());

        boolean areDataSizesEqual = firstCommand.getDataSize()
            .equals(secondCommand.getDataSize());

        boolean areDestinationRegistersEqual
            = (firstCommand.getDestinationRegister() == null
            && secondCommand.getDestinationRegister() == null)
            || (firstCommand.getDestinationRegister() != null)
            && firstCommand.getDestinationRegister()
            .equals(secondCommand.getDestinationRegister());

        boolean areSourceRegisters1Equal
            = (firstCommand.getSourceRegister() == null
            && secondCommand.getSourceRegister() == null)
            || (firstCommand.getSourceRegister() != null)
            && firstCommand.getSourceRegister()
            .equals(secondCommand.getSourceRegister());

        boolean areSourceRegisters2Equal
            = (firstCommand.getSourceRegister2() == null
            && secondCommand.getSourceRegister2() == null)
            || (firstCommand.getSourceRegister2() != null)
            && firstCommand.getSourceRegister2()
            .equals(secondCommand.getSourceRegister2());

        boolean doBothHaveSameAddressing
            = (firstCommand.isPreIndexAddressing()
            && secondCommand.isPreIndexAddressing())
            || (!firstCommand.isPreIndexAddressing()
            && !secondCommand.isPreIndexAddressing());

        boolean areValuesEqual
            = (firstCommand.getValue() == null
            && secondCommand.getValue() == null)
            || (firstCommand.getValue() != null)
            && firstCommand.getValue()
            .equals(secondCommand.getValue());

        boolean areRegisterOffsetsEqual
            = (firstCommand.getRegisterOffset() == null
            && secondCommand.getRegisterOffset() == null)
            || (firstCommand.getRegisterOffset() != null)
            && firstCommand.getRegisterOffset()
            .equals(secondCommand.getRegisterOffset());

        boolean areAddressingTypesEqual = firstCommand.getAddressType()
            .equals(secondCommand.getAddressType());

        return (areConditionFieldsEqual
            && areDataSizesEqual
            && areDestinationRegistersEqual
            && areSourceRegisters1Equal
            && areSourceRegisters2Equal
            && doBothHaveSameAddressing
            && areValuesEqual
            && areRegisterOffsetsEqual
            && areAddressingTypesEqual);

    }

    public static boolean doLoadAndLogicalCommandShareRegister(
        LdrCommand ldrCommand, LogicalCommand logicalCommand) {

        return ldrCommand.getDestinationRegister().equals(
            logicalCommand.getSourceRegister2());
    }


    public static boolean doMoveAndLogicalCommandShareRegister(
        MovCommand movCommand, LogicalCommand logicalCommand) {

        return movCommand.getDestinationRegister().equals(
            logicalCommand.getSourceRegister2());
    }


    public static boolean doLoadAndComparisonCommandShareRegister(
        LdrCommand ldrCommand, ComparisonCommand comparisonCommand) {

        return ldrCommand.getDestinationRegister().equals(
            comparisonCommand.getSourceRegister2());
    }

    public static boolean doMoveAndComparisonCommandShareRegister(
        MovCommand movCommand, ComparisonCommand comparisonCommand) {

        return movCommand.getDestinationRegister().equals(
            comparisonCommand.getSourceRegister2());
    }

    public static boolean doLoadAndMoveCommandShareRegister(
        LdrCommand ldrCommand, MovCommand movCommand) {

        return ldrCommand.getDestinationRegister().equals(
            movCommand.getSourceRegister());
    }
}
