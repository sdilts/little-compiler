package littleCompiler;

import symbolTable.*;
import ast.*;

import java.util.LinkedList;
import java.util.Deque;

import org.antlr.v4.runtime.*;
import antlr.main.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;


public class ParserListener extends LittleBaseListener {

    public Deque<MathExpression> exprStack = new LinkedList<MathExpression>();
    public MathExpression curTree;
    
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

    private void printStack() {
	System.out.println("the contents of the stack are:");
	for(MathExpression e : exprStack) {
	    System.err.println(e);
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
    public void enterPrimary(LittleParser.PrimaryContext ctx) {
	if(!ctx.getChild(0).getText().equals("(") ) {
	    MathExpression temp = new MathExpression(ctx.getChild(0).getText());
	    System.err.println("Primary: " + ctx.getChild(0).getText());
	    System.err.println("primary");
	    curTree.addChild(temp);
	}
    }

    @Override
    public void enterFactor_prefix(LittleParser.Factor_prefixContext ctx) {
	if(ctx.getChild(2) != null) {
	    System.err.println("Mulop: " + ctx.getChild(2).getText());
	    MathExpression temp = new MathExpression(ctx.getChild(2).getText());
	    if(curTree != null) {
		System.err.println("we pushed");
		exprStack.push(curTree);
		printStack();
	    }
	    curTree = temp;
	}
    }


    @Override
    public void exitFactor_prefix(LittleParser.Factor_prefixContext ctx) {
	// if(ctx.getChild(2) != null) {
	//     if(!exprStack.isEmpty()) {
	// 	MathExpression temp = exprStack.pop();
	// 	System.err.println("exit");
	// 	temp.addChild(curTree);
	// 	curTree = temp;
	// 	printStack();
	//     }
	// }
	if(ctx.getChild(2) != null) {
	    if(!exprStack.isEmpty() && curTree.isFull()) {		
		printStack();
		MathExpression temp = exprStack.pop();
		temp.addChild(curTree);
		curTree = temp;
	    }
	}
    }

    @Override
    public void enterExpr_prefix(LittleParser.Expr_prefixContext ctx) {
	if(ctx.getChild(2) != null) {
	    System.err.println("Mulop: " + ctx.getChild(2).getText());
	    MathExpression temp = new MathExpression(ctx.getChild(2).getText());
	    if(curTree != null) {
		System.err.println("we pushed");
		exprStack.push(curTree);
		printStack();
	    }
	    curTree = temp;
	}
    }

    @Override
    public void exitExpr_prefix(LittleParser.Expr_prefixContext ctx) {
	if(ctx.getChild(2) != null) {
	    if(!exprStack.isEmpty() && curTree.isFull()) {		
		printStack();
		MathExpression temp = exprStack.pop();
		temp.addChild(curTree);
		curTree = temp;
	    }
	}
    }

    
    // @Override
    // public void enterAddop(LittleParser.AddopContext ctx) {
    // 	System.err.println("Addop: " + ctx.getText());
    // 	MathExpression temp = new MathExpression(ctx.getText());
    // 	temp.left = curTree;
    // 	curTree = temp;
    // }

    // @Override
    // public void enterExpr(LittleParser.ExprContext ctx) {
    // 	if(curTree != null) {
    // 	    exprStack.push(curTree);
    // 	    curTree = null;
    // 	}
    // }

    public void exitExpr(LittleParser.ExprContext ctx) {
	System.err.println("exiting expr");
	System.err.println("I'm happy");
	if(!exprStack.isEmpty() && curTree.isFull()) {		
	    printStack();
	    MathExpression temp = exprStack.pop();
	    temp.addChild(curTree);
	    curTree = temp;
	}
	
	System.out.println(curTree.isFull());
	System.err.println(curTree);
	
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
