package ast;

public interface ITree {
    public void addChild(ITree child);
    public boolean isFull();
    public String flatten(String action);
}
