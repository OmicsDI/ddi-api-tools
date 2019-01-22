package uk.ac.ebi.ddi.api.readers.massive.ws.filters;

import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetSummaryMassive;
import uk.ac.ebi.ddi.api.readers.model.IFilter;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 09/11/15
 */
public class DatasetSummarySizeFilter<T> implements IFilter<T> {

    private double dataSizeMb = 0;
//    Pattern regPattern = Pattern.compile("(-)?(([^\\d])(0)|[1-9][0-9]*)(.)([0-9]+)");

    public DatasetSummarySizeFilter(double dataSizeMb) {
        this.dataSizeMb = dataSizeMb;
    }

    @Override
    public boolean valid(Object object) {
        return ((MassiveDatasetSummaryMassive) object).getFileSize() / 1000 > dataSizeMb;
    }
}
