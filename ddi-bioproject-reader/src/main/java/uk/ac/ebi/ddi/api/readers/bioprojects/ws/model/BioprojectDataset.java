package uk.ac.ebi.ddi.api.readers.bioprojects.ws.model;

import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.BiologicalDatabases;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.util.Map;
import java.util.Set;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class BioprojectDataset implements IAPIDataset{

    private String identifier = null;

    private String name;

    private String description;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getDataProtocol() {
        return null;
    }

    @Override
    public String getPublicationDate() {
        return null;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return null;
    }

    @Override
    public String getSampleProcotol() {
        return null;
    }

    @Override
    public Set<String> getOmicsType() {
        return null;
    }

    @Override
    public String getRepository() {
        return null;
    }

    @Override
    public String getFullLink() {
        return null;
    }

    @Override
    public Set<String> getInstruments() {
        return null;
    }

    @Override
    public Set<String> getSpecies() {
        return null;
    }

    @Override
    public Set<String> getCellTypes() {
        return null;
    }

    @Override
    public Set<String> getDiseases() {
        return null;
    }

    @Override
    public Set<String> getTissues() {
        return null;
    }

    @Override
    public Set<String> getSoftwares() {
        return null;
    }

    @Override
    public Set<String> getSubmitter() {
        return null;
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return null;
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        return null;
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return null;
    }

    @Override
    public Set<String> getLabHead() {
        return null;
    }

    @Override
    public Set<String> getLabHeadMail() {
        return null;
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return null;
    }

    @Override
    public Set<String> getDatasetFiles() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        return null;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
