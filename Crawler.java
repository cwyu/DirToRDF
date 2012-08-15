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
import java.util.Date;
import java.text.DateFormat;
import org.apache.commons.net.ftp.*;

/*
 * Description
 *   Web/FTP crawler.
 *
 * TODO
 *   Web crawler.
 */
public class Crawler
{
    public static void main(String [] args)
    {
        // get CWB data and save to local
        String serverIP = "ftpsv.cwb.gov.tw";
        String localPath = "./data/";
        new Crawler().getFTP(serverIP, localPath);

        // exit
        System.exit(0);
    }

    /*
     * Method Description
     *   Get data from FTP, and save files to local.
     *
     * Parameter
     *   serverIP
     *     [in] server IP address.
     *   localPath
     *     [in] The path to save on local.
     *
     * Return value
     *   None.
     */
    public void getFTP(String serverIP, String localPath)
    {
        String ftpCurrentDirectory = "/";
        String logFileName = "./log/ftp.log";
        FTPClient ftpClient = null;
        FileWriter fileWriter = null;

        try {
            // create FTP object
            ftpClient = new FTPClient();

            // open log file
            boolean append = true;
            fileWriter = new FileWriter(logFileName, append);

            // connect to FTP
            ftpClient.connect(serverIP);

            // check FTP reply code
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }

            // print connect message
            System.out.println("### Connect to FTP server successful. ###");
            System.out.println(ftpClient.getReplyString());

            // login to FTP
            String user = "anonymous";
            String password = "";
            ftpClient.login(user, password);

            // avoid local <--x-- FTP
            ftpClient.enterLocalPassiveMode();

            // check FTP reply code
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.logout();
                ftpClient.disconnect();
                System.err.println("FTP server login failed.");
                System.exit(1);
            }

            // print login message
            System.out.println(ftpClient.getReplyString());
            System.out.println("Current Directory: " + ftpClient.printWorkingDirectory());

            // init FTP working directory
            if (ftpClient.changeWorkingDirectory(ftpCurrentDirectory) == false) {
                System.out.println("Warning: cannot change to " + ftpCurrentDirectory + ".");
            }

            // create output directory
            createLocalDirectory(localPath);

            // get data from FTP and save to local
            getData(ftpClient, ftpCurrentDirectory, fileWriter, localPath);
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            // release resource
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
                fileWriter.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Method Description
     *   For each file in ftpCurrentDirectory, 
     *   if the file type is directory, then go to sub-directory recursively, 
     *   otherwise (regular file), save it to local.
     *
     * Parameter
     *   ftpClient
     *     [in] FTPClient object.
     *   ftpCurrentDirectory
     *     [in] FTP current directory.
     *   fileWriter
     *     [in] FileWriter object.
     *   localPath
     *     [in] The path to save on local.
     *
     * Return value
     *   None.
     */
    private void getData(FTPClient ftpClient, String ftpCurrentDirectory, FileWriter fileWriter, String localPath) throws Exception
    {
        try {
            // get FTP files in current directory
            FTPFile[] ftpFiles = ftpClient.listFiles();

            // print message
            System.out.print("Current Directory: " + ftpClient.printWorkingDirectory());
            System.out.println(" (" + ftpFiles.length + " files)");

            // for each file type
            for (int i = 0; i < ftpFiles.length; i++) {
                // directory
                if (ftpFiles[i].isDirectory()) {
                    // create local directory
                    createLocalDirectory(localPath + File.separator + ftpFiles[i].getName());

                    // before recursive call
                    String tmpLocalPath = localPath;
                    String tmpFTPCurrentDirectory = ftpCurrentDirectory;
                    localPath += File.separator + ftpFiles[i].getName();
                    ftpCurrentDirectory += File.separator + ftpFiles[i].getName();
                    if (ftpClient.changeWorkingDirectory(ftpCurrentDirectory) == false) {
                        System.out.println("Warning: cannot change to " + ftpCurrentDirectory + ".");
                    }

                    // recursive call
                    getData(ftpClient, ftpCurrentDirectory, fileWriter, localPath);

                    // after recursive call
                    localPath = tmpLocalPath;
                    ftpCurrentDirectory = tmpFTPCurrentDirectory;
                    if (ftpClient.changeWorkingDirectory(ftpCurrentDirectory) == false) {
                        System.out.println("Warning: cannot change to " + ftpCurrentDirectory + ".");
                    }
                // regular file
                } else {
                    // print message
                    System.out.println(ftpFiles[i].getName());

                    // save file to local
                    File file = new File(localPath + File.separator + ftpFiles[i].getName());
                    FileOutputStream fileOutputStream = new FileOutputStream(file); 
                    ftpClient.retrieveFile(ftpFiles[i].getName(), fileOutputStream);
                    fileOutputStream.close();

                    // log file timestamp formating
                    String timestamp = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date());
                    fileWriter.write("[CWB FTP] [" + ftpClient.printWorkingDirectory() + "] [" + ftpFiles[i].getName() + "] [" + timestamp + "]" + "\n");
                }
            }
        }
        catch (Exception e) {
            throw new Exception();
        }
    }

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
    private void createLocalDirectory(String path) throws Exception
    {
        try {
            if (new File(path).mkdir() == false) {
                ;
                //// no message is ok (update data)
                //System.out.println("Warning: directory already exists.");
            }
        }
        catch (Exception e) {
            throw new Exception();
        }
    }
}
