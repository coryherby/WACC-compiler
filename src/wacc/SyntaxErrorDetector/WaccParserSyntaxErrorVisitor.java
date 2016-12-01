package wacc.SyntaxErrorDetector;

import antlr.WaccParser;
import antlr.WaccParser.FunctionContext;
import antlr.WaccParser.IntLiteralContext;
import antlr.WaccParserBaseVisitor;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.NotNull;

public class WaccParserSyntaxErrorVisitor<T>
    extends WaccParserBaseVisitor<T> {

    private WaccParser parser;

	public WaccParserSyntaxErrorVisitor(WaccParser parser) {
		super();
        this.parser = parser;
	}
	
    @Override
    public T visitIntLiteral(@NotNull IntLiteralContext ctx) {

        try {
            Integer.valueOf(ctx.getText());
        } catch (NumberFormatException e) {
            parser.notifyErrorListeners(ctx.getStart(),"Integer value " + 
                ctx.getText() + " is too large for a " +
                    "32-bit signed integer.", (RecognitionException) null);
        }

        return visitChildren(ctx);
    }
    
    @Override
    public T visitFunction (@NotNull FunctionContext ctx) {
    	
    	SyntaxErrorHelper helper = new SyntaxErrorHelper();
    	
    	if (!helper.hasReturnStatement(ctx.statement())) {
    		parser.notifyErrorListeners(ctx.getStart(), "Function " + 
    			ctx.IDENTIFICATION() + " is not ended with a return or " +
    				"an exit statement.", (RecognitionException) null);
    	}

    	return visitChildren(ctx);
    }
    
}                                                                                
                                                                                 