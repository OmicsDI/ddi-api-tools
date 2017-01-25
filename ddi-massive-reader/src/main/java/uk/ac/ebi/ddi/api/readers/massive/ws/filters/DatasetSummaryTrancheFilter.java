package uk.ac.ebi.ddi.api.readers.massive.ws.filters;

import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetSummary;
import uk.ac.ebi.ddi.api.readers.model.IFilter;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 10/11/15
 */
public class DatasetSummaryTrancheFilter<T> implements IFilter<T> {


    @Override
    public boolean valid(Object object) {
        MassiveDatasetSummary dataSetSummary = (MassiveDatasetSummary) object;
        return !dataSetSummary.getTask().toUpperCase().contains("TRANCHE") || (dataSetSummary.getTitle() != null && !dataSetSummary.getTitle().toUpperCase().contains("TITLE HIDDEN") && dataSetSummary.getInstrument() != null && !dataSetSummary.getInstrument().isEmpty());
    }
}
