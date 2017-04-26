package littleCompiler.ast;

import symbolTable.*;

public class If implements IStmt {

    Condition cond;
    StmtList trueBody;
    StmtList falseBody;

    static int blockCount;

    public If(Condition cond, StmtList trueBody, StmtList falseBody) {
	this.cond = cond;
	this.trueBody = trueBody;
	this.falseBody = falseBody;
    }

    public If(Condition cond, StmtList trueBody) {
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

    public StringBuilder flatten(SymbolStack symbols) {
	blockCount++;
	StringBuilder addTo = new StringBuilder();
	String endLabel = "IFEND" + Integer.toString(blockCount);
	if(falseBody != null) {
	    String elseLabel = "ELSE" + Integer.toString(blockCount);
	    //get the condition, reverse the condition:
	    addTo.append(cond.flatten(symbols, elseLabel, true));
	    //fall through to the if part:
	    addTo.append(trueBody.flatten());
	    //skip the else part:
	    addTo.append("jmp " + endLabel + "\n");
	    //add else label:
	    addTo.append("label " + elseLabel + "\n");
	    //do the else part:
	    addTo.append(falseBody.flatten());
	    //add the end label:
	    addTo.append("label " + endLabel + "\n");
	} else {
	    //get the condition, reverse the condition:
	    addTo.append(cond.flatten(symbols, endLabel, true));
	    //fall through to the if part:
	    addTo.append(trueBody.flatten());
	    addTo.append("label " + endLabel + "\n");
	}
	return addTo;
    }
}