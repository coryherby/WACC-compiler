package wacc.SemanticErrorDetector.WaccTypes;

import antlr.WaccParser;

public class PairElemType extends Type {

    private Type mPairElementType;

    public PairElemType() {
        super(WaccType.PAIR_ELEM);
        mPairElementType = null;
    }

    public PairElemType(BaseType type) {
        super(WaccType.PAIR_ELEM);
        mPairElementType = type;
    }

    public PairElemType(ArrayType type) {
        super(WaccType.PAIR_ELEM);
        mPairElementType = type;
    }

    // Method to create an PairElemType - have to use the TypeBuilder Class
    // to create a Type in program
    public static PairElemType createPairElemType(
        WaccParser.PairElementTypeContext pairElemTypeContext) {

        if (pairElemTypeContext.arrayType() != null) {

            return new PairElemType(ArrayType.createArrayType(
                pairElemTypeContext.arrayType().type()));

        } else if (pairElemTypeContext.baseType() != null) {

            return new PairElemType(
                BaseType.createBaseType(pairElemTypeContext.baseType()));

        } else if (pairElemTypeContext.PAIR() != null) {

            return new PairElemType();
        }

        return null;
    }

    public boolean isPairElemOfTypePair() {
        return mPairElementType == null;
    }

    public Type getPairElementType() {
        return mPairElementType;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof PairElemType) {
            PairElemType t = (PairElemType) o;

            if (mPairElementType == null) {
                return t.getPairElementType() == null 
                    && mType == t.getWaccType();
            }

            return mType == t.getWaccType()
                && mPairElementType.equals(t.getPairElementType());

        } else if (o instanceof NullType) {

            if (mPairElementType == null) {

                return true;
            }

        } else if (o instanceof Type) {
            Type t = (Type) o;

            if (mPairElementType == null) {
                return t instanceof PairType;

            } else {
                return mPairElementType.equals(t);
            }

        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + mPairElementType.hashCode();
    }

    @Override
    public String toString() {
        return isPairElemOfTypePair()? "PAIR" : mPairElementType.toString();
    }
}
