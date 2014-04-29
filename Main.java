/*
 * Classname
 *
 *   Main
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

import java.io.*;

/*
 * Description
 *
 */
public class Main
{
    public static void main(String [] args)
    {
        try {
            // read config.xml
            String remoteIP = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/host");
            String localPath = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/localPath");
            String rootPath = ParseXML.getNodeValue("config.xml", "/config/generate-rdf/rootPath");

            // get CWB data and save to local
            new Crawler().getFTP(remoteIP, localPath);

            // generate RDF
            new GenerateRDF().new FromFileHierarchy(rootPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // exit
        System.exit(0);
    }
}
