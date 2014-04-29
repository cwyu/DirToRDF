/*
 * Classname
 *
 *   Crawler
 * 
 * Version information
 *
 *   v0.2
 *
 * Date
 *
 *   2012-08-15 Cheng-Wei Yu (Old Yu)
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
import java.io.*;
import org.openjena.atlas.io.IndentedWriter;

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
}
