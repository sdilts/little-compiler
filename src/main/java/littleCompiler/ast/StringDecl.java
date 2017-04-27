package littleCompiler.ast;

public class StringDecl implements IStmt {

    private String varName;

    public StringDecl(String varName) {
	this.value = toDeclare;
    }
    
    public void addChild(Object child) {
	System.out.println("Something is wrong");
    }
    public boolean isFull() {
	System.out.println("Something is wrong");
	return true;
    }
    
    public void print() {
	System.out.println("STRING " + value);
    }

    public String flatten(SymbolTable symbols) {
	return "str " + varName + " " + symbols.getValue(varName) + "\n";
    }

}
