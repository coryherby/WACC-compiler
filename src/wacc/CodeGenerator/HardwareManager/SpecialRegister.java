package wacc.CodeGenerator.HardwareManager;

public enum SpecialRegister implements Register {

    SP, LR, PC;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
