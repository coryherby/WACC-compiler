package wacc.WaccTable;

import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.Type;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SymbolTable {
    
    private HashMap<String, Symbol> symbolList;
    private SymbolTable parent;
    private List<SymbolTable> children;
    private int childToVisitNumber;
    private int totalOffset;
    private int currentOffset;
    private Set<String> alreadyInitialised;
    private Set<String> alreadyFreedReference;
    private HashMap<String, Set<String>> linkedReference;

    public SymbolTable (SymbolTable parentSymbolTable,
                        List<SymbolTable> children) {

        this.symbolList =  new LinkedHashMap<>();
        this.parent = parentSymbolTable;
        this.children = children;
        this.childToVisitNumber = 0;
        this.totalOffset = 0;
        this.currentOffset = 0;
        this.alreadyInitialised = new HashSet<>();
        this.alreadyFreedReference = new HashSet<>();
        this.linkedReference = new LinkedHashMap<>();
    }

    // Adds element to SymbolTable
    public void put(String name, Type type) {
        symbolList.put(name, new Symbol(name, type));
    }

    // Get Element from SymbolTable Recursively
    public Symbol get(String name) {
        for (SymbolTable table = this ; table != null ;
            table = table.getParentScope()) {

            Symbol returnSymbol = table.symbolList.get(name);
            if (returnSymbol != null) {
                return returnSymbol;
            }
        }
        return null;
    }

    // Get Element from SymbolTable Recursively a certain number of times
    public Symbol getForXScope(String name, int maxNumberOfScope) {
        int numberOfScope = 0;
        for (SymbolTable table = this ; table != null ;
            table = table.getParentScope()) {
        	
            Symbol returnSymbol = table.symbolList.get(name);
            
            if (returnSymbol != null) {
                return returnSymbol;
            }
            if(numberOfScope >= maxNumberOfScope) {
                break;
            }
            numberOfScope++;
        }
        return null;
    }

    // Return ParentScope
    public SymbolTable getParentScope() {
        return parent;
    }

    public void addChildren(SymbolTable symbolTable) {
        children.add(symbolTable);
    }

    // Remove selected child
    public void removeChildren(int index) {
        children.remove(index);
    }

    /*
      For CodeGenerator use only
    */

    // Returns children list
    public List<SymbolTable> getChildren() {
        return children;
    }

    // Set parent of this SymbolTable
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    // Return next symbolTable in a logical order, used by codeGenerator
    public SymbolTable getNextSymbolTable(int scopeShift) {

        if (children.size() == 0 || childToVisitNumber == children.size()) {

            // We don't stop at the top node
            if (parent.getParentScope() == null) {
                return parent.getNextSymbolTable(scopeShift);
            }
            return parent;
        }

        SymbolTable nextSymbolTable = children.get(childToVisitNumber);

        childToVisitNumber++;

        // Initialize nextSymbolTable offSet
        nextSymbolTable.initializeOffsets(scopeShift);

        return nextSymbolTable;
    }

    // Initialise the Offset of all variable in this
    // scope with a shift in particular cases
    private void initializeOffsets(int shift) {

        Set<String> variableNames = symbolList.keySet();

        List<Symbol> variables
            = variableNames.stream().map(variableName ->
            symbolList.get(variableName)).collect(Collectors.toList());

        Consumer<Symbol> initialiseOffset = variable -> {
            int memorySize = MainWaccTable
                .calculateMemorySize(variable.getType());
            variable.setMemorySize(memorySize);
            totalOffset += memorySize;
        };

        // Count the total offset
        variables.forEach(initialiseOffset);

        currentOffset = totalOffset;
        currentOffset += shift;

        // Set the offset for every variable
        variables.forEach(this::setVariableCurrentOffset);
    }

    // Set offset for a specific variable
    private int setVariableCurrentOffset(Symbol variable) {

        currentOffset -= variable.getMemorySize();
        variable.setOffset(currentOffset);

        return currentOffset;
    }

    // Return total offset of this scope
    public int getTotalOffset() {
        return totalOffset;
    }

    // Returns offset in this scope for a specific variable
    public int getVariableTotalOffset(String variableName) {
        Symbol variable = symbolList.get(variableName);
        Boolean isInitialisedIdentifier =
            alreadyInitialised.contains(variableName);

        if(variable == null || !isInitialisedIdentifier) {
            return totalOffset
                + getParentScope().getVariableTotalOffset(variableName);
        }

        return variable.getOffset();
    }

    // Return the memory size of the variable depending on the type
    public int getVariableMemorySize(String variableName) {
        Symbol variable = getSymbolRecursively(variableName);

        return variable.getMemorySize();
    }

    // Return the type of a specific variable in this scope
    public Type getVariableType(String variableName) {
        Symbol variable = getSymbolRecursively(variableName);

        return variable.getType();
    }

    // Return the first occurrence of a symbol
    // found by going recursively to parent scope
    private Symbol getSymbolRecursively(String variableName) {
        Symbol variable = symbolList.get(variableName);
        Boolean isInitialisedIdentifier =
            alreadyInitialised.contains(variableName);

        if(variable == null || !isInitialisedIdentifier) {
            return getParentScope().getSymbolRecursively(variableName);
        }

        return variable;
    }

    // Add variable to initialised variable, used in codeGenerator
    // to keep track of initialisation
    public void registerInitialisationIdentifier(String identifier) {
        alreadyInitialised.add(identifier);
    }

    // Return the offset for function scope by going up the tree
    public int getTotalOffsetForFunctionScope() {
        SymbolTable parent = getParentScope();
        SymbolTable grandGrandParent = parent.getParentScope().getParentScope();

        if(grandGrandParent == null) {
            return totalOffset;
        } else {
            return parent.getTotalOffsetForFunctionScope();
        }
    }

    /*
      Manage free Reference use to keep track of freed
      variables in codeGenerator
    */

    public void addToFreedReference(String identifier) {
        alreadyFreedReference.add(identifier);
    }

    public void removeFreedReference(String identifier) {
        alreadyFreedReference.remove(identifier);
    }

    public boolean hasAlreadyBeenFreed(String identifier) {
        Boolean isInitialisedIdentifier = isAlreadyInitialised(identifier);

        if(isInitialisedIdentifier) {
            return true;
        } else if(this.parent != null) {
            return getParentScope().hasAlreadyBeenFreed(identifier);
        }

        return false;
    }

    /*
      Keep Track of the linked variable in semantic for further use
      in codeGenerator in the case of double free
    */

    private Boolean isAlreadyInitialised(String identifier) {
        Boolean isInitialisedIdentifier =
            alreadyFreedReference.contains(identifier);

        if(linkedReference.get(identifier) != null) {
            for (String currentIdentifier : linkedReference.get(identifier)) {
                isInitialisedIdentifier = isInitialisedIdentifier
                    || alreadyFreedReference.contains(currentIdentifier);
            }
        }
        return isInitialisedIdentifier;
    }


    public void addLinkedReferenceForAssignement(
        String variable1, String variable2) {

        Set<String> setVariable1 = linkedReference.get(variable1);

        if (setVariable1 != null) {
            
            for (String current : setVariable1) {
                if (linkedReference.containsKey(current)) {
                    Set<String> currentSet = linkedReference.get(current);
                    currentSet.remove(variable1);
                    linkedReference.put(current, currentSet);
                }
            }

            addLinkedReference(variable1, variable2);
        }

    }

    public void addLinkedReference(String variable1, String variable2) {
        Set<String> setVariable1 = new HashSet<>();
        if(linkedReference.get(variable1) != null) {
            setVariable1.addAll(linkedReference.get(variable1));
        }
        if(!setVariable1.contains(variable2)) {
            setVariable1.add(variable2);
        }

        Set<String> setVariable2 = new HashSet<>();
        if(linkedReference.get(variable2) != null) {
            setVariable2.addAll(linkedReference.get(variable2));
        }
        if(!setVariable2.contains(variable1)) {
            setVariable2.add(variable1);
        }

        linkedReference.put(variable1, setVariable1);
        linkedReference.put(variable2, setVariable2);
    }

    /*
      Representation for debugging
    */
    
    public String generateSymbolTableRepresentation(int depthCount) {

        StringBuilder scopesRepresentation = new StringBuilder();

        for (SymbolTable child : children) {
            scopesRepresentation.append(
                child.generateSymbolTableRepresentation(
                    depthCount + RepresentationFormatter.INDENT_ONCE));
            scopesRepresentation.append("\n");
        }

        return generateRepresentation(depthCount,
            scopesRepresentation.toString());
    }

    public String generateSymbolTableLocalRepresentation() {
        return generateRepresentation(0, "");
    }

    private String generateRepresentation(
        int depthCount, String scopesRepresentation) {

        return TableFormatter.generateSymbolTableRepresentation(
            depthCount,
            children.size(),
            symbolList,
            scopesRepresentation,
            childToVisitNumber,
            totalOffset,
            currentOffset);
    }
}