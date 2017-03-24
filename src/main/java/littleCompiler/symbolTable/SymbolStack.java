package symbolTable;

import java.util.LinkedList;
import java.util.Deque;

public class SymbolStack {

    private Deque<SymbolTable> stack;
    
    public SymbolStack() {
	stack = new LinkedList<SymbolTable>();
    }

    public void enterScope(String name) {
	System.out.printf("Symbol table %s\n", name);
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

    public void addSymbol(String symbolName, String dataType) throws DeclarationError {
	if(stack.peek().insert(symbolName, dataType)) {
	    System.out.printf("name %s type %s\n", symbolName, dataType);
	} else {
	    throw new DeclarationError(symbolName);
	}
    }
}
