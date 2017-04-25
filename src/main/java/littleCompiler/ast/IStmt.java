package littleCompiler.ast;

import symbolTable.*;

public interface IStmt extends ITree {
    public StringBuilder flatten(SymbolStack symbols);

}
