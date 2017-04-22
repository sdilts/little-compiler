package littleCompiler.ast;

import java.util.LinkedList;
import java.util.Deque;

public class StmtList implements ITree {
    private Deque stmts;

    public StmtList() {
	stmts = new LinkedList();
    }

    public boolean isFull() {
	return false;
    }

    public void addChild(Object child) {
	stmts.addLast(child);
    }

    public void print() {
	System.out.println("This is a statement list:");
	for(ITree t : stmts) {
	    t.print();
	}
	System.out.println("Done printing the list");
    }
	
}