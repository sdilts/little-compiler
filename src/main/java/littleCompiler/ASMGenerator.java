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
	if(vals[2].trim().equals(vals[3].trim())) {
	    if(vals[0].contains("div")) {
		System.err.println("This runs div");
		result = result + "move " + vals[1] + " r0\n";
		result = result + vals[0] + " " + vals[2] + " r0\n";
		result = result + "move r0 " + vals[3];
		// result = result + vals[0] + " " + vals[2] + " " + vals[1] + "\n";
		// result = result + "move " + vals[1] + " " + vals[3];
	    } else if(vals[0].contains("sub")) {
		System.err.println("This runs sub");
		result = result + "move " + vals[1] + " r0\n";
		result = result + vals[0] + " " + vals[2] + " r0\n";
		result = result + "move r0 " + vals[3];
	    } else {
		result = result + "move " + vals[1] + " " + vals[3] + "\n";
		result = result + vals[0] + " " + vals[2] + " " + vals[3];
	    }
	}else{
	    result = result + "move " + vals[1] + " " + vals[3] + "\n";
	    result = result + vals[0] + " " + vals[2] + " " + vals[3];
	}
	return result;
    }

    private static String convertTempsToRegisters(String ir){
	int tempRegisterCounter = 99;
	String[] toFind = { "TMath", "TComp"};

	for(String search : toFind) {
	    int counter = 1;
	    //search for search until we can't find it any more:
	    while(ir.contains(search + counter)) {
		ir = ir.replaceAll(search + counter+"(?=\\s)", "r" + tempRegisterCounter);
		counter++;
		tempRegisterCounter--;
	    }
	}
	
	// for(String line : irResult.split("\n")){
	//     Pattern tempPattern = Pattern.compile("T((Math)|(Comp))[0-9]*");
	//     Matcher tempMatcher = tempPattern.matcher(line);
	//     System.err.println("LINE");
	//     while(tempMatcher.find()){
	// 	String tempName = tempMatcher.group();
	// 	System.err.println("tempName: " + tempName + "\t"+tempRegisterCounter);
	// 	irResult = irResult.replaceAll(tempName, "r"+tempRegisterCounter);
	// 	line = line.replaceAll(tempName, "r"+tempRegisterCounter);
	// 	tempMatcher = tempPattern.matcher(line);
	// 	tempRegisterCounter--;
	//     }
	// }

	return ir;
    }
}
