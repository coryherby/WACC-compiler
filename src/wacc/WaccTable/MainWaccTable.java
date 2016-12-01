package wacc.WaccTable;

import wacc.SemanticErrorDetector.WaccTypes.Type;

import java.util.LinkedList;

public class MainWaccTable {

    public static final int STACK_SIZE_4 = 4;
    public static final int STACK_SIZE_1 = 1;

    private static final int STACK_SIZE_INT = STACK_SIZE_4;
    private static final int STACK_SIZE_CHAR = STACK_SIZE_1;
    private static final int STACK_SIZE_STRING = STACK_SIZE_4;
    private static final int STACK_SIZE_BOOL = STACK_SIZE_1;
    private static final int STACK_SIZE_ARRAY = STACK_SIZE_4;
    private static final int STACK_SIZE_PAIR = STACK_SIZE_4;

    private static MainWaccTable mainWaccTable = new MainWaccTable();
    private SymbolTable symbolTable;
    private FunctionTable functionTable;

    private MainWaccTable() {
        // Exists only to defeat instantiation
        symbolTable = new SymbolTable(null, new LinkedList<>());
        functionTable = new FunctionTable();
    }

    public static MainWaccTable getInstance() {
        return mainWaccTable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public FunctionTable getFunctionTable() {
        return functionTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
       this.symbolTable = symbolTable;
    }

    /*
      Representation for debugging
    */

    public String generateSymbolTableRepresentation() {
        return symbolTable.generateSymbolTableRepresentation(0);
    }

    public static int calculateMemorySize(Type type) {
        switch(type.getWaccType()) {
            case INT : return STACK_SIZE_INT;
            case CHAR : return STACK_SIZE_CHAR;
            case STRING : return STACK_SIZE_STRING;
            case BOOL : return STACK_SIZE_BOOL;
            case ARRAY : return STACK_SIZE_ARRAY;
            case PAIR : return STACK_SIZE_PAIR;

            default : throw new RuntimeException("Unexpected type of variable");
        }
    }
}


