package wacc.SemanticErrorDetector.WaccTypes;

import antlr.WaccParser.PairTypeContext;

public class PairType extends Type {

    private PairElemType mLeftType;
    private PairElemType mRightType;

    public PairType(PairElemType leftType, PairElemType rightType) {
        super(WaccType.PAIR);
        mLeftType = leftType;
        mRightType = rightType;
    }

    // Method to create an PairType - have to use the TypeBuilder Class
    // to create a Type in program
    public static PairType createPairType(PairTypeContext pairTypeContext) {

        return new PairType(
            PairElemType.createPairElemType(
                pairTypeContext.pairElementType(0)),
            PairElemType.createPairElemType(
                pairTypeContext.pairElementType(1)));
    }

    public PairElemType getLeftType() {
        return mLeftType;
    }

    public PairElemType getRightType() {
        return mRightType;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof PairType) {
            PairType t = (PairType) o;
            return mType == t.getWaccType()
                && mRightType.equals(t.getRightType())
                && mLeftType.equals(t.getLeftType());

        } else if (o instanceof PairElemType) {
            PairElemType pairElemType = (PairElemType) o;
            return pairElemType.isPairElemOfTypePair();

        } else if (o instanceof NullType) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + mLeftType.hashCode() + mRightType.hashCode();
    }

    @Override
    public String toString() {
        return "(" + mLeftType.toString() + ", "
            + mRightType.toString() + ")";
    }

}
