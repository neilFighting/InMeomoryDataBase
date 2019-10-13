**************Supported Data Type *****
    1.CHAR
    2.INT
    3.FLOAT
    4.DOUBLE
    5.VARCHAR

 *************Supported SQL Statement ******

    !!!!all statement are case sensitive
    1.CreateDataBase <dataBaseName>
      Example : CreateDataBase db

    2.CreateTable <TableName> <Column1> <Type1> <Column2> <Type2> ...... Primary Key <Column Name>
      Example : CreateTable Person Name VARCHAR Age INT Height DOUBLE Primary Key Name
                CreateTable Car Brand VARCHAR Seats INT Color CHAR

    3.InsertRow <Column1> <Value1> <Column2> <Value2> <Column3> <Value3> ......
      Example : InsertRow Name neil Age 15 Height 180.0

    4.UpdateRow <Column1> <Value1> <Column2> <Value2> ......
      Example : UpdateRow paul Age 35 Height 185.0

    5.DeleteRow <RowKey>
      Example : DeleteRow neil

    6.Select * From <TableName>
      Example: Select * From Person

    7.Select * From <TableName> Limit <Count>
      Example: Select * From Person Limit 2

    8.Select * From <tableName> Order By <Column1> Limit <Count>;
      Example: Select * From Person Order By Age Limit 4

    9.Select <ColumnName> Count(*) From <TableName> Group By <ColumnName>
      Example: Select Age Count(*) From Person Group By Age

  ***********How to Start ***********

    use start() in class Shell
    type sql statement in console