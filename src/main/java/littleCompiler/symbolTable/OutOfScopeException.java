package symbolTable;

public class OutOfScopeException extends Exception {
    public OutOfScopeException(String varName) {	
	super(varName + " is not defined");
    }
}