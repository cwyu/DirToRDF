/*
 * Classname
 *
 *   Main
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

/*
 * Description
 *
 */
public class Main
{
    public static void main(String [] args)
    {
        // get CWB data and save to local
        String serverIP = "ftpsv.cwb.gov.tw";
        String localPath = "./output/ftp-data/";
        new Crawler().getFTP(serverIP, localPath);

        // generate RDF
        new FileTraversal();

        // exit
        System.exit(0);
    }
}
