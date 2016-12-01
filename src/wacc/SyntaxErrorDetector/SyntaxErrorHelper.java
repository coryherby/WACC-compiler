package wacc.SyntaxErrorDetector;

import antlr.WaccParser.StatementContext;

public class SyntaxErrorHelper {

    protected boolean hasReturnStatement(StatementContext ctx) {
    	if (isReturnStatement(ctx)) {
    		return true;
    	} else if (isExitStatement(ctx)) {
    		return true;
    	} else if (isIfStatement(ctx)) {
    		return hasReturnStatement(ctx.statement(0)) 
    		    && hasReturnStatement(ctx.statement(1));
    	} else if (isTwoStatements(ctx)) {
    		return hasReturnStatement(ctx.statement(0)) 
    		    || hasReturnStatement(ctx.statement(1));
    	} else {
    		return false;
    	}
    }

	private boolean isReturnStatement(StatementContext ctx) {
		return (ctx.RETURN() != null);
    }
    
    private boolean isExitStatement(StatementContext ctx) {
    	return (ctx.EXIT() != null);
    }
	
    private boolean isIfStatement(StatementContext ctx) {
    	return (ctx.IF() != null);
    }
    
    private boolean isTwoStatements(StatementContext ctx) {
    	return ((ctx.statement(0) != null) && (ctx.statement(1) != null));
	}
}
