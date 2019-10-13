package Shell;

import java.util.*;
//----------------------------------------Shell----------------------------------------
//Class shell is designed to read user's input and execute the corresponding operation
//Variable SyntaxChecker helps check if input is legal sql statement
//Variable DataBase stores the actual data

public class Shell {
    private String  command;
    private final int WHITE_SPACE_LENGTH = 1;
    private DataBase dataBase;
    private SyntaxChecker sqlChecker;
    public Shell(){
        sqlChecker = new SyntaxChecker();
    }

    //start the the shell, type sql statement in console
    public void start(){
        Scanner sc = new Scanner(System.in);
        while(true){
            String sql = sc.nextLine();
            executeSQL(sql);
        }
    }
    //map input to corresponding operation && handle Exception
    public boolean executeSQL(String sql){
        try{
            if(sql.indexOf("CreateDataBase") != -1){
                createDataBase(sql);
            }else if(sql.indexOf("CreateTable") != -1){
                createTable(sql);
            }else if(sql.indexOf("InsertRow") != -1){
                insertRow(sql);
            }else if(sql.indexOf("DeleteRow") != -1){
                deleteRow(sql);
            }else if(sql.indexOf("UpdateRow") != -1){
                updateRow(sql);
            }else if(sql.indexOf("Select *") != -1){
                if(sql.indexOf("Order By") != -1){
                    orderBy(sql);
                }else if(sql.indexOf("Limit") != -1){
                    selectLimit(sql);
                }else{
                    selectAll(sql);
                }
            }else if(sql.indexOf("Group By") != -1){
                aggregate(sql);
            }else{
                throw new Exception("Invalid Or Unsupported Command");
            }
            return true;
        }catch(Exception e){
              System.out.println("----" +e.getMessage() + "----");
              return false;
        }
  }
  //function that handle----createDate---- statement
   private void createDataBase(String sql) throws Exception{
        String[] parts = sql.split(" +");
        if(parts.length != 2)
            throw new Exception("invalid create command, please use CreateDatabase <Name>");

        this.dataBase = new DataBase(parts[1]);
    }
    //function that handle----createTable----statement
    private void createTable(String sql) throws Exception{
        this.command = "CreateTable";
        sql = sql.substring(command.length() + WHITE_SPACE_LENGTH);
        int index = sql.indexOf("Primary");
        try{
            Set<String> colNames = new HashSet<>();
            if(index == -1){
                sqlChecker.typeChecking(sql,colNames);

                this.dataBase.createTable(sql);
            }
            else{
                String[] parts = sql.split(" +");
                String pk = parts[parts.length - 1];
                sqlChecker.typeChecking(sql.substring(0,index),colNames);
                sqlChecker.pkChecking(colNames, sql.substring(index));

                this.dataBase.createTable(sql.substring(0,index));
                this.dataBase.getCurrentTable().setPrimaryKey(pk);
            }
        }catch (Exception e){
            throw e;
        }
    }
    //function that handle====insertRow----statement
    private void insertRow(String sql) throws Exception{
        this.command = "InsertRow";
        sql = sql.substring(command.length() + WHITE_SPACE_LENGTH);
        try{
            sqlChecker.filedChecking(sql,dataBase.getCurrentTable().getTypeMap());
            sqlChecker.formatChecking(sql,dataBase.getCurrentTable().getTypeMap());

            dataBase.getCurrentTable().insertRow(sql);
        }catch (Exception e){
            throw e;
        }
    }
    //function that handle----deleteRow----statement
    private void deleteRow(String sql) throws Exception{
        String[] parts = sql.split(" +");
        if(parts.length != 2){
            throw new Exception("invalid Delete, please follow the pattern DeleteRow <RowKey>");
        }

        try{
            this.dataBase.getCurrentTable().deleteRow(parts[1]);
        }catch (Exception e){
            throw e;
        }
    }
    //function that handle ----updateRow----
    private void updateRow(String sql) throws Exception{
        this.command = "UpdateRow";
        String[] parts = sql.split(" +");
        if(parts.length < 2){
            throw new Exception("Invalid updatee, please follow the pattern UpdateRow <RowKey> <Key,Value>");
        }
        String pk = parts[1];
        String values = sql.substring(command.length() + WHITE_SPACE_LENGTH + pk.length() + WHITE_SPACE_LENGTH);
        try{
            sqlChecker.formatChecking(values,this.dataBase.getCurrentTable().getTypeMap());
        }catch (Exception e){
            throw e;
        }

        try{
            this.dataBase.getCurrentTable().updateRow(pk,values);
        }catch (Exception e){
            throw e;
        }
    }
    //function that handle ---Select * From -----
    private void selectAll(String sql) throws Exception{
        String[] parts =  sql.split(" +");
        if(parts.length != 4 || !parts[1].equals("*") || !parts[2].equals("From"))
            throw new Exception("invalid command, please follow the pattern Select * From <TableName> ");

        String tableName = parts[3];
        try{
            Table table = this.dataBase.getTable(tableName);
            table.getAllRows();
        }catch (Exception e){
            throw e;
        }
    }
    //function that handle ---Select * From  Limit----
    private void selectLimit(String sql) throws Exception{
        String[] parts = sql.split(" +");
        if(parts.length != 6 || !parts[1].equals("*") || !parts[2].equals("From") || !parts[4].equals("Limit"))
            throw new Exception("Invalid command, please follow the pattern Select * From <TableName> Limit <count>");

        String tableName = parts[3];
        int count = Integer.valueOf(parts[5]);
        if(!sqlChecker.isInteger(parts[5]) || count < 0)
            throw new Exception("Invalid command, limit must be an integer greater than 0");

        Table table = this.dataBase.getTable(tableName);
        table.getLimitRows(count);
    }
    //function that handle ---- Order By ----
    private void orderBy(String sql) throws Exception{
        String[] parts = sql.split(" +");
        if(parts.length != 9 || !parts[1].equals("*") || !parts[2].equals("From")|| !parts[4].equals("Order")|| !parts[5].equals("By")|| !parts[7].equals("Limit"))
            throw new Exception("Invalid command, please follow the pattern Select * From <TableName> Order By <ColName> Limit <count>");

        if(!sqlChecker.isInteger(parts[8]))
            throw new Exception("The value of the Limit must be Integer");

        int limit = Integer.valueOf(parts[8]);
        String tableName = parts[3],colName = parts[6];
        int count = Integer.valueOf(parts[8]);
        try{
            Table table = this.dataBase.getTable(tableName);
            table.sortBy(colName,limit);
        }catch (Exception e){
            throw e;
        }

    }
    //function that handle ----Group By----
    private void aggregate(String sql) throws Exception{
        String[] parts = sql.split(" +");
        if(parts.length != 8 || !parts[0].equals("Select") || !parts[2].equals("Count(*)") || !parts[3].equals("From") || !parts[5].equals("Group") || !parts[6].equals("By"))
            throw new Exception("Invalid Command, please follow the pattern Select <ColName> Count(*) From <TableName> Group By<ColName>");

        try{
            String tableName = parts[4], colName = parts[1];
            Table table = this.dataBase.getTable(tableName);
            table.aggregate(colName);
        }catch (Exception e){
            throw e;
        }
    }

}
