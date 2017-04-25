package symbolTable;

import java.util.LinkedList;
import java.util.Deque;
import java.util.Formatter;

public class SymbolStack {

    private Deque<SymbolTable> stack;
    private Formatter printer;
    
    public SymbolStack() {
	stack = new LinkedList<SymbolTable>();
	printer = new Formatter(new StringBuilder());
    }

    public void enterScope(String name) {
	if(!stack.isEmpty()) {
	    printer.format("\n");
	}
	printer.format("Symbol table %s\n", name);
	stack.push(new SymbolTable(name));
    }

    public boolean exitScope() {
	return stack.pop() != null;
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
	if(stack.peek().insert(symbolName, dataType, value)) {
	    printer.format("name %s type %s value %s\n", symbolName, dataType, value);
	} else {
	    throw new DeclarationError(symbolName);
	}
    }

    public void addSymbol(String dataType, String symbolName) throws DeclarationError {
	if(stack.peek().insert(symbolName, dataType)) {
	    printer.format("name %s type %s\n", symbolName, dataType);
	} else {
	    throw new DeclarationError(symbolName);
	}
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
