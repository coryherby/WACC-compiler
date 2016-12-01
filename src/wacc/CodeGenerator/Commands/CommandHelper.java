package wacc.CodeGenerator.Commands;

import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;

public class CommandHelper {

    public static String getConditionCode(ConditionCode conditionField) {
        if (conditionField == ConditionCode.AL) {
            return "";
        } else if (conditionField == ConditionCode.SAL) {
            return "S";
        } else {
            return conditionField.toString();
        }
    }

    public static String getDataSize(DataSize dataSize) {
        if (dataSize == DataSize.W) {
            return "";
        } else {
            return dataSize.toString();
        }
    }
}
