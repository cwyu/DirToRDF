// http://boards.openlinksw.com/phpBB3/viewtopic.php?f=5&t=1191
//
import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.sql.*;

class sample
  {
  public static void PrintField (String name)
    {
      if (name == null)
        name = "NULL";
      System.out.print (name + " ");
    }

  public static void
  ExecuteQuery (Connection conn, String query)
      throws Exception
    {
      ResultSetMetaData meta;
      Statement stmt;
      ResultSet result;
      int count;

      System.out.println ("EXECUTE: " + query);

      stmt = conn.createStatement ();
      result = stmt.executeQuery (query);

      meta = result.getMetaData ();
      count = meta.getColumnCount ();
      for (int c = 1; c <= count; c++)
   PrintField (meta.getColumnName (c));
      System.out.println ("\n--------------");

      while (result.next())
        {
     for (int c = 1; c <= count; c++)
       PrintField (result.getString (c));
     System.out.println ("");
   }
      stmt.close ();
      System.out.println ("");
    }

  public static void main (String argv[])
      throws Exception
    {
      // Connection conn;

      // Add the OpenLink JDBC driver to the system properties
      // Properties p = System.getProperties ();
      // p.put ("jdbc.drivers", "virtuoso.jdbc3.Driver");
      // System.setProperties (p);

      // conn = DriverManager.getConnection ("jdbc:virtuoso://localhost:1111/UID=dba/PWD=dba");

   Class.forName("virtuoso.jdbc3.Driver");
   Connection conn = DriverManager.getConnection("jdbc:virtuoso://localhost:1111","dba","dba");
   // Statement stmt = conn.createStatement();

      // Query database ...
      // for (int i = 0; i < 10; i++)
        ExecuteQuery (conn, "SPARQL SELECT * WHERE {?s ?p ?o} LIMIT 10");
    }
}
