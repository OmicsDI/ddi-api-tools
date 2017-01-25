package uk.ac.ebi.ddi.api.readers.massive.ws.filters;

import uk.ac.ebi.ddi.api.readers.model.IFilter;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetSummary;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 09/11/15
 */
public class DatasetSummaryUserFilter<T> implements IFilter<T> {

    String user = null;

    public DatasetSummaryUserFilter(String user) {
        this.user = user;
    }

    @Override
    public boolean valid(Object object) {
        MassiveDatasetSummary dataset = (MassiveDatasetSummary) object;
        return (dataset.getUser() != null && dataset.getUser().toLowerCase().equalsIgnoreCase(user));
    }
}
