package littleCompiler.ast;

import symbolTable.SymbolStack;

import java.util.LinkedList;
import java.util.Deque;

public class StmtList implements IStmt {
    private Deque<IStmt> stmts;

    public StmtList() {
	stmts = new <IStmt>LinkedList();
    }

    public boolean isFull() {
	return false;
    }

    public void addChild(Object child) {
	stmts.addFirst( (IStmt) child);
    }

    public void print() {
	System.out.println("This is a statement list:");
	for(IStmt t : stmts) {
	    t.print();
	}
	System.out.println("Done printing the list");
    }

    public StringBuilder flatten(SymbolStack symbols) {
	StringBuilder addTo = new StringBuilder();
	for(IStmt t : stmts) {
	    addTo.append(t.flatten(symbols));
	}
	return addTo;
    }
}
