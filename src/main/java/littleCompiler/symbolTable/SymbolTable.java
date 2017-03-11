package symbolTable;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {

    String name;
    Map<String,TableEntry> table;
    
    public SymbolTable(String name) {
	this.name = name;
	table = new Hashtable<String,TableEntry>();
    }
    
    public boolean isDefined(String name) {
	return table.containsKey(name);
    }

    public boolean insert(String name, String dataType) {
	if(!isDefined(name)) {
	    table.put(name, new TableEntry(name, dataType));
	    return true;
	} else return false;
    }

    public String getName() {
	return name;
    }

    public static class TableEntry {
	public String name;
	public String dataType;
	
	public TableEntry(String name, String dataType) {
	    this.name = name;
	    this.dataType = dataType;
	}
    }

}
