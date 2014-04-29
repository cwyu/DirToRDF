/*
 * Classname
 *
 *   ParseXML
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
 */

package openisdm;

import java.io.*;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;

/*
 * Description
 *
 * TODO
 */
public class ParseXML
{

    public static void main(String [] args)
    {

        try {
            String host = getNodeValue("config.xml", "/config/cwb-ftp/host");
            System.out.println("host = " + "\"" + host + "\"");

            String port = getNodeValue("config.xml", "/config/cwb-ftp/port");
            System.out.println("port = " + "\"" + port + "\"");

            String username = getNodeValue("config.xml", "/config/cwb-ftp/username");
            System.out.println("username = " + "\"" + username + "\"");

            String password = getNodeValue("config.xml", "/config/cwb-ftp/password");
            System.out.println("password = " + "\"" + password + "\"");

            String timeout = getNodeValue("config.xml", "/config/cwb-ftp/timeout");
            System.out.println("timeout = " + "\"" + timeout + "\"");

            String logFile = getNodeValue("config.xml", "/config/cwb-ftp/logFile");
            System.out.println("logFile = " + "\"" + logFile + "\"");

            String localPath = getNodeValue("config.xml", "/config/cwb-ftp/localPath");
            System.out.println("localPath = " + "\"" + localPath + "\"");

            String isLocalPassiveMode = getNodeValue("config.xml", "/config/cwb-ftp/isLocalPassiveMode");
            System.out.println("isLocalPassiveMode = " + "\"" + isLocalPassiveMode + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public static String getNodeValue(String fileName, String nodeName) throws Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = new InputSource(fileName);
        String result = xpath.evaluate(nodeName, inputSource);

        return result;
    }
}
