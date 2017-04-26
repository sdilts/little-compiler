package littleCompiler.ast;

import symbolTable.SymbolStack;

public class MathExpression implements IReturnable {
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

    /**
     * Save location is the name of a register
     **/
    @Override
    public StringBuilder flatten(SymbolStack symbols, String saveLocation) {
	System.out.println("MathExpression.flatten() does nothing yet");
	return (new StringBuilder()).append("Some mathy stuff\n");
    }

    @Override
    public String getReturnType() {
	return type;
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