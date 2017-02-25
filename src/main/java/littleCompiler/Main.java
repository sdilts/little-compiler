package littleCompiler;
import org.antlr.v4.runtime.*;
import antlr.main.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by stuart on 1/25/17.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                File f = new File(args[i]);
                if (f.exists() && f.isFile()) {
                    //System.out.printf("Compiling File %s\n", f.getName());
                    compileFile(f);
                } else {
                    System.out.printf("Filename %s is not a valid little file. Quiting.", args[i]);
                    break;
                }
            }
        } else {
            System.out.println("You must give one or more input files. Exiting");
        }
    }

    private static void compileFile(File file) {
        try {
            FileInputStream fStream = new FileInputStream(file);
            ANTLRInputStream stream = new ANTLRInputStream(fStream);
            //ANTLRInputStream stream = new ANTLRInputStream(System.in);

            LittleLexer lexer = new LittleLexer(stream);

	    
	    
	    LittleParser parser = new LittleParser(new CommonTokenStream(lexer));

	    parser.removeErrorListeners();
	    parser.addErrorListener(ThrowingErrorListener.INSTANCE);
	    parser.program();
	    System.out.println("Accepted");

        } catch(ParseCancellationException e){
	    System.out.println("Not accepted");
	    //System.out.println(e.getMessage());
	} catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
