package Shell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
//----------------------------------------DataBase----------------------------------------
//Class DataBase uses map to store tables
//Filed validTypes: stores all the valid data type the database support
//tableMap: stores all the tables
public class DataBase {
    public static Set<String> validTypes = new HashSet<>();
    private String name;

    private HashMap<String, Table> tableMap;
    private Table currentTable;
    static {
        validTypes.add("INT");
        validTypes.add("VARCHAR");
        validTypes.add("DOUBLE");
        validTypes.add("CHAR");
        validTypes.add("FLOAT");
    }
    public DataBase(String name){
        this.name = name;
        tableMap = new HashMap<>();
    }
    //create table and put it into map
    public void createTable(String s){
        String tableName = s.substring(0,s.indexOf(" "));
        currentTable = new Table(s);
        tableMap.put(tableName, currentTable);
    }
    //
    public Table getCurrentTable() {
        return currentTable;
    }
    public Table getTable(String tableName) throws Exception{
        if(!tableMap.containsKey(tableName)){
            throw new Exception("table " + tableName + " does not exist");
        }
        currentTable = tableMap.get(tableName);
        return tableMap.get(tableName);
    }
}
