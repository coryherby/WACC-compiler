package wacc.SemanticErrorDetector.ErrorManager;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import wacc.SemanticErrorDetector.HelperSemantic.GeneralHelper;

class ErrorHelper {

    public static String toText(ParserRuleContext parserRuleContext) {

        return toTextFromTo(
            parserRuleContext.getStart(), parserRuleContext.getStop());
    }

    public static String toTextFromTo(Token beginning, Token end) {

        CharStream charStream = beginning.getInputStream();
        Interval interval = new Interval(beginning
            .getStartIndex(), end.getStopIndex());
        return charStream.getText(interval);
    }

    public static <E extends ParserRuleContext, T extends ParserRuleContext>
        String toTextFirstContextFound(E initial, Class<T> returned) {

        T statement = GeneralHelper.
            <E, T>contextFromInitialContext(initial, returned);
        return toTextFromTo(statement.getStart(), statement
            .getStop());
    }

}