package symbolTable;

import java.util.LinkedList;
import java.util.Deque;
import java.util.Formatter;

public class SymbolStack {

    private Deque<SymbolTable> stack;
    private Formatter printer;

    private int registerCounter = 0;

    public static SymbolStack copy(SymbolStack stack) {
	Deque<SymbolTable> toAdd = new LinkedList<SymbolTable>();
	for(SymbolTable t : stack.stack) {
	    toAdd.push(t);
	}
	return new SymbolStack(toAdd, stack.registerCounter);
    }
    
    public SymbolStack() {
	stack = new LinkedList<SymbolTable>();
	printer = new Formatter(new StringBuilder());
    }

    private SymbolStack(Deque<SymbolTable> tbl, int  registerCounter) {
	stack = tbl;
	this.registerCounter = registerCounter;
    }

    public void enterScope(String name) {
	if(!stack.isEmpty()) {
	    printer.format("\n");
	}
	printer.format("Symbol table %s\n", name);
	stack.push(new SymbolTable(name));
    }

    public boolean exitScope() {
	SymbolTable rm = stack.pop();
	registerCounter = registerCounter - rm.table.size();
	//throws an exception if it doesn't work:
	return true;
    }

    public boolean isDefined(String symbolName) {
	for(SymbolTable s : stack) {
	    if(s.isDefined(symbolName)) {
		return true;
	    }
	}
	return false;
    }

    public String getType(String symbolName) {
	for(SymbolTable s : stack) {
	    String t = s.getType(symbolName);
	    if(t != null) {
		return t;
	    }
	}
	System.err.println("Could not determine the type of " + symbolName);
	return null;
    }

    public String getLocation(String varName) throws OutOfScopeException {
	for(SymbolTable s : stack) {
	    if(s.isDefined(varName)) {
		return s.getSymbolLocation(varName);
	    }
	}
	throw new OutOfScopeException(varName);
    }
    
    public void addSymbol(String dataType, String symbolName, String value) throws DeclarationError {
	registerCounter++;
	if(stack.peek().insert(symbolName, dataType, value, registerCounter)) {
	    printer.format("name %s type %s value %s, location: %d\n", symbolName, dataType, value, registerCounter);
	} else {
	    throw new DeclarationError(symbolName);
	}
    }

    public void addSymbol(String dataType, String symbolName) throws DeclarationError {
	this.registerCounter++;
	if(stack.peek().insert(symbolName, dataType, registerCounter)) {
	    printer.format("name %s type %s, location: %d\n", symbolName, dataType, registerCounter);
	} else {
	    throw new DeclarationError(symbolName);
	}
    }

    public String getValue(String varName) {
	for(SymbolTable s : stack) {
	    String val = s.getValue(varName);
	    if(val != null) {
		return val;
	    }
	}
	System.err.println("Could not determine the type of " + varName);
	return null;
    }

    public void prettyPrint() {
	System.out.print(printer.out());
    }
    
    public void fullPrint(){
	for(SymbolTable symbolTable : stack){
	    symbolTable.prettyPrint();
	}
    }
}
