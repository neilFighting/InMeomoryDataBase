package Shell;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class SyntaxChecker {
    public void typeChecking(String s, Set<String> colNames) throws Exception{
        String[] parts = s.split(" +");
        int i = 1;
        while(i < parts.length - 1){
            String colName = parts[i];
            colNames.add(colName);
            i++;
            if(i == parts.length){
                throw new Exception("miss data type for " + colName);
            }
            String type = parts[i];
            if (!DataBase.validTypes.contains(type))
                throw new Exception("the type of " + colName + " is not valid");
            i++;
        }
    }
    //check for the insert and the update function
    public void formatChecking(String s, Map<String ,String> typeMap) throws Exception{
        String[] pairs = s.split(" +");
        int i = 0;
        while(i < pairs.length){
            String colName = pairs[i];
            if(!typeMap.containsKey(colName))
                throw new Exception("invalid Col:" + colName);

            i++;
            if(i == pairs.length)
                throw new Exception("missing value for the column" + colName);

            String value = pairs[i];
            String type = typeMap.get(colName);
            if(type.equals("DOUBLE") && !isDouble(value)||type.equals("INT") && !isInteger(value) || type.equals("FLOAT") && !isFloat(value) || type.equals("CHAR") && value.length() > 1)
                throw new Exception("the given value does not match the type of " + colName);
            i++;
        }
    }
    //check for the insert function
    public void filedChecking(String s, Map<String, String> typeMap) throws Exception{
        String[] part = s.split(" +");
        Set<String> allCols = new HashSet<>(typeMap.keySet());
        int i = 0;
        while(i < part.length){
            String colName = part[i];
            if(allCols.contains(colName)){
                allCols.remove(colName);
            }
            i += 2;
        }
        if(allCols.size() > 0)
            throw new Exception("Invalid insert");
    }
    public void pkChecking(Set<String> colNames, String s) throws Exception{
        String[] parts = s.split(" +");
        if(parts.length != 3 || !parts[1].equals("Key") || !colNames.contains(parts[2]))
            throw new Exception("invalid primary key");
    }
    public boolean isInteger(String s) {

        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
    public static  boolean isFloat(String s){

        try{
            Float.parseFloat(s);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    public boolean isDouble(String s){
        try{
            Double.parseDouble(s);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
