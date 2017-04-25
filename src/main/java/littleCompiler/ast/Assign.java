package littleCompiler.ast;

import symbolTable.SymbolStack;

public class Assign implements IStmt {
    public MathExpression expr;
    public String location;


    /**
     * id first, then expression
     **/
    public void addChild(Object child) {
	if(location == null) {
	    location = (String) child;
	} else if(expr == null) {
	    expr = (MathExpression) child;
	} else {
	    System.err.println("Something's wrong in assign");
	}
    }

    public boolean isFull() {
	return expr != null && location != null;
    }

    public void print() {
	System.out.println(this);

    }

    public String toString() {
	return location + " = " + expr;
    }

    public StringBuilder flatten(SymbolStack symbols) {
	System.out.println("Assign.flatten() does nothing yet");
	return new StringBuilder();
    }
}
