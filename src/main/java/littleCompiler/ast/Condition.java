package littleCompiler.ast;

public class Condition implements ITree {

    MathExpression first;
    MathExpression second;
    String cond;
    
    public Condition(ITree first, ITree second, String condType) {
	this.first = (MathExpression) first;
	this.second = (MathExpression) second;
	cond = condType;
    }

    public boolean isFull() {
	System.err.println("Not supposed to be calling isFull on cond");
	return true;
    }

    public void print() {
	System.out.print("\tcond: ");
	first.print();
	System.out.print("\t" + cond + " ");
	second.print();
    }

    public void addChild(Object child) {
	System.err.println("Not supposed to be calling addChild on cond");
    }

}
