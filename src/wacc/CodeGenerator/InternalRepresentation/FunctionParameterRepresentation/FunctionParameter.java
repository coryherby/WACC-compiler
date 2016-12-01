package wacc.CodeGenerator.InternalRepresentation.FunctionParameterRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.SemanticErrorDetector.WaccTypes.Type;

import java.util.ArrayList;
import java.util.List;

public class FunctionParameter {

    private Type type;
    private String identifier;

    public FunctionParameter(Type type,
                             String identifier) {

        this.type = type;
        this.identifier = identifier;
    }

    public List<Command> generateCommandsForFunctionParameter() {
        HardwareManager hardwareManager = HardwareManager.getInstance();
        hardwareManager.registerInitialisationIdentifier(identifier);

        return new ArrayList<>();
    }

    /*
      Representation for debugging
    */

    public String generateRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            "FunctionParameter",
            new String[] {
                "Type",
                "Identifier"},
            new String[] {
                indentDepth + type.toString(),
                indentDepth + identifier}
        );
    }

}
