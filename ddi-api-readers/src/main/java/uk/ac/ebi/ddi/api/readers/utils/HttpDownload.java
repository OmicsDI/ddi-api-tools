package uk.ac.ebi.ddi.api.readers.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 09/11/15
 */
public class HttpDownload {

    public static InputStream getPage(String urlString) throws Exception {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(10000); //set timeout to 10 seconds

        connection.setReadTimeout(300000); // set timeout to 10 seconds

        connection.connect();

        return connection.getInputStream();
    }

    public static InputStream getPage(URI uri) throws Exception {

        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

        connection.setConnectTimeout(10000); //set timeout to 10 seconds

        connection.setReadTimeout(300000); // set timeout to 10 seconds

        connection.connect();

        return connection.getInputStream();
    }
}
