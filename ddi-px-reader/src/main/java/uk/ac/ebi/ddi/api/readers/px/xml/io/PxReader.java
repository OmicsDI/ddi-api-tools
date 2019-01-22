package uk.ac.ebi.ddi.api.readers.px.xml.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.px.utils.Constants;
import uk.ac.ebi.ddi.api.readers.px.xml.model.*;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;


/**
 * @author ypriverol
 */
public class PxReader implements IAPIDataset {

    private static final Logger LOGGER = LoggerFactory.getLogger(PxReader.class);

    private Unmarshaller unmarshaller = null;

    private JAXBElement<ProteomeXchangeDatasetType> dataset = null;

    private String accession;

    /**
     * @param xml
     * @throws javax.xml.bind.JAXBException
     */
    public PxReader(InputStream xml) throws JAXBException {
        if (xml == null) {
            throw new IllegalArgumentException("Xml file to be indexed must not be null");
        }
        // create unmarshaller
        this.unmarshaller = PXUnmarshallerFactory.getInstance().initializeUnmarshaller();

        dataset = (JAXBElement<ProteomeXchangeDatasetType>) unmarshaller.unmarshal(xml);

    }

    @Override
    public String getIdentifier() {
        return accession;
    }

    @Override
    public String getName() {
        return dataset.getValue().getDatasetSummary().getTitle();
    }

    @Override
    public String getDescription() {
        return dataset.getValue().getDatasetSummary().getDescription();
    }

    @Override
    public String getDataProtocol() {
        return uk.ac.ebi.ddi.api.readers.utils.Constants.EMPTY_STRING;
    }

    @Override
    public String getPublicationDate() {
        if (dataset.getValue().getDatasetSummary() != null
                && dataset.getValue().getDatasetSummary().getAnnounceDate() != null) {
            return dataset.getValue().getDatasetSummary().getAnnounceDate().toGregorianCalendar().getTime().toString();
        }
        return uk.ac.ebi.ddi.api.readers.utils.Constants.EMPTY_STRING;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return Collections.emptyMap();
    }

    @Override
    public String getSampleProcotol() {
        return uk.ac.ebi.ddi.api.readers.utils.Constants.EMPTY_STRING;
    }

    @Override
    public Set<String> getOmicsType() {
        Set<String> omicsType = new HashSet<>();
        omicsType.add(OmicsType.PROTEOMICS.getName());
        return omicsType;
    }

    @Override
    public String getRepository() {
        if (dataset.getValue() != null
                && dataset.getValue().getDatasetSummary() != null
                && dataset.getValue().getDatasetSummary().getHostingRepository() != null
                && dataset.getValue().getDatasetSummary().getHostingRepository().value() != null) {
            return dataset.getValue().getDatasetSummary().getHostingRepository().value();
        }
        return uk.ac.ebi.ddi.api.readers.utils.Constants.EMPTY_STRING;
    }

