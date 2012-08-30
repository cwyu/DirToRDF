/*
 * Classname
 *
 *   ConfigXML
 * 
 * Version information
 *
 *   Beta v0.1
 *
 * Date
 *
 *   2012-08-30 Old Yu
 * 
 * Copyright notice
 *
 *   Open source
 */

package openisdm;

import java.io.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import org.xml.sax.InputSource;

/*
 * Description
 *
 * TODO
 */
public class ConfigXML
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
        String value = xpath.evaluate(nodeName, inputSource);

        return value;
    }
}
