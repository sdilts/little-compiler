package littleCompiler.ast;

import symbolTable.SymbolStack;

public interface IReturnable extends ITree {

    public StringBuilder flatten(SymbolStack symbols, String saveLocation);

    public String getReturnType();
}
