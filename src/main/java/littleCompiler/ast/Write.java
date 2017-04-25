package littleCompiler.ast;

import symbolTable.*;

import java.util.List;
import java.util.LinkedList;

public class Write implements IStmt {

    List<String> args;

    public Write(List<String> args) {
	this.args = args;
    }

    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on Write");
	return true;
    }

    public void print() {
	System.out.print("WRITE: ");
	for (String i : args) {
	    System.out.print(i + ", ");
	}
	System.out.println();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on Write");
    }

    public StringBuilder flatten(SymbolStack symbols) {
	StringBuilder addTo = new StringBuilder();
	for(String i : args) {
	    String command = getPrintCommand(symbols.getType(i)) + " " + i;
	    System.out.println("Generated command: " + command);
	    addTo.append(command);
	}
	return addTo;
    }

    private String getPrintCommand(String type) {
	switch(type) {
	case "INT":
	    return "sys writei";
	case "STRING":
	    return "sys writes";
	case "FLOAT":
	    return "sys writer";
	default:
	    throw new IllegalArgumentException("Variable type " + type + " is not defined for printing");
	}
    }
}
