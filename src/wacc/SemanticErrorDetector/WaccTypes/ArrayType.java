package wacc.SemanticErrorDetector.WaccTypes;

import antlr.WaccParser;

public class ArrayType extends Type {

    private Type mNestedType;

    public ArrayType(WaccType type) {
        super(type);
    }

    // Method to create an ArrayType - have to use the TypeBuilder Class
    // to create a Type in program
    public static ArrayType createArrayType(WaccParser.TypeContext typeContext){

        ArrayType arrayType = new ArrayType(WaccType.ARRAY);

        if (typeContext.type() != null) {

            arrayType.setNestedType(createArrayType(typeContext.type()));
            return arrayType;

        } else if (typeContext.baseType() != null) {

            arrayType.setNestedType(BaseType.createBaseType
            					       (typeContext.baseType()));
            return arrayType;

        } else if (typeContext.pairType() != null) {

            arrayType.setNestedType(PairType.createPairType
            					       (typeContext.pairType()));
            return arrayType;
        }

        return null;
    }

    public void setNestedType(Type nestedType) {
        mNestedType = nestedType;
    }

    public Type getNestedType() {
        return mNestedType;
    }

    public boolean hasNestedArrayType() {
        return mNestedType instanceof ArrayType;
    }

    public ArrayType getNestedArrayType() {
        return (ArrayType) mNestedType;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof ArrayType) {

            ArrayType arrayType = (ArrayType) o;
            return mNestedType.equals(arrayType.getNestedType());

        } else if (o instanceof PairElemType) {

            PairElemType pairElemType = (PairElemType) o;
            return equals(pairElemType.getPairElementType());

        } else if (o instanceof BaseType
            && mNestedType.getWaccType() == WaccType.CHAR) {

            return ((BaseType) o).getWaccType() == WaccType.STRING;
        }

        return false;
    }

    @Override
    public int hashCode() {

    	if (hasNestedArrayType()) {
    		return 2 * mNestedType.hashCode();
    	} else {
    		return super.hashCode();
    	}
    }

    @Override
    public String toString() {
        return mNestedType.toString() + "[]";
    }
}
