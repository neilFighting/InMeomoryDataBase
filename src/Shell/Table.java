package Shell;

import java.util.*;
//----------------------------------------Table----------------------------------------
//Class Table is designed to store all the rows in the table uses LinkedHashMap
//to map ****Primary Key **** to ****Corresponding Row****
public class Table {
    private String name;
    private int id;
    private LinkedHashMap<String, Row> rowMap;
    private LinkedHashMap<String, String> typeMap;
    private String primaryKey;
    private List<String> sortedList;
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    Table(String s){
        String[] args = s.split(" +");
        rowMap = new LinkedHashMap<>();
        typeMap = new LinkedHashMap<>();
        this.name = args[0];
        //if the number of args is not correct, view it as exception
        for (int i = 1; i < args.length - 1; i += 2) {
            String colName = args[i], colType = args[i + 1];
            typeMap.put(colName, colType);
        }
    }
    //create new row and add it to the
    public void insertRow(String s) throws Exception{
        //if the table does not have
        if(primaryKey == null){
            Row row = new Row(s, typeMap);
            rowMap.put(id + "", row);
            id++;
        }else{
            String key = null;
            String[] parts = s.split(" +");
            for(int  i = 0; i < parts.length; i++){
                if(parts[i].equals(primaryKey)){
                    key = parts[i + 1];
                }
            }
            if(rowMap.containsKey(key)){
                throw new Exception("primary key " + primaryKey + " has to be unqiue");
            }
            Row row = new Row(s,typeMap);
            rowMap.put(key,row);
        }


    }
    //remove the row from the HashMap(if it is in the table)
    public void deleteRow (String rowKey) throws Exception {
        if(primaryKey == null)
            throw new Exception("Shell.Table " + name + "does not have primary key");

        if(!rowMap.containsKey(rowKey))
            throw new Exception("the given row does not exist");

        rowMap.remove(rowKey);
    }
    //find target row via map and Primary Key, change some of the cells according to the column name
    public void updateRow(String rowKey, String s) throws Exception{
        if(primaryKey == null)
            throw new Exception("Shell.Table " + name + "does not have primary key");

        if(!rowMap.containsKey(rowKey))
            throw new Exception("the given row does not exist");

        rowMap.get(rowKey).setValue(s,this.typeMap);
    }
    //print all the rows in the order of being inserted
    public void getAllRows() {
        printTable(rowMap.size(),rowMap.keySet());
    }
    //print limit number of rows in the order of being inserted
    public void getLimitRows(int limit){
        printTable(limit,rowMap.keySet());
    }

    public void sortBy(String colName, int limit) throws Exception{
        if(!typeMap.containsKey(colName))
            throw new Exception("the col " + colName + " does not exist");

        limit = Math.min(rowMap.size(),limit);
        sortedList = new ArrayList<>();
        for(String row : rowMap.keySet()){
           sortedList.add(row);
        }
       Collections.sort(sortedList, new Comparator<String>() {
           @Override
           public int compare(String row1Id, String row2Id) {
               Cell c1 = rowMap.get(row1Id).getCell(colName), c2 = rowMap.get(row2Id).getCell(colName);
               return c1.compareTo(c2);
           }
       });
        printTable(limit,sortedList);
    }
    public void aggregate(String colName) throws Exception{
        if(!typeMap.containsKey(colName))
            throw new Exception("col " + colName + "does not exist");

        HashMap<String, Integer> freqMap = new HashMap<>();
        for(String i : rowMap.keySet()){
            String value = rowMap.get(i).getCell(colName).getValue() + " ";
            if(!freqMap.containsKey(value))
                freqMap.put(value, 1);
            else
                freqMap.put(value,freqMap.get(value) + 1);
        }
        System.out.print(String.format("%-10s",colName));
        System.out.println(String.format("%-10s","Count"));
        for(Map.Entry<String,Integer> entry : freqMap.entrySet()){
            System.out.print(String.format("%-10s",entry.getKey() + ""));
            System.out.print(String.format("%-10s",entry.getValue() + ""));
            System.out.print("\n");
        }
    }
    private void printTable(int limit,Collection<String> keys){
        limit = Math.min(limit,rowMap.size());
        for(String colName : typeMap.keySet()){
            String formatCol = String.format("%-10s",colName);
            System.out.print(formatCol);
        }
        System.out.print("\n");
        int count = 0;
        for(String key : keys){
            rowMap.get(key).printRow();
            count++;
            if(count == limit)
                break;

            System.out.print("\n");
        }
        System.out.println("\n");
    }

    public Map<String, String> getTypeMap() {
        return typeMap;
    }

}