package symbolTable;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {

    String name;
    Map<String,TableEntry> table;
    private static int registerCounter = 1;
    
    public SymbolTable(String name) {
	this.name = name;
	table = new Hashtable<String,TableEntry>();
    }
    
    public boolean isDefined(String name) {
	return table.containsKey(name);
    }

    public String getType(String name) {
	if(isDefined(name)) {
	    return table.get(name).dataType;
	} else return null;
    }

    public String getSymbolLocation(String varName) {
	if(isDefined(varName)) {

	    return table.get(varName).getLocation();
	} else {
	    System.err.println("Something Went horribly wrong...");
	    return null;
	}
    }

    public boolean insert(String name, String dataType) {
	if(!isDefined(name)) {
	    table.put(name, new TableEntry(name, dataType));
	    return true;
	} else return false;
    }

    public boolean insert(String name, String dataType, String value) {
	if(!isDefined(name)) {
	    table.put(name, new ConstTableEntry(name, dataType, value));
	    return true;
	} else return false;
    }

    public String getName() {
	return name;
    }

    public void prettyPrint(){
	System.out.println("Symbol table " + name);
	for(TableEntry entry : table.values()){
	    entry.prettyPrint();
	}
    }

    public static class TableEntry {
	public String name;
	public String dataType;

	//what it is refered to in the asm code:
	public String location;
	
	public TableEntry(String name, String dataType) {
	    this.name = name;
	    this.location = name + registerCounter;
	    this.dataType = dataType;
	}

	public void prettyPrint(){
	    System.out.printf("name %s type %s\n", name, dataType);
	}

	public String getLocation() {
	    return this.location;
	}
    }

    public static class ConstTableEntry extends TableEntry {
	public String value;

	public ConstTableEntry(String name, String dataType, String value) {
	    super(name, dataType);
	    this.value = value;
	}

	@Override
	public void prettyPrint(){
	    System.out.printf("name %s type %s value \"%s\"\n", name, dataType, value);
	}
    }

}
