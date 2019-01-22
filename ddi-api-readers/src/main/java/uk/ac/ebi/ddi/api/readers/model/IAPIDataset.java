package uk.ac.ebi.ddi.api.readers.model;

import java.util.Map;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 29/04/2016
 */
public interface IAPIDataset {

    /**
     * Get dataset identifier or ID unique within the original resource
     *
     * @return
     */
    String getIdentifier();

    /**
     * Get Dataset title or name for the dataset.
     *
     * @return Name or Title for the dataset.
     */
    String getName();

    /**
     * Get the description or abstract of a dataset
     *
     * @return Description or abstract for a dataset
     */
    String getDescription();

    /**
     * Get the Data Protocol information for each dataset
     *
     * @return Data Protocol for Dataset
     */
    String getDataProtocol();


    /**
     * Get the publication date for the dataset
     *
     * @return Publication date should be provided
     */
    String getPublicationDate();

    /**
     * Get all the Dates additionals for dates.
     * Publication date can should be provided as mandatory
     *
     * @return Map of the corresponding dates
     */
    Map<String, String> getOtherDates();

    /**
     * Sample Protocol should be provided for must of the datasets
     * it is optional by it is recommended.
     *
     * @return Sample Protocol
     */
    String getSampleProcotol();

    /**
     * Get the list of omicsType for one dataset. It can be one or more.
     *
     * @return List of Omics Type
     */
    Set<String> getOmicsType();

    /**
     * Get the repository name. One provider can contains data from more than one
     * resource. This field is use to trace the specific database.
     *
     * @return Repository that contains the data set.
     */
    String getRepository();

    /**
     * Get the full Link of the dataset
     *
     * @return Full Link of the dataset
     */
    String getFullLink();

    /**
     * Get the List of the Instruments that has been use to measure
     * the experiments.
     *
     * @return Instruments
     */
    Set<String> getInstruments();

    /**
     * Get the Free Text Species for the Dataset.
     *
     * @return Ge the Species of one dataset.
     */
    Set<String> getSpecies();

    /**
     * Get the Free Text of CellTypes for a Dataset.
     *
     * @return Cell Type
     */
    Set<String> getCellTypes();

    /**
     * Get the Free Text of Diseases
     *
     * @return List of Diseases
     */
    Set<String> getDiseases();

    /**
     * Get the corresponding tissues for a dataset
     *
     * @return List of Tissues
     */
    Set<String> getTissues();

    /**
     * Get the corresponding software List for a dataset
     *
     * @return List of Software
     */
    Set<String> getSoftwares();

    /**
     * Get the Submitters names
     *
     * @return Submitter names
     */
    Set<String> getSubmitter();

    /**
     * Get Submitter emails
     *
     * @return submitter emails
     */
    Set<String> getSubmitterEmails();

    /**
     * Get Submitters Affiliation
     *
     * @return submitter affiliation list
     */
    Set<String> getSubmitterAffiliations();

    /**
     * Get the Submitter Keywords of a Dataset
     *
     * @return Submitter Keywords.
     */
    Set<String> getSubmitterKeywords();

    /**
     * Get the Lab Head of a dataset.
     *
     * @return lab head
     */
    Set<String> getLabHead();

    /**
     * Get Dataset Lab Head email
     *
     * @return email
     */
    Set<String> getLabHeadMail();

    /**
     * Get Dataset Lab Head Affiliation
     *
     * @return Lab Head or PI
     */
    Set<String> getLabHeadAffiliation();

    /**
     * Get Dataset Files.
     * @return  List of Files
     */
    Set<String> getDatasetFiles();

    /**
     * Get Cross References for each Dataset
     * @return Cross References
     */
    Map<String, Set<String>> getCrossReferences();

    /**
     * Get all the other additional fields
     * @return Additional fields
     */
    Map<String, Set<String>> getOtherAdditionals();

}
