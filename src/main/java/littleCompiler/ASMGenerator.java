package littleCompiler;

import java.util.regex.*;

public class ASMGenerator {
    public static String toASM(String ir) {
	ir = convertTempsToRegisters(ir);
	StringBuilder irResult = new StringBuilder();
	
	for(String line: ir.split("\n")){
	    if(isMathInstruction(line)){
		line = convertMathInstruction(line);
	    }
	    irResult.append(line+"\n");
	}
	return irResult.toString();
    }

    private static boolean isMathInstruction(String line){
	line = line.split(" ")[0];
	return (line.contains("add") ||
		line.contains("sub") ||
		line.contains("mul") ||
		line.contains("div"));
    }

    public static String commentString(String code) {
	return code.replaceAll("(?m)^", ";");
    }

    private static String convertMathInstruction(String line) {
	String[] vals = line.split(" ");
	String result = "";
	result = result + "move " + vals[1] + " " + vals[3] + "\n";
	result = result + vals[0] + " " + vals[2] + " " + vals[3] + "\n";
	return result;
    }

    private static String convertTempsToRegisters(String ir){
	String irResult = new String(ir);
	int tempRegisterCounter = 99;
	
	for(String line : ir.split("\n")){
	    Pattern tempPattern = Pattern.compile("T((Math)|(Comp))[0-9]{1,3}");
	    Matcher tempMatcher = tempPattern.matcher(line);

	    while(tempMatcher.find()){
		String tempName = tempMatcher.group();
		irResult = irResult.replaceAll(tempName, "r"+tempRegisterCounter);
		line = line.replaceAll(tempName, "r"+tempRegisterCounter);
		tempMatcher = tempPattern.matcher(line);
		tempRegisterCounter--;
	    }
	}
	return irResult;
    }
}
