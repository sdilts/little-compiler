package littleCompiler.ast;

import symbolTable.*;

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

    //yes, this is duplicate, but I am running out of time:
    private String getType(String name) {
	if(name == null) {
	    return null;
	}else if(Character.isLetter(name.charAt(0))) {
	    return "id";
	}else if(name.contains("\"")){
	    return "STRING";
	}else if(name.contains(".")){
	    return "FLOAT";
	}
	return "INT";
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

    private String getOpPostFix() {
	switch(type) {
	case "INT":
	    return "i";
	case "FLOAT":
	    return "r";
	default:
	    System.err.println("type " + terminal + " snuck into a mathexpression");
	    return null;
	}
    }

    private String getOpString() {
	switch(terminal) {
	case "+":
	    return "add" + getOpPostFix();
	case "-":
	    return "sub" + getOpPostFix();
	case "*":
	    return "mul" + getOpPostFix();
	case "/":
	    return "div" + getOpPostFix();
	default:
	    System.err.println(terminal + " snuck into a mathexpression");
	    return null;
	}
    }

    private String getTerminalName(SymbolStack stack, String name) throws OutOfScopeException {
	String type = getType(name);
	if(type.equals("id")) {
	    return stack.getLocation(name);
	} else {
	    return name;
	}
    }

    private static int tempCount = 0;

    private void dispatcher(StringBuilder bldr, SymbolStack symbols, String saveLocation) throws OutOfScopeException {
	if(this.isOperator) {
	    System.out.println(this);
	    //we can re-use our temp val for one of the values
	    if(left.isOperator && !right.isOperator) {
		System.out.println("only right terminal");
		//compute one side:
		left.dispatcher(bldr,symbols,saveLocation);
		//add them both together:
		bldr.append(getOpString() + " " + saveLocation + " " + getTerminalName(symbols,right.terminal) + "\n");
	    } else if(right.isOperator && !left.isOperator) {
		System.out.println("only left terminal");
		//compute one side:
		right.dispatcher(bldr,symbols,saveLocation);
		//add them both together:
		bldr.append(getOpString() + " " + getTerminalName(symbols, left.terminal) + " " + saveLocation + "\n");
	    } else if(right.isOperator && left.isOperator) {
		System.out.println("none terminal");
		//both are operators:
		//use our temp for one:
		left.dispatcher(bldr,symbols,saveLocation);
		//summon another temp for the other:
		tempCount++;
		String tempLoc = "TMath" + Integer.toString(tempCount);

		right.dispatcher(bldr,symbols,tempLoc);
		
		bldr.append(getOpString() + " " + saveLocation + " " + tempLoc + "\n");
		tempCount--;
	    } else {
		System.out.println("both terminal");
		//neither are operators:
		bldr.append(getOpString() + " " + getTerminalName(symbols, left.terminal) + " " + getTerminalName(symbols,right.terminal) + "\n");
	    }
	} else {
	    System.err.println("A terminal snuck into the dispatcher");
	    System.exit(1);
	    //hopefully this breaks something:
	}
    }
    
    /**
     * Save location is the name of a register
     **/
    @Override
    public StringBuilder flatten(SymbolStack symbols, String saveLocation) {
	try {
	    StringBuilder add = new StringBuilder();
	    //simple assignment:
	    if(!isOperator) {
		String type = getType(terminal);
		if(type.equals("id")) {
		    add.append("move " + symbols.getLocation(terminal) + " " + saveLocation + "\n");
		} else {
		    add.append("move " + terminal + " " + saveLocation + "\n");
		}
	    } else {
		//actually have to do math:
		this.dispatcher(add,symbols,saveLocation);
	    }
	    return add;
	} catch(OutOfScopeException e) {
	    System.out.println(e.getMessage());
	    System.exit(1);
	}
	return null;
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