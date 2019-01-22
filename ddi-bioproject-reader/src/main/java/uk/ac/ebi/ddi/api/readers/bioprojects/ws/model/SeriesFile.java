package uk.ac.ebi.ddi.api.readers.bioprojects.ws.model;

import java.io.File;
import java.util.List;

/**
 * Created by azorin on 28/11/2017.
 */


public class SeriesFile extends SoftFile {
    static final String SERIES_TYPE = "SERIES";

    public SeriesFile(File file) throws Exception {
        super(file);

        if (!this.Type.equals(SERIES_TYPE)) {
            throw new Exception("expected SERIES, received " + this.Type);
        }
    }

    public List<String> getSampleIds() {
        return this.Attributes.get(SeriesAttribute.Series_sample_id.getName());
    }

    public String getOrganism() {
        return this.getFirstAttribute(SeriesAttribute.Series_sample_organism.getName());
    }

    public List<String> getSeriesSuplimentraryFile() {
        return this.Attributes.get(SeriesAttribute.Series_supplementary_file.getName());
    }

    public List<String> getSeriesContactName() {
        return this.Attributes.get(SeriesAttribute.Series_contact_name.getName());
    }

    public List<String> getSeriesContactEmail() {
        return this.Attributes.get(SeriesAttribute.Series_contact_email.getName());
    }

    public List<String> getSeriesContactInstitute() {
        return this.Attributes.get(SeriesAttribute.Series_contact_institute.getName());
    }

    public String getPlatformId() {
        List<String> keys = this.Attributes.get(SeriesAttribute.Series_platform_id.getName());
        return keys == null || keys.isEmpty() ? null : keys.get(0);
    }

    public String getPubmedId() {
        List<String> keys = this.Attributes.get(SeriesAttribute.Series_pubmed_id.getName());
        return keys == null || keys.isEmpty() ? null : keys.get(0);
    }

    public String getStatus() {
        List<String> keys = this.Attributes.get(SeriesAttribute.Series_status.getName());
        return keys == null || keys.isEmpty() ? null : keys.get(0);
    }

    public String getSubmissionDate() {
        List<String> keys = this.Attributes.get(SeriesAttribute.Series_submission_date.getName());
        return keys == null || keys.isEmpty() ? null : keys.get(0);
    }
}
