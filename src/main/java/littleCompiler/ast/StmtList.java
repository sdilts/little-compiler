package littleCompiler.ast;

import symbolTable.SymbolStack;

import java.util.LinkedList;
import java.util.Deque;

public class StmtList implements ITree {
    private Deque<IStmt> stmts;
    private SymbolStack symbols;

    public StmtList(SymbolStack tbl) {
	stmts = new LinkedList<IStmt>();
	symbols = tbl;
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

    public StringBuilder flatten() {
	StringBuilder addTo = new StringBuilder();
	for(IStmt t : stmts) {
	    addTo.append(t.flatten(symbols));
	}
	return addTo;
    }
}