package uk.ac.ebi.ddi.api.readers.massive.ws.filters;

import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetSummary;
import uk.ac.ebi.ddi.api.readers.model.IFilter;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 09/11/15
 */
public class DatasetSummaryNumberFiles<T> implements IFilter<T> {

    int numberFile = 0;

    @Override
    public boolean valid(Object object) {
        return ((MassiveDatasetSummary) object).getFileCount() > numberFile;
    }
}
