package uk.ac.ebi.ddi.api.readers.massive.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.massive.ws.utils.Utilities;
import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 06/11/15
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MassiveDatasetDetail extends AbstractDataset implements IAPIDataset{

    private static final Logger logger = LoggerFactory.getLogger(MassiveDatasetDetail.class);

    @JsonProperty("description")
    String description;

    @JsonProperty("dataset_id")
    String id;

    @JsonProperty("publications")
    Publication[] publications;

    @JsonProperty("pxaccession")
    String pxAccession;

    @JsonProperty("convertedandcomputable")
    String converted;

    @JsonProperty("subscriptsions")
    String subscriptions;

    @JsonProperty("filesize")
    String fileSize;

    @JsonProperty("has_access")
    String hasAccess;

    @JsonProperty("filecount")
    String fileCount;

    @JsonProperty("keywords")
    String keywords;


    @JsonProperty("ftp")
    String ftp;

    @JsonProperty("modifications")
    String modification;

    private List<String> datasetPaths;


    @Override
    public String getDescription() {
        if(description != null && !description.equalsIgnoreCase(Constants.MASSIVE_STRING_NULL))
            return description;
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getName() {
        return getTitle();
    }

    @Override
    public String getDataProtocol() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getPublicationDate() {
        if(created != null && !created.isEmpty()){
            DateFormat df1 = new SimpleDateFormat("MMM. d, yyyy, h:mm a");
            try {
                return df1.parse(created).toString();
            } catch (ParseException e) {
                logger.debug(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public String getSampleProcotol() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public Set<String> getOmicsType() {
        Set<String> omics = new HashSet<>();
        if(getRepository().equalsIgnoreCase(Constants.GNPS)){
            omics.add(OmicsType.METABOLOMICS.getName());
        }else if(getRepository().equalsIgnoreCase(Constants.MASSIVE)){
            omics.add(OmicsType.PROTEOMICS.getName());
        }
        return omics;
    }

    @Override
    public String getRepository() {
        if(getName()!= null && getName().length() > 0)
            if(getName().contains(Constants.GNPS))
                return Constants.GNPS;
        return Constants.MASSIVE;
    }

    @Override
    public String getFullLink() {
        return Constants.MASSIVE_URL + task;
    }

    @Override
    public Set<String> getInstruments() {
        return Utilities.trimProperty(getInstrument());
    }

    @Override
    public Set<String> getSpecies()  {
        return Utilities.trimPropertyFromBytes(getStringSpecies());
    }

    @Override
    public Set<String> getCellTypes() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getDiseases() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getTissues() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getSoftwares() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getSubmitter() {
        return getLabHead();
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return getLabHeadMail();
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        return getLabHeadAffiliation();
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return Utilities.trimProperty(keywords);
    }

    @Override
    public Set<String> getLabHead() {
        Set<String> labNames = new HashSet<>();
        if(principalInvestigator != null && principalInvestigator.length > 0){
            Arrays.asList(principalInvestigator).forEach(s -> {
                if(s != null && s.getName() != null && s.getName().length() > 0)
                    labNames.add(s.getName().trim());
            });
        }
        return labNames;
    }

    @Override
    public Set<String> getLabHeadMail() {
        Set<String> labEmails = new HashSet<>();
        if(principalInvestigator != null && principalInvestigator.length > 0){
            Arrays.asList(principalInvestigator).forEach(s -> {
                if(s != null && s.getEmail() != null && s.getEmail().length() > 0)
                    labEmails.add(s.getEmail().trim());
            });
        }
        return labEmails;
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        Set<String> labAffiliations = new HashSet<>();
        if(principalInvestigator != null && principalInvestigator.length > 0){
            Arrays.asList(principalInvestigator).forEach(s -> {
                if(s != null && s.getInstitution() != null && s.getInstitution().length() > 0)
                    labAffiliations.add(s.getInstitution().trim());
            });
        }
        return labAffiliations;
    }

    @Override
    public Set<String> getDatasetFiles() {
        Set<String> datasetFiles = new HashSet<>();
        if(ftp != null && ftp.length() > 0)
            datasetFiles.add(ftp.trim());
        if(datasetPaths != null && datasetPaths.size() > 0) {
            datasetPaths.forEach(s -> datasetFiles.add(s.trim()));
            return datasetFiles;
        }
        return datasetFiles;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        Map<String, Set<String>> crossReferences = new HashMap<>();
        if (publications != null && publications.length > 0) {
            Arrays.asList(publications).stream().filter(reference -> reference.getPubmedId() != null).forEach(reference -> {
                Set<String> values = new HashSet<>();
                if(crossReferences.containsKey(Field.PUBMED.getName()))
                    values = crossReferences.get(Field.PUBMED.getName());
                values.add(reference.getPubmedId().toString());
                crossReferences.put(Field.PUBMED.getName(), values);
            });
        }
        return crossReferences;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        Map<String, Set<String>> additionals = new HashMap<>();
        if(modification != null && modification.length() > 0){
            Set<String> modifications = new HashSet<>();
            if(modification != null && !modification.isEmpty()) {
                String[] modArray = modification.split(Constants.MASSIVE_INFO_SEPARATOR);
                Arrays.asList(modArray).forEach(s -> modifications.add(s.trim()));
                additionals.put(Field.PTM_MODIFICATIONS.getName(), modifications);
            }
        }

        if(pxAccession != null && pxAccession.length() > 0 && !pxAccession.equalsIgnoreCase(Constants.MASSIVE_STRING_NULL)){
            Set<String> accessions = new HashSet<>();
            accessions.add(pxAccession);
            additionals.put(Field.SECONDARY_ACCESSION.getName(), accessions);
        }

        if(fileSize != null && fileSize.length() > 0){
            Set<String> fileSizes = new HashSet<>();
            fileSizes.add(fileSize);
            additionals.put(Field.FILE_SIZE.getName(), fileSizes);
        }

        if(fileCount != null && fileCount.length() > 0){
            Set<String> fileCounts = new HashSet<>();
            fileCounts.add(fileCount);
            additionals.put(Field.FILE_SIZE.getName(), fileCounts);
        }
        //Add only the publications without pubmed ID.
        if(publications != null && publications.length > 0 ){
            Set<String> citations = new HashSet<>();
            Arrays.asList(publications).forEach( s -> {
                if(s.getPubmedId() == null && s.pmcid == null){
                    String citation = (s.getTitle() != null)?s.getTitle():"";
                    citation = (s.getAuthors() != null)? citation + ". " + s.getAuthors():citation;
                    citation = (s.getCitation() != null)?citation + ". " + s.getCitation():citation;
                    citations.add(citation);

            }});
            additionals.put(Field.PUBLICATION.getName(), citations);
        }
        return additionals;
    }


    public void setDataFilePaths(List<String> dataFilePaths) {
        this.datasetPaths = dataFilePaths;
    }

    @Override
    public String toString() {
        return "MassiveDatasetDetail{" +
                "description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", publications=" + Arrays.toString(publications) +
                ", pxAccession='" + pxAccession + '\'' +
                ", converted='" + converted + '\'' +
                ", subscriptions='" + subscriptions + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", hasAccess='" + hasAccess + '\'' +
                ", fileCount='" + fileCount + '\'' +
                ", keywords='" + keywords + '\'' +
                ", ftp='" + ftp + '\'' +
                '}';
    }


}
