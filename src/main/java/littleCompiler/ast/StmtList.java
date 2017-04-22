package littleCompiler.ast;

import java.util.LinkedList;
import java.util.Deque;

public class StmtList implements ITree {
    private Deque<ITree> stmts;

    public StmtList() {
	stmts = new <ITree>LinkedList();
    }

    public boolean isFull() {
	return false;
    }

    public void addChild(Object child) {
	stmts.addFirst( (ITree)child);
    }

    public void print() {
	System.out.println("This is a statement list:");
	for(ITree t : stmts) {
	    t.print();
	}
	System.out.println("Done printing the list");
    }
	
}