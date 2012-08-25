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

/*
 * Description
 *
 * TODO
 */
public class FileTraversal
{
    public static void main(String [] args)
    {

        new FileTraversal() {
            public void onFile( final File f ) {
                System.out.println(f);
            }
        }.traverse(new File("./data/"));
        //}.traverse(new File("somedir"));


        //        // get CWB data and save to local
        //        String serverIP = "ftpsv.cwb.gov.tw";
        //        String localPath = "./data/";
        //        new Crawler().getFTP(serverIP, localPath);

        // exit
        System.exit(0);
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
                onDirectory(file);
                final File [] childs = file.listFiles();
                for (File child : childs) {
                    traverse(child);
                }
                return;
            }
            onFile(file);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }



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
    }
}
