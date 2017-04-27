package littleCompiler.ast;

import symbolTable.*;


public class While implements IStmt {

    StmtList body;
    Condition cond;

    private static int blockCount = 0;
    
    public While(Condition con, StmtList body) {
	this.cond = con;
	this.body = body;
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

    public StringBuilder flatten(SymbolStack symbols) {
	blockCount++;
	//System.out.println("While.flatten() does nothing yet");
	StringBuilder addTo = new StringBuilder();
	String jmpLabel = "WHILE" + Integer.toString(blockCount);
	addTo.append("label " + jmpLabel + "\n");
	//reverse the condition: "skip" if statement is false:
	addTo.append(cond.flatten(symbols, "WEND" + Integer.toString(blockCount), true));
	addTo.append(body.flatten());
	addTo.append("jmp " + jmpLabel + "\n");
	addTo.append("label WEND" + Integer.toString(blockCount) + "\n");
	return addTo;
    }
}
