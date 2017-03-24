package littleCompiler;

import symbolTable.*;

import org.antlr.v4.runtime.*;
import antlr.main.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ParserListener extends LittleBaseListener {

    public SymbolStack stack = new SymbolStack();
    private Parser parser;
    
    public int blockCounter = 0;


    public ParserListener(Parser parser) {
	this.parser = parser;
    }

    
    @Override public void enterVar_type(LittleParser.Var_typeContext ctx) {
	
    }
    
    @Override
    public void enterFunc_decl(LittleParser.Func_declContext ctx) {
	//System.out.println(ctx.getChild(2).getChild(0).getText());
	
	// System.out.println(ctx.getChildCount());
	// System.out.println("This is the context as a string: " + ctx.toString());
	// System.out.println("This is the 0th child: " + ctx.getChild(0));
	stack.enterScope(ctx.getChild(2).getChild(0).getText());
    }

    
    @Override
    public void exitFunc_decl(LittleParser.Func_declContext ctx) {
	//System.out.println(ctx.children);	
	stack.exitScope();
    }


    
    @Override
    public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
	blockCounter++;
	stack.enterScope("BLOCK" + blockCounter);
    }

    @Override
    public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
	stack.exitScope();

    }

    @Override
    public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
	blockCounter++;
	//stack.enterScope("while" + whileCounter);
	stack.enterScope("BLOCK " + blockCounter);
    }

    @Override
    public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
	stack.exitScope();
    }


    @Override
    public void enterElse_part(LittleParser.Else_partContext ctx) {
	blockCounter++;
	stack.enterScope("BLOCK " + blockCounter);
    }
    
    @Override
    public void exitElse_part(LittleParser.Else_partContext ctx) {
	stack.exitScope();
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