    @Override
    public String getFullLink() {
        if (dataset != null && dataset.getValue() != null &&
                dataset.getValue().getFullDatasetLinkList() != null &&
                dataset.getValue().getFullDatasetLinkList().getFullDatasetLink() != null) {
            for (FullDatasetLinkType datasetLink : dataset.getValue().getFullDatasetLinkList().getFullDatasetLink()) {
                if (datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.MASSIVEURL_ACCESSION) ||
                        datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.PRIDE_LINK) ||
                        datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.PASSELURL_ACCESSION) ||
                        datasetLink.getCvParam().getAccession().equalsIgnoreCase(Constants.JPOSTURL_ACCESSION)) {
                    return datasetLink.getCvParam().getValue();
                }
            }
        }
        return uk.ac.ebi.ddi.api.readers.utils.Constants.EMPTY_STRING;
    }

    /**
     * Return the List of instruments related with the dataset
     *
     * @return List<InstrumentType>
     */
    @Override
    public Set<String> getInstruments() {
        Set<String> instruments = new HashSet<>();
        if (dataset != null && dataset.getValue() != null && dataset.getValue().getInstrumentList() != null
                && dataset.getValue().getInstrumentList().getInstrument() != null) {
            for (InstrumentType instrument : dataset.getValue().getInstrumentList().getInstrument()) {
                if (instrument != null && instrument.getCvParam() != null) {
                    for (CvParamType cv : instrument.getCvParam()) {
                        instruments.add(cv.getValue());
                    }
                }
            }

        }
        return instruments;
    }

    /**
     * Get the List of Species including taxonomies and name of species
     *
     * @return
     */
    @Override
    public Set<String> getSpecies() {
        Set<String> speciesResult = new HashSet<>();
        if (dataset != null && dataset.getValue() != null && dataset.getValue().getSpeciesList() != null) {
            for (SpeciesType s : dataset.getValue().getSpeciesList().getSpecies()) {
                for (CvParamType cv : s.getCvParam()) {
                    if (!cv.getAccession().equalsIgnoreCase(Constants.TAXONOMY_ACCESSION)) {
                        speciesResult.add(cv.getValue());
                    }
                }
            }
        }
        return speciesResult;
    }

    @Override
    public Set<String> getCellTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getDiseases() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getTissues() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSoftwares() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSubmitter() {
        return getCVProperty(Constants.SUBMITTER_ACCESSION, Constants.CONTACT_NAME);
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return getCVProperty(Constants.SUBMITTER_ACCESSION, Constants.CONTACT_EMAIL);
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        return getCVProperty(Constants.SUBMITTER_ACCESSION, Constants.CONTACT_ORG);
    }


    /**
     * Get PX keywords
     *
     * @return List<CvParamType>
     */
    @Override
    public Set<String> getSubmitterKeywords() {
        Set<String> keywords = new HashSet<>();
        if (dataset.getValue().getKeywordList() != null
                && dataset.getValue().getKeywordList().getCvParam() != null
                && dataset.getValue().getKeywordList().getCvParam().size() > 0) {
            for (CvParamType cv : dataset.getValue().getKeywordList().getCvParam()) {
                if (cv.getAccession().equalsIgnoreCase(Constants.SUBMITTERKEY_ACCESSION)
                        || cv.getAccession().equalsIgnoreCase(Constants.CURATORKEY_ACCESSION)) {
                    Collections.addAll(keywords, cv.getValue().split(","));
                }
            }
        }
        return keywords;
    }

    @Override
    public Set<String> getLabHead() {
        return getCVProperty(Constants.LABHEAD_ACCESSION, Constants.CONTACT_NAME);
    }

    @Override
    public Set<String> getLabHeadMail() {
        return getCVProperty(Constants.LABHEAD_ACCESSION, Constants.CONTACT_EMAIL);
    }

    private Set<String> getCVProperty(String typeContact, String property) {
        Set<String> properties = new HashSet<>();
        if (dataset != null && dataset.getValue().getContactList() != null
                && dataset.getValue().getContactList().getContact() != null) {
            String name = null;
            boolean isContact = false;
            for (ContactType contactType : dataset.getValue().getContactList().getContact()) {
                for (CvParamType cv : contactType.getCvParam()) {
                    if (cv.getAccession().equalsIgnoreCase(property)) {
                        name = cv.getValue();
                    }
                    if (cv.getAccession().equalsIgnoreCase(typeContact)) {
                        isContact = true;
                    }
                }
            }
            if (isContact && name != null) {
                properties.add(name);
            }
        }
        return properties;
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return getCVProperty(Constants.LABHEAD_ACCESSION, Constants.CONTACT_ORG);
    }

    @Override
    public Set<String> getDatasetFiles() {
        Set<String> files = new HashSet<>();
        if (dataset != null && dataset.getValue() != null && dataset.getValue().getDatasetFileList() != null) {
            for (DatasetFileType cvList : dataset.getValue().getDatasetFileList().getDatasetFile()) {
                if (cvList != null && cvList.getCvParam() != null) {
                    for (CvParamType cv : cvList.getCvParam()) {
                        if (cv != null && cv.getAccession() != null && cv.getValue() != null) {
                            if (cv.getAccession().equalsIgnoreCase(Constants.FILE_TYPE_ACCESSION)) {
                                files.add(cv.getValue());
                            }
                        }
                    }
                }
            }
        }
        return files;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        Map<String, Set<String>> crossReferences = new HashMap<>();

        if (dataset != null && dataset.getValue() != null && dataset.getValue().getSpeciesList() != null) {
            Set<String> speciesResult = new HashSet<>();
            for (SpeciesType s : dataset.getValue().getSpeciesList().getSpecies()) {
                for (CvParamType cv : s.getCvParam()) {
                    if (!cv.getAccession().equalsIgnoreCase(Constants.SPECIES_ACCESSION)) {
                        speciesResult.add(cv.getValue());
                    }
                }
            }
            crossReferences.put(Field.TAXONOMY.getName(), speciesResult);
        }

        if (dataset != null && dataset.getValue() != null && dataset.getValue().getPublicationList() != null) {
            Set<String> publicationIds = new HashSet<>();
            for (PublicationType s : dataset.getValue().getPublicationList().getPublication()) {
                for (CvParamType cv : s.getCvParam()) {
                    if (cv.getAccession().equalsIgnoreCase(Constants.PUBMED_ACCESSION)) {
                        publicationIds.add(cv.getValue());
                    }
                }
            }
            crossReferences.put(Field.PUBMED.getName(), publicationIds);
        }

        return crossReferences;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        // Get the Modification information
        Map<String, Set<String>> additionals = new HashMap<>();
        if (dataset != null && dataset.getValue() != null && dataset.getValue().getModificationList() != null) {
            Set<String> publication = new HashSet<>();
            for (CvParamType mod : dataset.getValue().getModificationList().getCvParam()) {
                if (!mod.getAccession().equalsIgnoreCase(Constants.NO_MODIFICATIONS)) {
                    publication.add(mod.getValue());
                }
            }
            additionals.put(Field.PTM_MODIFICATIONS.getName(), publication);
        }


        if (dataset != null && dataset.getValue() != null && dataset.getValue().getKeywordList() != null) {
            Set<String> experimentTypes = new HashSet<>();
            for (CvParamType cvParamType : dataset.getValue().getKeywordList().getCvParam()) {
                if (cvParamType.getValue().contains(Constants.SRM_KEYWORD) || cvParamType.getName().contains("SRM")) {
                    experimentTypes.add("SRM/MRM");
                }
            }
            additionals.put(Field.TECHNOLOGY_TYPE.getName(), experimentTypes);
        }

        return additionals;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

}
