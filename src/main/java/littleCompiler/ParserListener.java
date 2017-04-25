package littleCompiler;

import symbolTable.*;
import littleCompiler.ast.*;

import java.util.LinkedList;
import java.util.Deque;
import java.util.List;

import org.antlr.v4.runtime.*;
import antlr.main.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;


public class ParserListener extends LittleBaseListener {

    public Deque<ITree> exprStack = new LinkedList<ITree>();
    public Deque<MathExpression> mathStack = new LinkedList<MathExpression>();

    public SymbolStack stack = new SymbolStack();
    
    private LittleParser parser;
    
    public int blockCounter = 0;

    public ParserListener(Parser parser) {
	this.parser = (LittleParser) parser;
    }
    
    @Override
    public void enterVar_decl(LittleParser.Var_declContext ctx) {
	String type = ctx.getChild(0).getText();
	String id = ctx.getChild(1).getChild(0).getText();
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
	for(ITree e : exprStack) {
	    System.err.println("Class = " + e.getClass().toString());
	    e.print();
	}
	System.out.println("Done printing stack.");
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
	    mathStack.push(temp);
	}
    }

    @Override
    public void enterFactor_prefix(LittleParser.Factor_prefixContext ctx) {
	if(ctx.getChild(2) != null) {
	    MathExpression temp = new MathExpression(ctx.getChild(2).getText());
	    mathStack.push(temp);

	}
    }

    @Override
    public void enterExpr_prefix(LittleParser.Expr_prefixContext ctx) {
	if(ctx.getChild(2) != null) {
	    MathExpression temp = new MathExpression(ctx.getChild(2).getText());
	    mathStack.push(temp);
	}
    }

    private void printMath() {
	for(MathExpression e : mathStack) {
	    System.out.println(e);
	}
    }

    @Override
    public void exitExpr(LittleParser.ExprContext ctx) {
	String parent = parser.ruleNames[ctx.getParent().getRuleIndex()];
	if(!parent.equals("primary")) {
	    Deque<MathExpression> varStack = new LinkedList<MathExpression>();
	    while(mathStack.size() > 1) {
		while(mathStack.peekFirst().isFull()) {
		    varStack.push(mathStack.pop());
		}
		MathExpression op = mathStack.pop();
		op.addChild(varStack.pop());
		op.addChild(varStack.pop());
		mathStack.push(op);
	    }
		
	    if(mathStack.size() > 1) {
		System.out.println("there was a problem parsing the math");
	    } else {
		exprStack.push(mathStack.pop());
	    }
	}
    }

    @Override
    public void exitAssign_expr(LittleParser.Assign_exprContext ctx) {
	
	Assign asn = new Assign();
	asn.addChild(ctx.getChild(0).getText());
	asn.addChild(exprStack.pop());
	exprStack.push(asn);
    }

    @Override
    public void enterStmt_list(LittleParser.Stmt_listContext ctx) {
    }
    
    @Override
    public void exitStmt_list(LittleParser.Stmt_listContext ctx) {
	if(ctx.getChild(0) != null) {
	    ITree lst = null;
	    if(!(exprStack.peek() instanceof StmtList)) {
		lst = new StmtList();
	    } else {
		lst = exprStack.pop();
	    }
	    ITree expr = exprStack.pop();
	    lst.addChild(expr);
	    exprStack.push(lst);
	}
    }

    @Override
    public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
	List<String> ids = new LinkedList<String>();
	String id = ctx.getChild(2).getChild(0).getText();
	ids.add(id);

	ParseTree curTail = ctx.getChild(2).getChild(1);

	while(curTail.getChild(2) != null){
	    id = curTail.getChild(1).getText();
	    //System.out.println("ID: "+id);
	    ids.add(id);
	    curTail = curTail.getChild(2);
	}
	exprStack.push(new Write(ids));
    }
    
    @Override
    public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
	List<String> ids = new LinkedList<String>();
	String id = ctx.getChild(2).getChild(0).getText();
	ids.add(id);

	ParseTree curTail = ctx.getChild(2).getChild(1);

	while(curTail.getChild(2) != null){
	    id = curTail.getChild(1).getText();
	    //System.out.println("ID: "+id);
	    ids.add(id);
	    curTail = curTail.getChild(2);
	}
	exprStack.push(new Read(ids));
    }
    
    @Override
    public void exitFunc_decl(LittleParser.Func_declContext ctx) {
	stack.exitScope();
    }


    @Override
    public void exitCond(LittleParser.CondContext ctx) {
	MathExpression second = (MathExpression) exprStack.pop();
	exprStack.push(new Condition(exprStack.pop(), second, ctx.getChild(1).getText()));
    }
    
    @Override
    public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
	blockCounter++;
	stack.enterScope("BLOCK " + blockCounter);
    }

    @Override
    public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
	if(ctx.getChild(6).getChild(0) != null) {
	    //get all of it:
	    StmtList elsePart = (StmtList) exprStack.pop();
	    StmtList ifPart = (StmtList) exprStack.pop();
	    exprStack.push(new If(stack, (Condition) exprStack.pop(), ifPart, elsePart));
	} else {
	    StmtList ifPart = (StmtList) exprStack.pop();
	    exprStack.push(new If(stack, (Condition) exprStack.pop(), ifPart));
	}
	stack.exitScope();

    }

    @Override
    public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
	blockCounter++;
	stack.enterScope("BLOCK " + blockCounter);
	
    }

    @Override
    public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
	StmtList bdy = (StmtList) exprStack.pop();
	exprStack.push(new While(stack, (Condition) exprStack.pop(), bdy));
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
	printStack();
    }
}