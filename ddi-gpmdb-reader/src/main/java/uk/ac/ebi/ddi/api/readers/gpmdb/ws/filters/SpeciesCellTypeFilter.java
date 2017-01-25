package uk.ac.ebi.ddi.api.readers.gpmdb.ws.filters;

import uk.ac.ebi.ddi.api.readers.gpmdb.ws.model.Model;
import uk.ac.ebi.ddi.api.readers.model.IFilter;

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
 * Created by ypriverol (ypriverol@gmail.com) on 25/01/2017.
 */
public class SpeciesCellTypeFilter<T> implements IFilter<T>{

    public SpeciesCellTypeFilter() {
    }

    @Override
    public boolean valid(T type) {
        Model model = (Model) type;
        if(model != null && (model.getTissues().size() > 0 || model.getCellTypes().size() > 0))
            return true;
        return false;
    }
}
