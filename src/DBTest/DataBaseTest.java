package DBTest;

import Shell.*;

public class DataBaseTest {
    //@BeforeClass
    private Shell shell;

    private void init() {
        shell = new Shell();
        shell.executeSQL("CreateDataBase db");
        shell.executeSQL("CreateTable Person Name VARCHAR Age INT Height DOUBLE Primary Key Name");
        shell.executeSQL("InsertRow Name neil Age 15 Height 180.0");
        shell.executeSQL("InsertRow Name paul Age 25 Height 182.2");
        shell.executeSQL("InsertRow Name simon Age 22 Height 175.5");
        shell.executeSQL("InsertRow Name Jojo Age 22 Height 162.1");
        shell.executeSQL("CreateTable Car Brand VARCHAR Seats INT Color CHAR");
        shell.executeSQL("InsertRow Brand Honda Seats 4 Color W");
        shell.executeSQL("InsertRow Brand Ford Seats 2 Color R");
        shell.executeSQL("InsertRow Brand Audi Seats 2 Color S");
        shell.executeSQL("InsertRow Brand BMW Seats 4 Color R");
    }

    //Test For ----------Create Table---------------------------
    //the supported datatype : INT CHAR VARCHAR DOUBLE
    public void testCreateTable() {
        //(1)Test for when create table correctly
        sqlTest("CreateTable Player Name VARCHAR Age INT Height DOUBLE", shell);

        //(2)Test for the invalid dataType
        sqlTest("CreateTable Teacher Name String Age Integer", shell);
    }

    //Test For ----------Insert Row---------------------------
    public void testInsertRow() {
        init();
        System.out.println("Before insert, the table is :");
        shell.executeSQL("Select * From Person");
        //(1) syntax is correct
        sqlTest("InsertRow Name Christina Age 28 Height 156.2", shell);
        System.out.println("After insert, the table is :");
        shell.executeSQL("Select * From Person");
        //(2) missing field
        sqlTest("InsertRow Name tom", shell);
        System.out.println("After insert, the table is :");
        shell.executeSQL("Select * From Person");
        //(3) unmatched data type
        sqlTest("InsertRow Name niel Age sixteen", shell);
        System.out.println("After insert, the table is :");
        shell.executeSQL("Select * From Person");
    }

    //Test For ----------Delete Row---------------------------
    public void testDeleteRow(){
        init();
        System.out.println("Before the delete, the table is :");
        shell.executeSQL("Select * From Person");
        //correct syntax
        sqlTest("DeleteRow neil",shell);
        System.out.println("After the delete, the table is :");
        //deleting non-existent row
        shell.executeSQL("Select * From Person");
        sqlTest("DeleteRow blabla",shell);
        System.out.println("Aftere the delete, the table is :");
        shell.executeSQL("Select * From Person");
    }
    //Test For ----------Update Row---------------------------
    public void testUpdateRow(){
        init();
        System.out.println("Before update, the table is :");
        //(1) syntax is correct
        shell.executeSQL("Select * From Person");
        sqlTest("UpdateRow paul Age 35 Height 185.0",shell);
        System.out.println("After the update, the table is :");
        shell.executeSQL("Select * From Person");

        //(2) test for non-exist row
        sqlTest("UpdateRow blablabla Age 35 Height 185.0",shell);
        System.out.println("After the update, the table is :");
        shell.executeSQL("Select * From Person");

        //(3) test for unmatched datatype
        sqlTest("UpdateRow Jojo Height tall",shell);
        System.out.println("After the update, the table is :");
        shell.executeSQL("Select * From Person");
    }
    //Test For ----------Select *---------------------------
    public void testGetAll(){
        init();
        //(1)get all rows from table Person
        sqlTest("Select * From Person",shell);
        //(2)get all rows from table Car
        sqlTest("Select * From Car",shell);
        //(3)get all rows from non-existent Table
        sqlTest("Select * From FOO",shell);
    }
    //Test For ----------Select * From Limit---------------------------
    public void testGetLimit(){
        init();
        //(1)get limit rows from table Person
        sqlTest("Select * From Person Limit 2",shell);
        //(2)test for condition where value of limit is bigger than the size of the table
        sqlTest("Select * From Person Limit 20",shell);
        //(3)test for condition where value of limit is negative
        sqlTest("Select * From Person Limit -2",shell);

    }
    //Test For ----------Order By---------------------------
    public void testOrderBy(){
        init();
        //(1)test for Person Table sorted by Name (data type : VARCHAR)
        sqlTest("Select * From Person Order By Name Limit 4",shell);
        //(2)test for Person Table sorted by Age (data type : INT)
        sqlTest("Select * From Person Order By Age Limit 4",shell);
        //(3)test for Person Table sorted by Height(data type : DOUBLE
        sqlTest("Select * From Person Order By Height Limit 4",shell);
        //(4)test for Car Table sorted by Color(data type : CHAR)
        sqlTest("Select * From Car Order By Color Limit 4",shell);
        //(5)test for non-existent column
        sqlTest("Select * From Car Order By Length Limit 4",shell);
    }
    //Test For ----------Aggregate ---------------------------
    public void testAggregate(){
        init();
        //(1)test for group by primary key
        sqlTest("Select Name Count(*) From Person Group By Name",shell);
        //(2)test for group by other column
        sqlTest("Select Age Count(*) From Person Group By Age",shell);
    }
    //----------Unit Test Entrance---------------------------
    public static void main(String[] args) {


        DataBaseTest test = new DataBaseTest();
        //entrance of the unit test


    }

    public void sqlTest(String sql, Shell shell) {
        System.out.println("----test result for '" + sql + "':----");
        if (shell.executeSQL(sql)) {
            System.out.println("----sql executed successfully----");
        }else{
            System.out.println("----unable to execute sql");
        }
    }
}
