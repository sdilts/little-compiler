package littleCompiler;

import symbolTable.*;

import org.antlr.v4.runtime.*;
import antlr.main.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;


public class ParserListener extends LittleBaseListener {

    public SymbolStack stack = new SymbolStack();
    private Parser parser;
    
    public int blockCounter = 0;


    public ParserListener(Parser parser) {
	this.parser = parser;
    }
    
    @Override
    public void enterVar_decl(LittleParser.Var_declContext ctx) {
	String type = ctx.getChild(0).getText();
	//System.out.println("TYPE: +t"ype);
	String id = ctx.getChild(1).getChild(0).getText();
	//System.out.println("ID: "+id);
	interSymbol(type, id);

	ParseTree curTail = ctx.getChild(1).getChild(1);

	while(curTail.getChild(2) != null){
	    id = curTail.getChild(1).getText();
	    //System.out.println("ID: "+id);
	    interSymbol(type, id);
	    curTail = curTail.getChild(2);
	}
    }
    
    @Override
    public void enterString_decl(LittleParser.String_declContext ctx) {
	try {
	    stack.addSymbol("STRING", ctx.getChild(1).getText(), ctx.getChild(3).getChild(0).getText());
	}
	catch(DeclarationError de) {
	    System.out.println("DECLARATION ERROR " + de.getMessage());
	    System.exit(1);
	}
	
    }
    
    @Override
    public void enterFunc_decl(LittleParser.Func_declContext ctx) {
	//enter the scope:
	stack.enterScope(ctx.getChild(2).getChild(0).getText());
	//examine the arguments:
	ParseTree argList = ctx.getChild(4);
	ParseTree firstArg = argList.getChild(0);
	if(firstArg != null) {
	    //intern the first argument:
	    interSymbol(firstArg.getChild(0).getText(),firstArg.getChild(1).getText());
	    //grab the tail:
	    argList = argList.getChild(1);
	    firstArg = argList.getChild(1);
	    while(firstArg != null) {
		interSymbol(firstArg.getChild(0).getText(),firstArg.getChild(1).getText());
		argList = argList.getChild(2);
		firstArg = argList.getChild(1);
	    }
	}
	
    }

    //A rather morbid function name:
    private void interSymbol(String type, String name) {
	try {
	    stack.addSymbol(type, name);
	} catch(DeclarationError de) {
	    System.out.println("DECLARATION ERROR " + de.getMessage());
	    System.exit(1);
	}
    }
    
    @Override
    public void exitFunc_decl(LittleParser.Func_declContext ctx) {
	//System.out.println(ctx.children);	
	stack.exitScope();
    }


    
    @Override
    public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
	blockCounter++;
	stack.enterScope("BLOCK " + blockCounter);
    }

    @Override
    public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
	stack.exitScope();

    }

    @Override
    public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
	blockCounter++;
	stack.enterScope("BLOCK " + blockCounter);
    }

    @Override
    public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
	stack.exitScope();
    }


    @Override
    public void enterElse_part(LittleParser.Else_partContext ctx) {
	if(ctx.getChild(0) != null) {
	    blockCounter++;
	    stack.enterScope("BLOCK " + blockCounter);
	}
    }
    
    @Override
    public void exitElse_part(LittleParser.Else_partContext ctx) {
	if(ctx.getChild(0) != null) {
	    stack.exitScope();
	}
    }

    @Override
    public void enterProgram(LittleParser.ProgramContext ctx) {
	stack.enterScope("GLOBAL");
    }


    @Override
    public void exitProgram(LittleParser.ProgramContext ctx) {
    	stack.exitScope();
    }
}