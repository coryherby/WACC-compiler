package wacc.SemanticErrorDetector.WaccTypes;

import antlr.WaccParser.BaseTypeContext;

public class BaseType extends Type {

    public BaseType(WaccType type) {
        super(type);
    }

    // Method to create a BaseType - have to use the TypeBuilder Class
    // to create a BaseType in program
    public static BaseType createBaseType(BaseTypeContext baseContext) {

        WaccType t;

        if (baseContext.INT() != null) {
            t =  WaccType.INT;
        } else if (baseContext.BOOL() != null) {
            t = WaccType.BOOL;
        } else if (baseContext.CHAR() != null) {
            t = WaccType.CHAR;
        } else if (baseContext.STRING() != null) {
            t = WaccType.STRING;
        } else {
            return null;
        }

        return new BaseType(t);
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof BaseType) {

            BaseType t = (BaseType) o;
            return t.getWaccType() == mType;

        } else if (o instanceof PairElemType) {

            PairElemType pairElemType = (PairElemType) o;
            return equals(pairElemType.getPairElementType());

        } else if (o instanceof ArrayType && mType == WaccType.STRING) {

            ArrayType array = (ArrayType) o;

            if (array.getNestedType().getWaccType() == WaccType.CHAR) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {

        return getWaccType().hashCode();
    }
}
