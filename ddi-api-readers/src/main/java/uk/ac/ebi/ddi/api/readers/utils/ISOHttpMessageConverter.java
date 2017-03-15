package uk.ac.ebi.ddi.api.readers.utils;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

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
 * Created by ypriverol (ypriverol@gmail.com) on 25/01/2017.
 */
public class ISOHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public ISOHttpMessageConverter() {
        List<MediaType> types = Collections.singletonList(
                new MediaType("text", "html", Charset.forName("ISO-8859-1"))
        );
        super.setSupportedMediaTypes(types);
    }
}
