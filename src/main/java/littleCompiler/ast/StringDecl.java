package littleCompiler.ast;

import symbolTable.*;
import java.util.List;

public class StringDecl implements IStmt {

    private String varName;

    public StringDecl(String varName) {
	this.varName = varName;
    }
    
    public void addChild(Object child) {
	System.out.println("Something is wrong");
    }
    public boolean isFull() {
	System.out.println("Something is wrong");
	return true;
    }
    
    public void print() {
	System.out.println("STRING " + varName);
    }

    public StringBuilder flatten(SymbolStack symbols) {
	return new StringBuilder("str " + varName + " " + symbols.getValue(varName) + "\n");
    }

}
