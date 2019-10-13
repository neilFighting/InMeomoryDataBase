package Shell;

import java.util.*;

public class Row<T> {
    private LinkedHashMap<String, Cell> cellMap;
    public Row(String s,Map<String, String> typeMap){
        cellMap = new LinkedHashMap<>();

        setValue(s,typeMap);
    }

    public Cell getCell(String colName){
        return cellMap.get(colName);
    }
    public void setValue(String s,Map<String, String> typeMap){

        String[] parts = s.split(" +");
        for(int i = 0; i < parts.length - 1; i += 2){
            String colName = parts[i], value = parts[i + 1],type = typeMap.get(colName);
            Cell cell = null;
            if(type.equals("INT")){
                cell = new Cell(Integer.valueOf(value));
            }else if(type.equals("FLOAT")){
                cell = new Cell(Float.valueOf(value));
            }else if(type.equals("CHAR")){
                cell = new Cell(value.charAt(0));
            }else{
                cell = new Cell(value);
            }
            cellMap.put(colName,cell);
        }
    }
    public void printRow(){
        for(String colName : this.cellMap.keySet()){
            String formatValue = String.format("%-10s",this.cellMap.get(colName).getValue());
            System.out.print(formatValue);
        }
    }
}








