package wacc.SyntaxErrorDetector;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaccParserSyntaxErrorStrategy extends DefaultErrorStrategy {

    public WaccParserSyntaxErrorStrategy() {
        super();
    }

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
    	
        if(!this.inErrorRecoveryMode(recognizer)) {
        	
            this.beginErrorCondition(recognizer);
            
            if(e instanceof NoViableAltException) {
            	
                this.reportNoViableAlternative(recognizer,
                    (NoViableAltException)e);
                
            } else if(e instanceof InputMismatchException) {
            	
                this.reportInputMismatch(recognizer,
                    (InputMismatchException)e);
                
            } else if(e instanceof FailedPredicateException) {
            	
                this.reportFailedPredicate(recognizer,
                    (FailedPredicateException)e);
                
            } else {
            	
                System.err.println("Unknown recognition error type: "
                    + e.getClass().getName() + ".");
                recognizer.notifyErrorListeners(e.getOffendingToken(),
                    e.getMessage(), e);
            }

        }
    }

    @Override
    protected void reportNoViableAlternative(@NotNull Parser recognizer,
                                             @NotNull NoViableAltException e) {
    	
        TokenStream tokens = recognizer.getInputStream();
        String input;
        
        if (tokens != null) {
        	
            if (e.getStartToken().getType() == -1) {
                input = "<EOF>";              
            } else { 	
                input = tokens.getText(e.getStartToken(),e.getOffendingToken());
            }
            
        } else {
        	
            input = "<unknown input>";
        }

        String msg = "No viable alternative at input " +
            this.escapeWSAndQuote(input) + ".";
        recognizer.notifyErrorListeners(e.getOffendingToken(), msg, e);
    }

    @Override
    protected void reportInputMismatch(@NotNull Parser recognizer,
                                       @NotNull InputMismatchException e) {

        List<String> predictionList = predictExpression(e.getOffendingToken(),
            e.getExpectedTokens(),recognizer);
        String msg;
        
        if (predictionList.size() == 1 && !checkThen(e, predictionList)) {
        	
            msg = "Mismatched input " +
                this.getTokenErrorDisplay(e.getOffendingToken()) +
                    ", perhaps meant " + predictionList.get(0) + ".";

        } else {
        	
            msg = "Mismatched input " +
                this.getTokenErrorDisplay(e.getOffendingToken()) +
                    ", expecting " + e.getExpectedTokens().
                        toString(recognizer.getTokenNames())
                            + ".";
        }
        
        recognizer.notifyErrorListeners(e.getOffendingToken(), msg, e);
    }

    @Override
    protected void reportFailedPredicate(@NotNull Parser recognizer,
                                         @NotNull FailedPredicateException e) {
        String ruleName = recognizer.getRuleNames()
            [recognizer.getContext().getRuleIndex()];
        String msg = "Rule " + ruleName + " " + e.getMessage() + ".";
        recognizer.notifyErrorListeners(e.getOffendingToken(), msg, e);
    }

    @Override
    protected void reportUnwantedToken(@NotNull Parser recognizer) {
        if(!this.inErrorRecoveryMode(recognizer)) {
        	
            this.beginErrorCondition(recognizer);
            Token t = recognizer.getCurrentToken();
            String tokenName = this.getTokenErrorDisplay(t);
            IntervalSet expecting = this.getExpectedTokens(recognizer);
            String msg = "Extraneous input " + tokenName + " expecting " +
                expecting.toString(recognizer.getTokenNames()) + ".";
            recognizer.notifyErrorListeners(t, msg, (RecognitionException)null);
        }
    }

    @Override
    protected void reportMissingToken(@NotNull Parser recognizer) {
        if(!this.inErrorRecoveryMode(recognizer)) {
        	
            this.beginErrorCondition(recognizer);
            Token t = recognizer.getCurrentToken();
            IntervalSet expecting = this.getExpectedTokens(recognizer);
            String msg = "Missing " +
                expecting.toString(recognizer.getTokenNames()) + " at " +
                    this.getTokenErrorDisplay(t) + ".";
            recognizer.notifyErrorListeners(t, msg, (RecognitionException)null);
        }
    }

    protected String getTokenErrorDisplay(Token t) {
        if (t == null) {
            return "<no token>";
        } else {
            String s = this.getSymbolText(t);
            if (s == null) {
                if (this.getSymbolType(t) == -1) {
                    s = "<EOF>";
                } else {
                    s = "<" + this.getSymbolType(t) + ">";
                }
            }

            return this.escapeWSAndQuote(s);
        }
    }
    
	private boolean checkThen(@NotNull InputMismatchException e,
		List<String> predictionList) {
		
		return this.getTokenErrorDisplay(e.getOffendingToken()).equals("else")
			&& predictionList.get(0).contains("then");
	}

	private String elementName(String[] tokenNames, int a) {
		if (a == -1) {
			return "<EOF>";
		} else if (a == -2) {
			return "<EPSILON>";
		} else {
			return tokenNames[a];
		}
	}

	private List<String> predictExpression(Token offendingToken,
		IntervalSet expectedTokens, Parser recognizer) {

		List<String> expected = new ArrayList<String>();

		if (expectedTokens.getIntervals() != null
			&& !expectedTokens.getIntervals().isEmpty()) {

			Iterator<?> iter = expectedTokens.getIntervals().iterator();

			while (iter.hasNext()) {
				
				Interval I = (Interval) iter.next();
				int a = I.a;
				int b = I.b;
				
				if (a == b) {
					expected.add(elementName(recognizer.getTokenNames(), a));
				} else {
					for (int i = a; i <= b; ++i) {
						expected.add(elementName(recognizer.getTokenNames(),i));
					}
				}
			}
		}

		List<String> list = getPrediction(offendingToken, expected, 1);

		clearAndCopy(expected, list);

		if (expected.size() > 1) {
			list = getPrediction(offendingToken, expected, 2);
			clearAndCopy(expected, list);
		}

		return expected;
	}

	private void clearAndCopy(List<String> expected, List<String> list) {
		expected.clear();
		for (String s : list) {
			expected.add(s);
		}
	}

	private List<String> getPrediction(Token offendingToken,
		List<String> expected, int i) {

		List<String> list = new ArrayList<String>();
		Iterator<String> stringIterator = expected.iterator();
		
		while (stringIterator.hasNext()) {
			
			String str = stringIterator.next();

			if (str.contains(offendingToken.getText().substring(0, i))) {
				list.add(str);
			}
		}

		return list;
	}

}
