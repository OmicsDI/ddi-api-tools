package uk.ac.ebi.ddi.api.readers.px;

import org.junit.Test;
import uk.ac.ebi.ddi.api.readers.px.utils.ReaderPxXML;
import uk.ac.ebi.ddi.api.readers.px.xml.io.PxReader;

import static org.junit.Assert.*;

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
 * Created by ypriverol (ypriverol@gmail.com) on 14/03/2017.
 */
public class GeneratePxOmicsXMLTest {

    String url = "http://proteomecentral.proteomexchange.org/cgi/GetDataset?ID=PXD005952&outputMode=XML";

    @Test
    public void getPage() throws Exception {
        String page = GeneratePxOmicsXML.getPage(url);
        PxReader dataset = ReaderPxXML.parseDocument(page);
        System.out.println(dataset.getIdentifier());
    }

}