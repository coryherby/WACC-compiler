package wacc.SemanticErrorDetector.WaccTypes;

public abstract class Type {

    protected WaccType mType;

    public Type(WaccType type) {
        mType = type;
    }

    public WaccType getWaccType() {
        return mType;
    }

    @Override
    public String toString() {
        return getWaccType().toString();
    }
}
