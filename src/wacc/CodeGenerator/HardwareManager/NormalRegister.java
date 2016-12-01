package wacc.CodeGenerator.HardwareManager;

public enum NormalRegister implements Register {

    R0, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
