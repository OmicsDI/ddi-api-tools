package uk.ac.ebi.ddi.api.readers.px.xml.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.px.xml.model.ModelConstants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Yasset Perez-Riverol
 */
public class PXUnmarshallerFactory {

    private static final Logger logger = LoggerFactory.getLogger(PXUnmarshallerFactory.class);

    private static PXUnmarshallerFactory instance = new PXUnmarshallerFactory();

    private static JAXBContext jc = null;

    private PXUnmarshallerFactory() {
    }

    public static PXUnmarshallerFactory getInstance() {
        return instance;
    }

    public Unmarshaller initializeUnmarshaller() {

        try {
            // Lazy caching of the JAXB Context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.MODEL_PKG);
            }

            //create unmarshaller
            Unmarshaller pum = jc.createUnmarshaller();
            logger.debug("Unmarshaller Initialized");

            return pum;

        } catch (JAXBException e) {
            logger.error("UnmarshallerFactory.initializeUnmarshaller", e);
            throw new IllegalStateException("Could not initialize unmarshaller", e);
        }
    }
}
