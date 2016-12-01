package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class CommaStatement implements Statement {

    private Statement firstStatement;
    private Statement secondStatement;

    public CommaStatement(Statement firstStatement,
                          Statement secondStatement) {

        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    @Override
    public List<Command> generateCommandsForStatement() {
        List<Command> commands = new ArrayList<>();

        commands.addAll(firstStatement.generateCommandsForStatement());
        commands.addAll(secondStatement.generateCommandsForStatement());

        return commands;
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
            "CommaStatement",
            new String[] {
                "FirstStatement",
                "SecondStatement"},
            new String[] {
                firstStatement.generateStatementRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE),
                secondStatement.generateStatementRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE)}
        );
    }

}
