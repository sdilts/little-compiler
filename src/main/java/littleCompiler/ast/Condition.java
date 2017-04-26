package littleCompiler.ast;

import symbolTable.SymbolStack;

public class Condition implements IReturnable {

    MathExpression first;
    MathExpression second;
    String cond;
    
    public Condition(ITree first, ITree second, String condType) {
	this.first = (MathExpression) first;
	this.second = (MathExpression) second;
	cond = condType;
    }

    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on cond");
	return true;
    }

    public void print() {
	System.out.print("\tcond: ");
	first.print();
	System.out.print("\t" + cond + " ");
	second.print();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on cond");
    }

    //the language doesn't define this, but we have to do something:
    @Override
    public String getReturnType() {
	System.err.println("Not supposed to be calling getReturnType on Cond?");
	return "BOOL";
    }

    @Override
    public StringBuilder flatten(SymbolStack symbols, String label) {
	return flatten(symbols,label,false);
    }

    public StringBuilder flatten(SymbolStack symbols, String label, boolean neg) {
	StringBuilder toAdd = new StringBuilder();
	//check the types: tiny has different instructions for each:
	String t1 = first.getReturnType();
	String t2 = first.getReturnType();
	if(t1.equals(t2)) {
	    toAdd.append(first.flatten(symbols, "TComp1"));
	    toAdd.append(second.flatten(symbols, "TComp2"));
	    toAdd.append(getCompInstruction(t1) + " TComp1 TComp2\n");
	    toAdd.append(getJmpInstruction(cond, neg) + " " + label + "\n");
	} else {
	    System.out.println("Cannot compare two different variable types.");
	    System.err.println("Bad Mismatched type: " + t1 + " and " + t2);
	    System.exit(1);
	}
	return toAdd;	
    }
    
    private String getJmpInstruction(String type, boolean neg) {
	if(neg) {
	    //reverse condition
	    switch(type) {
	    case "<=":
		return "jgt";
	    case ">=":
		return "jlt";
	    case "<":
		return "jge";
	    case ">":
		return "jle";
	    case "=":
		return "jne";
	    case "!=":
		return "jeq";
	    default:
		throw new IllegalArgumentException("Comparison " + type + " is not defined.");
	    }
	} else {
	    switch(type) {
	    case "<=":
		//less than or equal
		return "jle";
	    case ">=":
		//greater than or equal
		return "jge";
	    case "<":
		return "jlt";
	    case ">":
		return "jgt";
	    case "=":
		return "jeq";
	    case "!=":
		return "jne";
	    default:
		throw new IllegalArgumentException("Comparison " + type + " is not defined.");
	    }
	}
    }

    private String getCompInstruction(String type) {
	switch(type) {
	case "INT":
	    return "cmpi";
	case "FLOAT":
	    return "cmpr";
	default:
	    throw new IllegalArgumentException("Variable type " + type + " is not defined for comparison");
	}
	    
    }
    
}