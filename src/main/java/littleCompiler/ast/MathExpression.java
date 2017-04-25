package littleCompiler.ast;

public class MathExpression implements ITree {
    public String terminal;
    public String type;
    public MathExpression left;
    public MathExpression right;
    public boolean isOperator;

    public MathExpression(String terminal) {
	this.terminal = terminal;
	isOperator = terminal.matches("\\+|\\*|/|-|%|");
    }

    public MathExpression(String terminal, String type) {
	this.terminal = terminal;
	this.type = type;
    }

    public void addChild(Object child) {
	if(left == null) {
	    left = (MathExpression) child;
	    determineType(left);
	} else if (right == null) {
	    right = (MathExpression) child;
	    determineType(right);
	} else {
	    System.err.println("We messed up bad at" + terminal + " with child " + child);
	    System.err.println(this);
	}
    }

    private void determineType(MathExpression e) {
	if(this.type == null) {
	    this.type = e.type;
	} else if(!e.type.equals(this.type)) {
	    //throw new TypeMismatchException("Bad Mismatched type: " + e.type + " and " + this.type);
	    System.err.println("Bad Mismatched type: " + e.type + " and " + this.type);
	    System.exit(1);
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
