package uk.ac.ebi.ddi.api.readers.ena.ws.model;

import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.BiologicalDatabases;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class EnaDataset implements IAPIDataset {

    private HashSet<String> omicsType = new HashSet<String>();
    private String fullLink;
    private HashSet<String> instruments = new HashSet<String>();
    private HashSet<String> species = new HashSet<String>();
    private HashSet<String> cellTypes = new HashSet<String>();
    private HashSet<String> diseases = new HashSet<String>();
    private HashSet<String> tissues = new HashSet<String>();
    private HashSet<String> softwares = new HashSet<String>();
    private HashSet<String> submitter = new HashSet<String>();
    private HashSet<String> submitterEmail = new HashSet<String>();
    private HashSet<String> submitterAffiliations = new HashSet<String>();
    private HashSet<String> submitterKeywords = new HashSet<String>();
    private HashSet<String> labHead = new HashSet<String>();
    private HashSet<String> labHeadMail = new HashSet<String>();
    private HashSet<String> labHeadAffiliation = new HashSet<String>();
    private String identifier = null;
    private String repository = null;
    private String name;
    private String description;
    private String publicationDate;
    private HashSet<String> datasetFiles = new HashSet<String>();

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

    public void setDescription(String description) {
        this.description = description;
    }

    String dataProtocol;

    public void setDataProtocol(String value){
        this.dataProtocol = value;
    }

    @Override
    public String getDataProtocol(){
        return null;
    }

    @Override
    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return new HashMap<String, String>();
    }

    String sampleProtocol;

    public void setSampleProtocol(String value){
        this.sampleProtocol = value;
    }

    @Override
    public String getSampleProcotol() {
        return null;
    }

    public void addOmicsType(String omicsType) {
        this.omicsType.add(omicsType);
    }

    @Override
    public Set<String> getOmicsType() {
        if (omicsType.size() < 1) {
            omicsType.add("Unknown");
        }
        return omicsType;
    }

    @Override
    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    @Override
    public String getFullLink() {
        return fullLink;
    }

    public void setFullLink(String value) {
        this.fullLink = value;
    }

    public void addInstrument(String value) {
        this.instruments.add(value);
    }

    @Override
    public Set<String> getInstruments() {
        return instruments;
    }

    public void addSpecies(String specimen) {
        this.species.add(specimen);
    }

    @Override
    public Set<String> getSpecies() {
        return species;
    }

    public void addCellType(String value) {
        this.cellTypes.add(value);
    }

    @Override
    public Set<String> getCellTypes() {
        return cellTypes;
    }

    public void addDisease(String value) {
        this.diseases.add(value);
    }

    @Override
    public Set<String> getDiseases() {
        return diseases;
    }

    public void addTissue(String value) {
        this.tissues.add(value);
    }

    @Override
    public Set<String> getTissues() {
        return tissues;
    }

    public void addSoftware(String value) {
        this.softwares.add(value);
    }

    @Override
    public Set<String> getSoftwares() {
        return softwares;
    }

    public void addSubmitter(String value) {
        this.submitter.add(value);
    }

    @Override
    public Set<String> getSubmitter() {
        return submitter;
    }

    public void addSubmitterEmail(String value) {
        this.submitterEmail.add(value);
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return submitterEmail;
    }

    public void addSubmitterAffiliations(String value) {
        this.submitterAffiliations.add(value);
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        return submitterAffiliations;
    }

    public void addSubmitterKeyword(String value) {
        this.submitterKeywords.add(value);
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return submitterKeywords;
    }

    public void addLabHead(String value) {
        this.labHead.add(value);
    }

    @Override
    public Set<String> getLabHead() {
        return labHead;
    }

    public void addlabHeadMail(String value) {
        this.labHeadMail.add(value);
    }

    @Override
    public Set<String> getLabHeadMail() {
        return labHeadMail;
    }

    public void addLabHeadAffiliation(String value) {
        this.labHeadAffiliation.add(value);
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return labHeadAffiliation;
    }

    public void addDatasetFile(String file) {
        this.datasetFiles.add(file);
    }

    @Override
    public Set<String> getDatasetFiles() {
        return datasetFiles;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        return new HashMap<String, Set<String>>();
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        return new HashMap<String, Set<String>>();
    }
}
