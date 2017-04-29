package littleCompiler.ast;

import symbolTable.*;
import java.util.List;

public class Program {

    List<StringDecl> decl;
    //something else if there where more than one function:
    StmtList stmts;

    public Program(StmtList stmts, List<StringDecl> decls) {
	this.decl = decls;
	this.stmts = stmts;
    }

    public StringBuilder flatten(SymbolStack symbols) {
	StringBuilder bldr = new StringBuilder();
	for(StringDecl d : decl) {
	    bldr.append(d.flatten(symbols));
	}
	bldr.append(stmts.flatten());
	bldr.append("sys halt");
	return bldr;
    }
}
