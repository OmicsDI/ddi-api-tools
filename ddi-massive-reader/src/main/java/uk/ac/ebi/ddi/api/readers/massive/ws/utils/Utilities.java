package uk.ac.ebi.ddi.api.readers.massive.ws.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * ==Overview==
 * <p>
 * This class
 * <p>
 * Created by ypriverol (ypriverol@gmail.com) on 22/01/2017.
 */
public class Utilities {

    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);


    public static Set<String> trimProperty(String property){
        Set<String> properties = new HashSet<>();
        if(property != null && property.length() > 0)
            Arrays.asList(property.split(Constants.MASSIVE_INFO_SEPARATOR)).forEach(s -> properties.add(s.trim()));
        return properties;
    }

    public static Set<String> trimPropertyFromBytes(String property){
        Set<String> properties = new HashSet<>();
        if(property != null && property.length() > 0)
            Arrays.asList(property.split(Constants.MASSIVE_INFO_SEPARATOR)).forEach((String s) -> {
                try {
                    properties.add(new String(s.getBytes(), "UTF-8").trim());
                } catch (UnsupportedEncodingException e) {
                    logger.debug(e.getMessage());
                }
            });
        return properties;
    }
}
