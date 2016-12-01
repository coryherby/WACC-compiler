package wacc.CodeGenerator.HardwareManager;

import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.WaccTable.MainWaccTable;
import wacc.WaccTable.SymbolTable;

import java.util.*;

public class HardwareManager {

    private static final int PROGRAM_COUNTER_ADDRESS_OFFSET = 4;

    private static HardwareManager hardwareManager = new HardwareManager();

    // Mapping of usable registers with their state (used/free)
    private Map<NormalRegister, Boolean> usableRegisters;
    private Stack<NormalRegister> storageRegisters;
    private SymbolTable currentStackScope;
    private int temporaryScopeOffset;
    private boolean memoryAddressBeingFreed;

    private HardwareManager() {
        usableRegisters = createUsedRegisterMapping();
        storageRegisters = new Stack<>();
        currentStackScope = getStartingStackScope();
        temporaryScopeOffset = 0;
        memoryAddressBeingFreed = false;
    }

    public static HardwareManager getInstance() {
        return hardwareManager;
    }

    // Give us the next free usable register in the set of all usable registers
    // Register that is returned cannot be used until it has been free
    public NormalRegister getFreeRegister() {
        for (NormalRegister normalRegister : usableRegisters.keySet()) {
            if (usableRegisters.get(normalRegister)) {
                usableRegisters.put(normalRegister, false);
                return normalRegister;
            }
        }

        throw new RuntimeException(
            "HardwareManager.getFreeRegister() : no available registers");
    }

    // Free the register that was used, allowing it to be used later
    public void freeRegister(NormalRegister normalRegister) {
        usableRegisters.put(normalRegister, true);
    }

    // Add the current register on the stack of storage registers
    // The registered push on the stack holds data that should be used later
    public void addStorageRegister(NormalRegister storageRegister) {
        storageRegisters.push(storageRegister);
    }

    // Get the most recent storage register pushed on the stack
    public NormalRegister getStorageRegister() {
        return storageRegisters.pop();
    }

    // Tell the hardware manager to move to the next stack scope
    public void moveToNextStackScope() {
        currentStackScope = currentStackScope.getNextSymbolTable(0);
    }

    // Move to the next scope which is a function scope
    public void moveToNextStackScopeEnteringFunction() {
        currentStackScope = currentStackScope.getNextSymbolTable(
            PROGRAM_COUNTER_ADDRESS_OFFSET);
    }

    // Get the total offset needed for the stack variables for the current scope
    public int getTotalOffsetForCurrentScope() {
        return currentStackScope.getTotalOffset();
    }

    // Give back the variable offset in memory
    public int getVariableStackOffset(String variableName) {
        return currentStackScope.getVariableTotalOffset(variableName);
    }

    // Give back the variable memory size
    public int getVariableMemorySize(String variableName) {
        return currentStackScope.getVariableMemorySize(variableName);
    }

    // Give back the variable type which is being stored
    public Type getVariableType(String variableName) {
        return currentStackScope.getVariableType(variableName);
    }

    // Add variable to initialised variables list to avoid confusion
    // when getting variable name redefined in different scope
    public void registerInitialisationIdentifier(String identifier) {
        currentStackScope.registerInitialisationIdentifier(identifier);
    }

    // Return the total offset for current function scope
    public int getTotalOffsetForFunctionScope() {
        return currentStackScope.getTotalOffsetForFunctionScope();
    }

    /*
      Manage temporary offset, which is an offset unrelated to the SymbolTable
      created by specific ARM command
    */

    public int addToTemporaryOffset(int value) {
        temporaryScopeOffset += value;
        return temporaryScopeOffset;
    }

    public void resetTemporaryOffset() {
        temporaryScopeOffset = 0;
    }

    public int getTemporaryScopeOffset() {
        return temporaryScopeOffset;
    }

    /*
      Manage boolean keeping track of the use of a free statement,
      used by the visitor for double free cases
     */

    public boolean isMemoryAddressBeingFreed() {
        return memoryAddressBeingFreed;
    }

    public void setMemoryAddressBeingFreed(boolean memoryAddressBeingFreed) {
        this.memoryAddressBeingFreed = memoryAddressBeingFreed;
    }

    /*
      Keep track of variable that has been freed
    */

    public void addToFreedReference(String identifier) {
        currentStackScope.addToFreedReference(identifier);
    }

    public void removeFreedReference(String identifier) {
        currentStackScope.removeFreedReference(identifier);
    }

    public boolean hasAlreadyBeenFreed(String identifier) {
        return currentStackScope.hasAlreadyBeenFreed(identifier);
    }

    /*
      Helper methods used in initialisation of the Hardware manager
    */

    private static Map<NormalRegister, Boolean> createUsedRegisterMapping() {
        Map<NormalRegister, Boolean> registers = new LinkedHashMap<>();

        for (NormalRegister usableNormalRegister : getAllUsableRegisters()) {
            registers.put(usableNormalRegister, true);
        }

        return registers;
    }

    private static SymbolTable getStartingStackScope() {
        return  MainWaccTable.getInstance().getSymbolTable();
    }

    /*
      Helper methods to manage usable registers
    */

    public NormalRegister getRegister0() {
        return NormalRegister.R0;
    }

    public NormalRegister getRegister1() {
        return NormalRegister.R1;
    }

    public NormalRegister getRegister2() {
        return NormalRegister.R2;
    }

    public NormalRegister getRegister3() {
        return NormalRegister.R3;
    }

    private static List<NormalRegister> getAllUsableRegisters() {
        return new ArrayList<>(Arrays.asList(
            NormalRegister.R4,
            NormalRegister.R5,
            NormalRegister.R6,
            NormalRegister.R7,
            NormalRegister.R8,
            NormalRegister.R9,
            NormalRegister.R10,
            NormalRegister.R11));
    }
}
