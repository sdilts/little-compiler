package symbolTable;

import java.util.LinkedList;
import java.util.Deque;

public class SymbolStack {

    private Deque<SymbolTable> stack;
    private Deque<SymbolTable> queue;
    
    public SymbolStack() {
	stack = new LinkedList<SymbolTable>();
	queue = new LinkedList<SymbolTable>();
    }

    public void enterScope(String name) {
	//System.out.printf("Symbol table %s\n", name);
	SymbolTable t = new SymbolTable(name); 
	stack.push(t);
	queue.push(t);
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

    public void addSymbol(String dataType, String symbolName, String value) throws DeclarationError {
	if(!stack.peek().insert(symbolName, dataType, value)) {
	    throw new DeclarationError(symbolName);
	}
    }

    public void addSymbol(String dataType, String symbolName) throws DeclarationError {
	if(!stack.peek().insert(symbolName, dataType)) {
	    throw new DeclarationError(symbolName);
	}
    }

    public void prettyPrint(){
	for(SymbolTable symbolTable : queue){
	    symbolTable.prettyPrint();
	}
    }
}
