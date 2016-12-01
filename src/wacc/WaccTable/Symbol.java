package wacc.WaccTable;

import wacc.SemanticErrorDetector.WaccTypes.Type;

public class Symbol {

    private String name;
    private Type type;
    private int offset;
    private int memorySize;

    // Optimisation
    private String value;

    public Symbol(String name, Type type) {
        this.type = type;
        this.name = name;
        this.value = null;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public void setOffset(int mOffset) {
        this.offset = mOffset;
    }

    public void setType(Type mType) {
        this.type = mType;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public int getOffset() { return offset; }

    public Type getType() {
        return type;
    }

    /*
      Optimisation
    */

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /*
      Representation for debugging
    */

    public String generateSymbolRepresentation() {

        return "Name: " + name + ", "
            + "Type: " + type.toString() + ", "
            + "Offset: " + offset + ", "
            + "MemorySize: " + memorySize + ", "
            + "Value: " + value;
    }
}
