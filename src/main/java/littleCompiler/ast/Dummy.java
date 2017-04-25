package littleCompiler.ast;

public class Dummy implements ITree {

    public boolean isFull() {
	return true;
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be adding to dummy, dummy");
    }

    public void print() {
	System.out.println("DUMMY");
    }

}
