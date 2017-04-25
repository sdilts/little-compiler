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

    public StringBuilder flatten(SymbolStack symbols) {
	StringBuilder addTo = new StringBuilder();
	for(String i : args) {
	    String command = getReadCommand(symbols.getType(i)) + " " + i + "\n";
	    System.out.println("Generated command: " + command);
	    addTo.append(command);
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
