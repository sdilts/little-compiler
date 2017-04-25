package littleCompiler.ast;

import symbolTable.*;


public class While implements ITree {

    SymbolStack symbols;
    StmtList body;
    Condition cond;
    
    public While(SymbolStack symbols, Condition con, StmtList body) {
	this.cond = con;
	this.body = body;
	this.symbols = symbols;
    }

    
    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on While");
	return true;
    }

    public void print() {
	System.out.print("While: ");
	cond.print();
	body.print();
	System.out.println();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on While");
    }

}
