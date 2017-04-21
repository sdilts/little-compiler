package littleCompiler.ast;

public class Assign implements ITree {
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
	    System.err.println("Something's wrong");
	}
    }

    public boolean isFull() {
	return expr != null && location != null;
    }

    public String toString() {
	return location + " = " + expr;
    }
}