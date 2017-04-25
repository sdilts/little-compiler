package littleCompiler.ast;

import symbolTable.*;

public class If implements ITree {

    SymbolStack symbols;
    Condition cond;
    StmtList trueBody;
    StmtList falseBody;

    public If(SymbolStack stack, Condition cond, StmtList trueBody, StmtList falseBody) {
	this.symbols = stack;
	this.cond = cond;
	this.trueBody = trueBody;
	this.falseBody = falseBody;
    }

    public If(SymbolStack stack, Condition cond, StmtList trueBody) {
	this.symbols = stack;
	this.cond = cond;
	this.trueBody = trueBody;
	this.falseBody = null;
    }
    
    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on While");
	return true;
    }

    public void print() {
	System.out.print("if: ");
	cond.print();
	System.out.println("true then");
	trueBody.print();
	System.out.println("End true");
	if(falseBody != null) {
	    System.out.println("false then:");
	    falseBody.print();
	    System.out.println("End false\n");
	}
	System.out.println();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on While");
    }

}


    
