// http://jena.apache.org/tutorials/rdf_api.html
// http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/vocabulary/package-tree.html
// http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/vocabulary/DC.html
// http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/rdf/model/Resource.html
// http://jena.sourceforge.net/IO/iohowto.html

/*
 * Classname
 *
 *   Crawler
 * 
 * Version information
 *
 *   Beta v0.1
 *
 * Date
 *
 *   2012-08-15 Old Yu
 * 
 * Copyright notice
 *
 *   Open source
 * 
 * TODO
 *
 *   1. Preserve timestamp on received files
 *   2. Check difference between local copy and remote file before downloading it
 *   3. Log file timestamp format
 */

package openisdm;

import java.io.*;
//import java.util.Date;
//import java.text.DateFormat;
//import org.apache.commons.net.ftp.*;

// The ARQ application API.
import org.openjena.atlas.io.IndentedWriter;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.vocabulary.DC;

/*
 * Description
 *
 *
 * TODO
 */
public class GenerateRDF
{

    public static void main(String [] args)
    {

        try {
            // read config.xml
            String rootPath = ParseXML.getNodeValue("config.xml", "/config/generate-rdf/rootPath");
            System.setProperty("user.dir", rootPath);

            // generate RDF
            new GenerateRDF().new FromFileHierarchy(rootPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // exit
        System.exit(0);
    }

    public class FromFileHierarchy
    {
        // Jena used
        private Model model;

        FromFileHierarchy(String rootPath) throws Exception
        {
            // read config.xml
            String outputPath = ParseXML.getNodeValue("config.xml", "/config/generate-rdf/outputPath");
            String fileName = ParseXML.getNodeValue("config.xml", "/config/generate-rdf/fileName");

            // create output directory
            createLocalDirectory(outputPath);

            // output file
            String outputFile = outputPath + fileName;

            // create object
            model = ModelFactory.createDefaultModel();

            // look into file hierarchy
            traverse(new File(rootPath));

            // generate RDF
            model.write(new FileOutputStream(outputFile + "-triple.rdf"), "N-TRIPLE");
            model.write(new FileOutputStream(outputFile + "-xml.rdf"), "RDF/XML-ABBREV");
            //model.write(System.out, "RDF/XML-ABBREV");
            //model.write(System.out, "N-TRIPLE");
        }

        /*
         * Method Description
         *
         * Parameter
         *
         * Return value
         */
        private void traverse(File file) throws Exception
        {
            if (file.isDirectory()) {
                // RDF part
                String parentName = file.getName();
                Resource parentResource = model.createResource("http://example.openisdm.org/" + parentName);

                onDirectory(file);
                File [] childs = file.listFiles();
                for (File child : childs) {

                    // RDF part
                    String childName = child.getName();
                    Resource childResource = model.createResource("http://example.openisdm.org/" + childName);
                    parentResource.addProperty(DC.title, childResource);

                    // recursive
                    traverse(child);
                }
                return;
            }
            onFile(file);
        }

        /*
         * Method Description
         *
         * Parameter
         *
         * Return value
         */
        private void onDirectory(File d)
        {
            System.out.println(d.getName());
        }

        /*
         * Method Description
         *
         * Parameter
         *
         * Return value
         */
        private void onFile(File f)
        {
            System.out.println(f.getName());
        }
    }    // end of class FromFileHierarchy

    /*
     * Method Description
     *   Create local directory
     *
     * Parameter
     *   path
     *     [in] The path to create the directory.
     *
     * Return value
     *   None.
     */
    private void createLocalDirectory(String path) throws Exception    // Crawler.java has this method too, Q_Q...
    {
        if (new File(path).mkdir() == false) {
            ;
            //// no message is ok (update data)
            //System.out.println("Warning: directory already exists.");
        }
    }
}    // end of class GenerateRDF

/*    
    public static Model createModel()
    {
        Model model = ModelFactory.createDefaultModel() ;
        
        Resource r1 = model.createResource("http://example.org/book#1") ;
        Resource r2 = model.createResource("http://example.org/book#2") ;
        Resource r3 = model.createResource("http://example.org/book#3") ;
        
        r1.addProperty(DC.title, "SPARQL - the book")
          .addProperty(DC.description, "A book about SPARQL") ;
        
        r2.addProperty(DC.title, "Advanced techniques for SPARQL") ;

        r3.addProperty(DC.title, "Jena - an RDF framework for Java")
          .addProperty(DC.description, "A book about Jena") ;

model.write(System.out, "RDF/XML-ABBREV");

        return model ;
    }
*/


//        File root = new File("/home/foobar/Personal/Examples");
//
//        try {
//            String[] extensions = {"xml", "java", "dat"};
//            boolean recursive = true;
//
//            //
//            // Finds files within a root directory and optionally its
//            // subdirectories which match an array of extensions. When the
//            // extensions is null all files will be returned.
//            //
//            // This method will returns matched file as java.io.File
//            //
//            Collection files = FileUtils.listFiles(root, extensions, recursive);
//
//            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
//                File file = (File) iterator.next();
//                System.out.println("File = " + file.getAbsolutePath());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


