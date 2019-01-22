package uk.ac.ebi.ddi.api.readers.model;

/**
 * Created by azorin on 13/12/2017.
 *
 * parsed BioProjects XML file
 * https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=bioproject&id=PRJNA17297
 *
 */

public class NcbiDataset {
    public String database;

    public String id;
    public String title;
    public String description;
    public String publicationDate;

    public String omicsType;

    public String organismName;
}
