package wacc.WaccTable;

import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Identification;
import wacc.SemanticErrorDetector.WaccTypes.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionTable {

    private HashMap<String, Function> functionList;

    public FunctionTable () {
        functionList =  new HashMap<String, Function>();
    }

    public void put(String name, Type type, List<Type> parameterList) {
        functionList.put(name, new Function(name, type, parameterList));
    }

    public Function get(String name) {
        return functionList.get(name);
    }

    public List<Integer> getParameterOffset(String functionName) {

        List<Integer> result = new ArrayList<>();

        List<Type> arguments =
            functionList.get(functionName).getParameterList();

        for(Type argument : arguments) {
            result.add(MainWaccTable.calculateMemorySize(argument));
        }

        return result;
    }


    public boolean hasFunction() {
        return functionList.size() != 0;
    }
}