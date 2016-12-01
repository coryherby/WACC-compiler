package wacc.WaccTable;

import wacc.SemanticErrorDetector.WaccTypes.Type;

import java.util.List;

public class Function {

    private String name;
    private Type returnType;
    private List<Type> parameterList;

    public Function(String name, Type type, List<Type> parameterList) {
        this.name = name;
        returnType = type;
        this.parameterList = parameterList;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type mReturnType) {
        this.returnType = mReturnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public List<Type> getParameterList() {
        return parameterList;
    }


}
