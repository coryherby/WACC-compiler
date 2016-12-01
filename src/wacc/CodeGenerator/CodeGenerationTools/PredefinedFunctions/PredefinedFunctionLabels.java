package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;

public enum PredefinedFunctionLabels {

    READ_INT_FUNCTION("p_read_int"),
    READ_CHAR_FUNCTION("p_read_char"),
    PRINT_STRING("p_print_string"),
    PRINT_INT("p_print_int"),
    PRINT_BOOL("p_print_bool"),
    PRINT_REFERENCE("p_print_reference"),
    PRINT_LN("p_print_ln"),
    CHECK_ARRAY_BOUNDS("p_check_array_bounds"),
    THROW_RUNTIME_ERROR("p_throw_runtime_error"),
    DIVIDE_BY_ZERO("p_check_divide_by_zero"),
    FREE_PAIR("p_free_pair"),
    CHECK_NULL_POINTER("p_check_null_pointer"),
    THROW_OVERFLOW_ERROR("p_throw_overflow_error"),
    TROW_RUNTIME_ERROR_FREE("p_throw_runtime_error_free");


    private String label;

    PredefinedFunctionLabels(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}