package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class SkipStatement implements Statement {

    @Override
    public List<Command> generateCommandsForStatement() {
        return new ArrayList<>();
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateStatementRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            "SkipStatement",
            ""
        );
    }

}
