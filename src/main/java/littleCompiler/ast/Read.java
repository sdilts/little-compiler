package littleCompiler.ast;

import java.util.List;
import java.util.LinkedList;

public class Read implements ITree {

    List<String> args;

    public Read(List<String> args) {
	this.args = args;
    }

    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on Read");
	return true;
    }

    public void print() {
	System.out.print("READ: ");
	for (String i : args) {
	    System.out.print(i + ", ");
	}
	System.out.println();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on Read");
    }
}
