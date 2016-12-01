package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.AddCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignLhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.ArrayType;
import wacc.SemanticErrorDetector.WaccTypes.BaseType;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;
import wacc.WaccTable.MainWaccTable;

import java.util.ArrayList;
import java.util.List;

public class ArrayElem implements Expression, AssignRhs, AssignLhs {

    private String identifier;
    private List<Expression> arrayAccesses;

    public ArrayElem(String identifier,
                     List<Expression> arrayAccesses) {

        this.identifier = identifier;
        this.arrayAccesses = arrayAccesses;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();

        // gets a free register and add it in the storage register stack
        NormalRegister arrayReg = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(arrayReg);

        // creates variable needed in commands
        ConditionCode always = ConditionCode.AL;
        SpecialRegister sp = SpecialRegister.SP;
        DataSize basic = DataSize.W;

        // gets variable offset and its string value
        int offset = hardwareManager.getVariableStackOffset(identifier);
        String offsetString = String.valueOf(offset);

        // add the command for the array address in the stack and shifts it
        // using the correct offset
        AddCommand arrayAddress
            = new AddCommand(always, arrayReg, sp, offsetString);
        commands.add(arrayAddress);

        // gets R0 and R1 registers from the HardwareManager
        NormalRegister R0 = hardwareManager.getRegister0();
        NormalRegister R1 = hardwareManager.getRegister1();

        int exprOffset = 0;

        // generates commands of the the expression in arrayAccess
        for (Expression arrayAccess : arrayAccesses) {

            // generates all the commands of the expression
            List<Command> exprs = arrayAccess.generateCommandsForExpression();
            commands.addAll(exprs);

            // loads the array in the memory
            LdrCommand loadArray
                = new LdrCommand(always, basic, arrayReg, arrayReg);
            commands.add(loadArray);

            // gets the expression register
            NormalRegister exprReg = hardwareManager.getStorageRegister();

            // mov the value of the expression register into the register R0
            MovCommand movExpr = new MovCommand(always, R0, exprReg);
            commands.add(movExpr);

            // mov the arrayRegister into R1
            MovCommand movArray = new MovCommand(always, R1, arrayReg);
            commands.add(movArray);

            // checks if index is out of bounds
            BlCommand checkArrayBounds = new BlCommand(always,
                PredefinedFunctionLabels.CHECK_ARRAY_BOUNDS.toString());
            commands.add(checkArrayBounds);

            // ask to creates a new method to check if out of bounds
            PredefinedFunctionGenerator codeGenHelper
                = PredefinedFunctionGenerator.getInstance();
            codeGenHelper.addPredefinedFunction(PredefinedFunctionLabels.
                CHECK_ARRAY_BOUNDS);

            // mov array register in memory and offset it by 4
            AddCommand getArrayStartingAddress = new AddCommand(always,
                arrayReg, arrayReg, "4");
            commands.add(getArrayStartingAddress);

            // get the array element at the requested index

            Type arrayType = getArrayType();

            if (arrayType.equals(new BaseType(WaccType.CHAR))
                || arrayType.equals(new BaseType(WaccType.BOOL))) {

                AddCommand getArrayElement = new AddCommand(always, arrayReg,
                    arrayReg, exprReg);
                commands.add(getArrayElement);

                exprOffset = MainWaccTable.STACK_SIZE_1;

            } else {

                AddCommand getArrayElement = new AddCommand(always, arrayReg,
                    arrayReg, exprReg, "LSL #2");
                commands.add(getArrayElement);
                exprOffset = MainWaccTable.STACK_SIZE_4;

            }

            // frees the expression register
            hardwareManager.freeRegister(exprReg);

        }

        LdrCommand loadElemIntoArrayRegister;

        if (exprOffset == MainWaccTable.STACK_SIZE_1) {

            // loads the array element into the array register
            loadElemIntoArrayRegister
                = new LdrCommand(ConditionCode.SAL, DataSize.B, arrayReg,
                arrayReg);

        } else {

            loadElemIntoArrayRegister
                = new LdrCommand(always, basic, arrayReg, arrayReg);

        }

        commands.add(loadElemIntoArrayRegister);

        return commands;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {
        return generateCommandsForExpression();
    }

    @Override
    public List<Command> generateCommandsForAssignLhs() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();

        ConditionCode always = ConditionCode.AL;
        SpecialRegister sp = SpecialRegister.SP;
        DataSize basic = DataSize.W;

        int offset = hardwareManager.getVariableStackOffset(identifier);
        String offsetString = String.valueOf(offset);

        // gets R0 and R1 registers from the HardwareManager
        NormalRegister R0 = hardwareManager.getRegister0();
        NormalRegister R1 = hardwareManager.getRegister1();

        NormalRegister arrayReg = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(arrayReg);

        // add the command for the array address in the stack and shifts it
        // using the correct offset
        AddCommand arrayAddress
            = new AddCommand(always, arrayReg, sp, offsetString);
        commands.add(arrayAddress);

        // generates commands of the the expression in arrayAccess
        for (Expression arrayAccess : arrayAccesses) {

            // generates all the commands of the expression
            List<Command> exprs = arrayAccess.generateCommandsForExpression();
            commands.addAll(exprs);

            // loads the array in the memory
            LdrCommand loadArray
                = new LdrCommand(always, basic, arrayReg, arrayReg);
            commands.add(loadArray);

            // gets the expression register
            NormalRegister exprReg = hardwareManager.getStorageRegister();

            // mov the value of the expression register into the register R0
            MovCommand movExpr = new MovCommand(always, R0, exprReg);
            commands.add(movExpr);

            // mov the arrayRegister into R1
            MovCommand movArray = new MovCommand(always, R1, arrayReg);
            commands.add(movArray);

            // checks if index is out of bounds
            BlCommand checkArrayBounds = new BlCommand(always,
                PredefinedFunctionLabels.CHECK_ARRAY_BOUNDS.toString());
            commands.add(checkArrayBounds);

            // ask to creates a new method to check if out of bounds
            PredefinedFunctionGenerator codeGenHelper
                = PredefinedFunctionGenerator.getInstance();
            codeGenHelper.addPredefinedFunction(PredefinedFunctionLabels.
                CHECK_ARRAY_BOUNDS);

            // mov array register in memory and offset it by 4
            AddCommand getArrayStartingAddress = new AddCommand(always,
                arrayReg, arrayReg, "4");
            commands.add(getArrayStartingAddress);

            // get the array element at the requested index

            Type arrayType = getArrayType();

            if (arrayType.equals(new BaseType(WaccType.CHAR))
                || arrayType.equals(new BaseType(WaccType.BOOL))) {

                AddCommand getArrayElement = new AddCommand(always, arrayReg,
                    arrayReg, exprReg);
                commands.add(getArrayElement);

            } else {

                AddCommand getArrayElement = new AddCommand(always, arrayReg,
                    arrayReg, exprReg, "LSL #2");
                commands.add(getArrayElement);
            }

            // frees the expression register
            hardwareManager.freeRegister(exprReg);

        }

        return commands;

    }

    private Type getArrayType() {
        HardwareManager hardwareManager = HardwareManager.getInstance();

        Type type = hardwareManager.getVariableType(identifier);

        if (type.equals(new BaseType(WaccType.STRING))) {
            type = new BaseType(WaccType.CHAR);
            return type;
        }

        while (((ArrayType) type).getNestedType()
            instanceof ArrayType) {

            type = ((ArrayType) type).getNestedArrayType();
        }

        type = ((ArrayType) type).getNestedType();

        return type;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "ArrayElem Assignement");
    }

    @Override
    public String generateLhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "ArrayElem Assigned");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "ArrayElem");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        StringBuilder arrayAccessesRepresentation = new StringBuilder();

        for (Expression arrayAccess : arrayAccesses) {
            arrayAccessesRepresentation.append(
                arrayAccess.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE));
            arrayAccessesRepresentation.append("\n");
        }

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            new String[]{
                "Identifier",
                "Array Accesses"},
            new String[]{
                indentDepth + identifier,
                arrayAccessesRepresentation.toString()}
        );
    }

}
