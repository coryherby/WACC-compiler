package wacc.SemanticErrorDetector.WaccTypes;

public class NullType extends Type {

    public NullType() {
        super(WaccType.NULL);
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof PairElemType) {

            PairElemType pairElemType = (PairElemType) o;

            if (pairElemType.getWaccType() == WaccType.PAIR) {
                return true;
            }
        }

        return o instanceof PairType || o instanceof NullType;
    }

    @Override
    public String toString() {
        return WaccType.PAIR.toString();
    }
}
