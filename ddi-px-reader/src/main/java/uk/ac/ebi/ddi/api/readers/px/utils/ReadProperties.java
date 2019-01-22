package uk.ac.ebi.ddi.api.readers.px.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class allows to retrieve the information related with the properties of the px services
 * such as the URL of the service the RSS url, etc. It can be use to define other parameters.
 *
 * @author ypriverol
 */
public class ReadProperties {

    private static ReadProperties instance = null;

    private Properties prop = null;

    /**
     * Private constructor to retrieve the properties for px like rss url and the url
     * to retrieve the information for experiments
     *
     * @throws IOException
     */
    private ReadProperties() throws IOException {
        InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream("px.properties");
        if (inputStream != null) {
            prop = new Properties();
            prop.load(inputStream);
        }

    }

    /**
     * Return a reader of all properties
     *
     * @return
     */
    public static ReadProperties getInstance() throws IOException {
        if (instance == null) {
            instance = new ReadProperties();
        }
        return instance;
    }

    /**
     * Retrieve the information for an specific key in the properties
     *
     * @param key Key of the feature
     * @return return the value of the property
     */
    public String getProperty(String key) {
        return prop.getProperty(key);
    }


}
