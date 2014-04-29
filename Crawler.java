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

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.util.Date;
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
        try {
            // read config.xml
            String remoteIP = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/host");
            String localPath = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/localPath");

            // get CWB data and save to local
            new Crawler().getFTP(remoteIP, localPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // exit
        System.exit(0);
    }

    /*
     * Method Description
     *   Get data from FTP, and save files to local.
     *
     * Parameter
     *   remoteIP
     *     [in] server IP address.
     *   localPath
     *     [in] The path to save on local.
     *
     * Return value
     *   None.
     */
    public void getFTP(String remoteIP, String localPath) throws Exception
    {
        // read config.xml
        int port = Integer.parseInt(ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/port"));
        String username = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/username");
        String password = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/password");
        int timeout = Integer.parseInt(ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/timeout"));
        String logFile = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/logFile");
        String isLocalPassiveMode = ParseXML.getNodeValue("config.xml", "/config/cwb-ftp/isLocalPassiveMode");

        // declaration
        String ftpCurrentDirectory = "/";
        FTPClient ftpClient = null;
        FileWriter fileWriter = null;

        try {
            // create FTP object
            ftpClient = new FTPClient();

            // create log directory
            String logPath = new File(logFile).getParent();
            createLocalDirectory(logPath);

            // set log file
            boolean append = true;
            fileWriter = new FileWriter(logFile, append);

            // set timeout
            ftpClient.setConnectTimeout(timeout);

            // connect to FTP
            ftpClient.connect(remoteIP, port);

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
            ftpClient.login(username, password);

            // avoid local <--x-- FTP
            if (isLocalPassiveMode.equals("yes") == true) {
                ftpClient.enterLocalPassiveMode();
            }

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
        } catch (SocketTimeoutException e) {
            System.out.println("Abort: timeout exceeded in " + timeout + "(ms), try to set another value in config.xml file.");
        } catch (Exception e) {
            throw e;
        } finally {
            // release resource
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
            fileWriter.close();
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
        // get FTP files in current directory
        FTPFile[] ftpFiles = ftpClient.listFiles();

        // print message
        System.out.print("Current Directory: " + ftpClient.printWorkingDirectory());
        System.out.println(" (" + ftpFiles.length + " files)");

        // for each file type
        for (int i = 0; i < ftpFiles.length; i++) {
            if (ftpFiles[i].isDirectory()) {    // directory
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
            } else {    // regular file
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
        if (new File(path).mkdir() == false) {
            ;
            //// no message is ok (update data)
            //System.out.println("Warning: directory already exists.");
        }
    }
}
