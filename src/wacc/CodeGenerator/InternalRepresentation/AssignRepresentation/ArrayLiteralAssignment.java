package wacc.CodeGenerator.InternalRepresentation.AssignRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.HardwareFunctions.HardwareFunctions;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.StrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.BoolLiteral;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.CharLiteral;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteralAssignment implements AssignRhs {

    private static final int ARRAY_MEMORY_SIZE = 4;
    private static final int ONE_BYTE_MEMORY_SIZE = 1;
    private static final int FOUR_BYTE_MEMORY_SIZE = 4;

    private List<Expression> arrayElementList;

    public ArrayLiteralAssignment(List<Expression> arrayElementList) {
        this.arrayElementList = arrayElementList;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {

        ArrayList<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        // Getting the size of the array
        int size = arrayElementList.size();
        String valueSize = String.valueOf(size);

        // Getting the offset of an element in the array
        int arrayElementOffset = 0;

        if (size > 0) {

            Expression firstExpression = arrayElementList.get(0);

            // If char or bool, use only 1 byte
            if (firstExpression instanceof CharLiteral
                || firstExpression instanceof BoolLiteral) {
                arrayElementOffset = ONE_BYTE_MEMORY_SIZE;

            } else {
                arrayElementOffset = FOUR_BYTE_MEMORY_SIZE;
            }

        }

        // Getting amount of memory needed
        int memoryAllocation = ARRAY_MEMORY_SIZE + arrayElementOffset * size;
        String sizeStringValue = String.valueOf(memoryAllocation);

        ConditionCode al = ConditionCode.AL;
        DataSize w = DataSize.W;

        // Loading the memory needed into r0
        LdrCommand ldrMemory
            = new LdrCommand(al, w, register0, sizeStringValue);
        commands.add(ldrMemory);

        // Calling malloc to allocate space in memory
        BlCommand allocMem = new BlCommand(ConditionCode.AL,
            HardwareFunctions.MALLOC_FUNCTION.toString());
        commands.add(allocMem);

        // Moving into a new register the amount of memory needed
        NormalRegister storage = hardwareManager.getFreeRegister();
        MovCommand movStorageR0 = new MovCommand(al, storage, register0);
        commands.add(movStorageR0);

        int i = 0;
        int offSet = ARRAY_MEMORY_SIZE;

        for (Expression expression : arrayElementList) {

            commands.addAll(expression.generateCommandsForExpression());

            String offset;

            if (expression instanceof CharLiteral
                || expression instanceof BoolLiteral) {

                offset = String.valueOf(offSet + i);
                
            } else {
                offset = String.valueOf(offSet * (i + 1));
            }

            NormalRegister strRegister = hardwareManager.getStorageRegister();
            hardwareManager.freeRegister(strRegister);

            StrCommand str = new StrCommand(al, w, strRegister, storage,
                offset, true, false);

            commands.add(str);

            i++;

        }

        NormalRegister strRegister = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(strRegister);

        Command ldrNextRegister
            = new LdrCommand(al, w, strRegister, valueSize);
        commands.add(ldrNextRegister);


        StrCommand strNextRegInStorage
            = new StrCommand(al, w, strRegister, storage);
        commands.add(strNextRegInStorage);

        hardwareManager.addStorageRegister(storage);
        hardwareManager.freeRegister(strRegister);

        return commands;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        StringBuilder arrayElementsRepresentation = new StringBuilder();

        for (Expression arrayElement : arrayElementList) {
            arrayElementsRepresentation.append(
                arrayElement.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE));
            arrayElementsRepresentation.append("\n");
        }

        return RepresentationFormatter.generateRepresentation(
            depth,
            "ArrayLiteralAssignment",
            new String[] {
                "Array Elements"},
            new String[] {
                arrayElementsRepresentation.toString()}
        );
    }

}
