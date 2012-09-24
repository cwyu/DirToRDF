// Connect to Virtuoso sql database
// Problems: (only xml.rdf works, triple.rdf ng) (cannot upload local file)
//
// http://boards.openlinksw.com/phpBB3/viewtopic.php?f=5&t=1191
// http://docs.openlinksw.com/virtuoso/VirtuosoDriverJDBC.html
// http://stackoverflow.com/questions/5531224/setup-rdf-ontology-with-virtuoso    # Setup RDF ontology with Virtuoso
// http://www.openlinksw.com/dataspace/dav/wiki/Main/VirtJenaProvider             # Virtuoso Jena Provider
// http://stackoverflow.com/questions/5531224/setup-rdf-ontology-with-virtuoso
// http://ods.openlinksw.com/dataspace/dav/wiki/Main/VirtRDFInsert
// http://stackoverflow.com/questions/9279258/how-to-upload-rdf-files-to-virtuoso-with-java
// http://answers.semanticweb.com/questions/12996/how-can-use-sparql-to-local-rdf-files

import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.sql.*;

class Virtuoso
{
    public static void PrintField(String name)
    {
        if (name == null) {
            name = "NULL";
        }
        System.out.print(name + " ");
    }

    public static void ExecuteQuery(Connection conn, String query) throws Exception
    {
        ResultSetMetaData meta;
        Statement stmt;
        ResultSet result;
        int count;

        System.out.println("EXECUTE: " + query);

        stmt = conn.createStatement();
        result = stmt.executeQuery(query);

        meta = result.getMetaData();
        count = meta.getColumnCount();
        for (int c = 1; c <= count; c++) {
            PrintField(meta.getColumnName(c));
        }
        System.out.println("\n--------------");

        while (result.next()) {
            for (int c = 1; c <= count; c++) {
                PrintField(result.getString(c));
            }
            System.out.println("");
        }
        stmt.close();
        System.out.println("");
    }

    public static void main(String [] args)
    {
        // Connection conn;

        // Add the OpenLink JDBC driver to the system properties
        // Properties p = System.getProperties();
        // p.put("jdbc.drivers", "virtuoso.jdbc4.Driver");
        // System.setProperties(p);

        // conn = DriverManager.getConnection("jdbc:virtuoso://localhost:1111/UID=dba/PWD=dba");

        try {

            Class.forName("virtuoso.jdbc4.Driver");
            //DriverManager.setLoginTimeout(1);
            //Connection conn = DriverManager.getConnection("jdbc:virtuoso://vrtestbed.iis.sinica.edu.tw","dba","iis404");
            Connection conn = DriverManager.getConnection("jdbc:virtuoso://vrtestbed.iis.sinica.edu.tw/charset=UTF-8/log_enable=2","dba","iis404");

            System.out.println("###333333333###");

            // origin
            //ExecuteQuery(conn, "SPARQL SELECT * WHERE {?s ?p ?o} LIMIT 10");
            //ExecuteQuery(conn, "SPARQL SELECT * WHERE {?s ?p ?o} LIMIT 30");

            //// Jim's Ruby
            //curl -F "query=select * from <http://openisdm.com> where {?s ?o ?p}" http://vrtestbed.iis.sinica.edu.tw:8890/sparql/
            //ExecuteQuery(conn, "SPARQL SELECT * from <http://openisdm.com> WHERE {?s ?p ?o} LIMIT 30");

            // Drop from virtuoso before upload to use the same URL
            ExecuteQueryUploadRDF(conn, "SPARQL DROP SILENT GRAPH <http://cwyu.com>");

            // Upload RDF & register URL on virtuoso
            ExecuteQueryUploadRDF(conn, "SPARQL LOAD <http://cwyubsd.no-ip.org/cwyu.rdf> INTO GRAPH <http://cwyu.com>");
            //// fail to upload localhost rdf to virtuoso
            //ExecuteQueryUploadRDF(conn, "SPARQL LOAD <http://localhost/haha-xml.rdf> INTO GRAPH <http://cwyu.com>");

            // Query from virtuoso
            ExecuteQuery(conn, "SPARQL SELECT * from <http://cwyu.com> WHERE {?s ?p ?o} LIMIT 400");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // cwyu
    public static void ExecuteQueryUploadRDF(Connection conn, String query) throws Exception
    {
        ResultSetMetaData meta;
        Statement stmt;
        ResultSet result;

        System.out.println("EXECUTE: " + query);

        stmt = conn.createStatement();
        result = stmt.executeQuery(query);

        stmt.close();
        System.out.println("");
    }
}
