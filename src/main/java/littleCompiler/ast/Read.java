package littleCompiler.ast;

import symbolTable.*;

import java.util.List;
import java.util.LinkedList;

public class Read implements IStmt {

    List<String> args;

    public Read(List<String> args) {
	this.args = args;
    }

    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on Read");
	return true;
    }

    public void print() {
	System.out.print("READ: ");
	for (String i : args) {
	    System.out.print(i + ", ");
	}
	System.out.println();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on Read");
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


    private String getTerminalName(SymbolStack stack, String name) throws OutOfScopeException {
	String type = getType(name);
	if(type.equals("id")) {
	    return stack.getLocation(name);
	} else {
	    return name;
	}
    }

    public StringBuilder flatten(SymbolStack symbols) {
	StringBuilder addTo = new StringBuilder();
	try {
	    for(String i : args) {
		String command = getReadCommand(symbols.getType(i)) + " " + getTerminalName(symbols, i) + "\n";
		System.out.println("Generated command: " + command);
		addTo.append(command);
	    }
	} catch (OutOfScopeException e) {
	    System.err.println(e.getMessage());
	    System.exit(1);
	} 
	return addTo;
    }

    private String getReadCommand(String type) {
	switch(type) {
	case "INT":
	    return "sys readi";
	case "FLOAT":
	    return "sys readr";
	default:
	    throw new IllegalArgumentException("Variable type " + type + " is not defined for reading");
	}
    }
}
