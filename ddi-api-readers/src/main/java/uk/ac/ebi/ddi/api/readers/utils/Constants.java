package uk.ac.ebi.ddi.api.readers.utils;

import java.util.HashSet;
import java.util.Set;

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

    public static String[] GPMDB_TAGS                      = {"ReAnalysis","Validation"};
    public static final String GPMDB_DESCRIPTION           = "The Global Proteome Machine Database was constructed to utilize the information obtained by GPM servers to aid in the difficult process of validating peptide MS/MS spectra as well as protein coverage patterns.";
    public static final String GPMDB_UKNOKNOWN_FILTER      = "none";
    public static final String FTP_PROTOCOL                = "ftp://";
    public static final String HTTP_PROTOCOL               = "http://";
    public static final String HTTPS_PROTOCOL              = "https://";
}
