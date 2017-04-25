package littleCompiler.ast;

public class MathExpression implements ITree {
    public String terminal;
    public MathExpression left;
    public MathExpression right;
    public boolean isOperator;

    public MathExpression(String terminal) {
	this.terminal = terminal;
	isOperator = terminal.matches("\\+|\\*|/|-|%|");
    }

    public void addChild(Object child) {
	if(left == null) {
	    left = (MathExpression) child;
	} else if (right == null) {
	    right = (MathExpression) child;
	} else {
	    System.err.println("We messed up bad at" + terminal + " with child " + child);
	    System.err.println(this);
	}
    }

    public boolean isFull() {
	if(isOperator) {
	    return (left != null && right != null);
	} //else:
	return true;
    }

    public void print() {
	System.out.println(this);
    }

    @Override
    public String toString() {
	if(left == null && right == null) {
	    return terminal;
	} else {
	    return "(" + terminal + " " + left + " " + right + ")";
	}
    }
}