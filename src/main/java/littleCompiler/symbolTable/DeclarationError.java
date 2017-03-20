package symbolTable;

public class DeclarationError extends Exception {
    public DeclarationError(String varName) {	
	super(varName);
    }
}
