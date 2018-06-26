package uk.ac.ebi.ddi.api.readers.utils;

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
 * Created by ypriverol (ypriverol@gmail.com) on 20/01/2017.
 */
public class Constants {

    public static final String MASSIVE_INFO_SEPARATOR        = " \\| ";

    public static final String MASSIVE                   = "MassIVE";  //Massive Database Name
    public static final String GNPS                      = "GNPS";     // GNPS Database Name
    public static final String EMPTY_STRING              = "";
    public static final String MASSIVE_DESCRIPTION       = "Massive is a community resource developed by the NIH-funded Center for Computational Mass Spectrometry to promote the global, free exchange of mass spectrometry data.";
    public static final String GNPS_DESCRIPTION          = "The Global Natural Products Social Molecular Networking (GNPS) is a platform for providing an overview of the molecular features in mass spectrometry based metabolomics by comparing fragmentation patterns to identify chemical relationships.";
    public static final String MASSIVE_URL               = "https://massive.ucsd.edu/ProteoSAFe/dataset.jsp?task=";
    public static final String MASSIVE_STRING_NULL       = "null";

    public static final String METABOLOMICS_WORKBENCH    = "MetabolomicsWorkbench";
    public static final String METABOLOMEWORKBENCH_LINK           = "http://www.metabolomicsworkbench.org/data/DRCCMetadata.php?Mode=Study&StudyID=";
    public static final String METABOLOMICS_WORKBENCH_DESCRIPTION = "Metabolome Workbechn is a scalable and extensible informatics infrastructure which will serve as a national metabolomics resource.";
    public static final String METABOLOMICS_WORKBENCH_DATA        = "http://www.metabolomicsworkbench.org/rest/study/study_id/%s/mwtab";

    public static final String PATH_DELIMITED              = "/";
    public static final String GPMDB_PREFIX_MODEL          = "GPM";
    public static final String GPMDB_FTP_ROOT_DIRECOTRY    = "gpmdb";
    public static final String GPMDB_MODEL_LINK            = "http://gpmdb.thegpm.org/~/dblist_gpmnum/gpmnum=";
    public static final String GPMDB                       = "GPMDB";
    public static final String GPMDB_CONTACT               = "rbeavis@thegpm.org";
    public static final String GPMDB_CONTACT_TEAM          = "Ronald C. Beavis";
    public static final String GPMDB_AFILLIATION           = "The Global Proteome Machine Organization";
    public static final String LINCS = "LINCS";
    public static final String LINCS_DESCRIPTION = "The BD2K-LINCS Data Coordination and Integration Center is part of the Big Data to Knowledge (BD2K) NIH initiative, and it is the data coordination center for the NIH Common Fund's Library of Integrated Network-based Cellular Signatures (LINCS) program, which aims to characterize how a " +
            "variety of human cells, tissues and the entire organism respond to perturbations by drugs and other molecular factors.";
    public static final String PAXDB_DESCRIPTION           = "PaxDB is a comprehensive absolute protein abundance database, which contains whole genome protein abundance information across organisms and tissues. In PaxDb, the publicly available experimental data are imported and mapped onto a common namespace and," +
            " in the case of tandem mass spectrometry data, re-processed using our in-house standardized spectral counting pipeline. ";

    public static final String PAXDB = "PaxDB";
    public static final String PAXDB_RELEASE_DATE = "12/03/2015";
    public static final String PAXDB_DATA_PROTOCOL = "For the rescaling, the\n" +
            "datasets are first parsed or processed such that the data reflect\n" +
            "proportional abundances of whole protein molecules\n" +
            "(i.e. proportionality to counts of complete, individual protein\n" +
            "molecules, not to molecular weights, protein volumes, or digested\n" +
            "peptides). In the case of spectral counting data protein. The proportional abundances are rescaled linearly to add up\n" +
            "to one million; this means the abundance of each protein of\n" +
            "interest is finally expressed in (parts per million,) relative to\n" +
            "all other proteins in a sample. \n" +
            "For a given protein abundance dataset, we then compute\n" +
            "the absolute log abundance ratios of all pairs of proteins\n" +
            "annotated to be functionally linked. The median of these absolute\n" +
            "log abundance ratios represents an indirect quality\n" +
            "metric: the closer it is to zero, the better (i.e. the more there\n" +
            "is consistency between abundance values and functional annotations\n" +
            "such as protein complexes or pathways). We then\n" +
            "compute a background expectation for this metric, by permuting\n" +
            "the abundance values in a given dataset randomly,\n" +
            "and recomputing the median log abundance ratios. The permutation\n" +
            "is repeated several times, yielding a distribution of\n" +
            "medians. The actually observed median is then expressed as a\n" +
            "Z-score distance to the random distribution ofmedians—this\n" +
            "distance is termed the interaction consistency score.";


    public static String[] GPMDB_TAGS                      = {"ReAnalysis","Validation"};
    public static final String GPMDB_DESCRIPTION           = "The Global Proteome Machine Database was constructed to utilize the information obtained by GPM servers to aid in the difficult process of validating peptide MS/MS spectra as well as protein coverage patterns.";
    public static final String GPMDB_UKNOKNOWN_FILTER      = "none";
    public static final String FTP_PROTOCOL                = "ftp://";
    public static final String HTTP_PROTOCOL               = "http://";
    public static final String HTTPS_PROTOCOL              = "https://";

    public static final String PAXDB_URL                   = "http://pax-db.org/dataset/";
    public static final String PAXDB_DATASET_FILE          = "http://pax-db.org/downloads/latest/datasets/bioprojects-abundance-files-v4.0.zip";
    public static final String PAXDB_PUBMED                = "22535208";
    public static final String PAXDB_CONTACT               = "mering@imls.uzh.ch";
    public static final String PAXDB_CONTACT_TEAM          = "Christian von Mering";
    public static final String PAXDB_AFILLIATION           = "University of Zurich";

    public static final String GEO_DATABASE_DATASETS       = "GEO Datasets";
    public static final String GEO_POSFIX_DATABASE                = ".soft.gz";
    public static final String GEO_DATASETS_FTP_ROOT_DIRECOTRY    = "/geo/datasets/";
    public static final String GEO_DATASET_LINK                   = "https://www.ncbi.nlm.nih.gov/sites/GDSbrowser?acc=";
    public static final String GEO_DESCRIPTION                    = "This database stores curated gene expression DataSets, as well as original Series and Platform records in the Gene Expression Omnibus (GEO) repository." +
            " Enter search terms to locate experiments of interest. DataSet records contain additional resources including cluster tools and differential expression queries.";

    public static final String PX_DESCRIPTION = "The ProteomeXchange Consortium has been set up to provide a globally coordinated submission of mass spectrometry proteomics" +
            " data to the main existing proteomics repositories, and to encourage optimal data dissemination.";
    public static final String PX_DATABASE  = "ProteomeXChange";

    public static final String GEO = "GEO";

    public static final String GENOMICS_TYPE = "Genomics";

}
