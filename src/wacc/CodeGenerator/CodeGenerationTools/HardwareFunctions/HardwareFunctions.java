package wacc.CodeGenerator.CodeGenerationTools.HardwareFunctions;

public enum HardwareFunctions {

    PRINTF_FUNCTION("printf"),
    MALLOC_FUNCTION("malloc"),
    FFLUSH_FUNCTION("fflush"),
    PUTS_FUNCTION("puts"),
    DIVIDE_FUNCTION("__aeabi_idiv"),
    DIVIDE_MOD_FUNCTION("__aeabi_idivmod");

    private String label;

    HardwareFunctions(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
