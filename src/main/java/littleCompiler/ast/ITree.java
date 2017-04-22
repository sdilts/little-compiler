package littleCompiler.ast;

public interface ITree {
    public void addChild(Object child);
    public boolean isFull();
    public void print();
}