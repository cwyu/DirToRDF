// http://jena.apache.org/tutorials/rdf_api.html
// http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/vocabulary/package-tree.html
// http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/vocabulary/DC.html
// http://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/rdf/model/Resource.html

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
import org.openjena.atlas.io.IndentedWriter ;

import com.hp.hpl.jena.graph.Triple ;
import com.hp.hpl.jena.query.Query ;
import com.hp.hpl.jena.query.QueryExecution ;
import com.hp.hpl.jena.query.QueryExecutionFactory ;
import com.hp.hpl.jena.query.QueryFactory ;
import com.hp.hpl.jena.query.QuerySolution ;
import com.hp.hpl.jena.query.ResultSet ;
import com.hp.hpl.jena.rdf.model.Literal ;
import com.hp.hpl.jena.rdf.model.Model ;
import com.hp.hpl.jena.rdf.model.ModelFactory ;
import com.hp.hpl.jena.rdf.model.RDFNode ;
import com.hp.hpl.jena.rdf.model.Resource ;
import com.hp.hpl.jena.sparql.core.Var ;
import com.hp.hpl.jena.sparql.syntax.ElementGroup ;
import com.hp.hpl.jena.vocabulary.DC ;

/*
 * Description
 *
 * TODO
 */
public class FileTraversal
{

    public Model model;
    //private Model model;

    public static void main(String [] args)
    {

//        try {
//            SAXBuilder parser = new SAXBuilder();
//            Document docConfig = parser.build("config.xml");
//            //Element elConfig = docConfig.getRootElement();
//            //String host = elConfig.getChildText("host");
//            //System.out.println("@@@@@@@@@@@@@@host = " + host);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            String host = ConfigXML.getNodeValue("config.xml", "/config/cwb-ftp/host");
            System.out.println("host = " + "\"" + host + "\"");

            String port = ConfigXML.getNodeValue("config.xml", "/config/cwb-ftp/port");
            System.out.println("port = " + "\"" + port + "\"");

            String username = ConfigXML.getNodeValue("config.xml", "/config/cwb-ftp/username");
            System.out.println("username = " + "\"" + username + "\"");

            String password = ConfigXML.getNodeValue("config.xml", "/config/cwb-ftp/password");
            System.out.println("password = " + "\"" + password + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }


        String basePath = "./output/ftp-data/";
        System.setProperty("user.dir", "./output/ftp-data/");
        FileTraversal fileTraversal = new FileTraversal();
        fileTraversal.traverse(new File(basePath));
        //fileTraversal.model.write(System.out, "RDF/XML-ABBREV");
        // haha
        fileTraversal.model.write(System.out, "N-TRIPLE");

//        new FileTraversal() {
//            public void onFile(final File f) {
//                System.out.println(f);
//            }
//        }.traverse(new File("./output/ftp-data/"));


        //        // get CWB data and save to local
        //        String serverIP = "ftpsv.cwb.gov.tw";
        //        String localPath = "./data/";
        //        new Crawler().getFTP(serverIP, localPath);

        // exit
        System.exit(0);
    }

    FileTraversal ()
    {
        // RDF part
        model = ModelFactory.createDefaultModel() ;


//        System.out.println("@@@@@@@@@@@@@@");
//        try {
//            System.setProperty("user.dir", "./output/ftp-data/");
//        } 
//        catch (Exception e) {
//            System.out.println("QQ...");
//            e.printStackTrace();
//        }
    }

    /*
     * Method Description
     *
     * Parameter
     *
     * Return value
     */
    public final void traverse(final File file)
    //public final void traverse(final File file) throws IOException
    {

        try {
            if (file.isDirectory()) {
                // RDF part
                String parentName = file.getName();
                Resource parentResource = model.createResource("http://example.openisdm.org/" + parentName);

                onDirectory(file);
                final File [] childs = file.listFiles();
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
        catch (Exception e) {
            e.printStackTrace();
        }

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


    }

    /*
     * Method Description
     *
     * Parameter
     *
     * Return value
     */
    public void onDirectory(final File d)
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
    public void onFile(final File f)
    {
        System.out.println(f.getName());
    }
}
