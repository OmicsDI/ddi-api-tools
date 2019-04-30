package uk.ac.ebi.ddi.api.readers.utils;

import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;


/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * ==Overview==
 * <p>
 * This class helps to transforms Readers API into an Entry to be write in the
 * OmicsDI Files
 * <p>
 * Created by ypriverol (ypriverol@gmail.com) on 21/01/2017.
 */
public class Transformers {

    public static Entry transformAPIDatasetToEntry(IAPIDataset dataset) {

        Entry entry = new Entry();

        entry.setId(dataset.getIdentifier());
        entry.setName(dataset.getName());
        entry.setDescription(dataset.getDescription());

        if (null != dataset.getPublicationDate()) {
            entry.addDate(DSField.Date.PUBLICATION.getName(), dataset.getPublicationDate());
        }

        entry.addAdditionalField(DSField.Additional.REPOSITORY.getName(), dataset.getRepository());
        entry.addAdditionalField(DSField.Additional.SAMPLE.getName(), dataset.getSampleProcotol());
        entry.addAdditionalField(DSField.Additional.DATA.getName(), dataset.getDataProtocol());
        entry.addAdditionalField(DSField.Additional.LINK.getName(), dataset.getFullLink());

        dataset.getOmicsType().forEach(s -> entry.addAdditionalField(DSField.Additional.OMICS.getName(), s));
        dataset.getCellTypes().forEach(s -> entry.addAdditionalField(DSField.Additional.CELL_TYPE_FIELD.getName(), s));
        dataset.getDiseases().forEach(s -> entry.addAdditionalField(DSField.Additional.DISEASE_FIELD.getName(), s));
        dataset.getSpecies().forEach(s -> entry.addAdditionalField(DSField.Additional.SPECIE_FIELD.getName(), s));
        dataset.getCrossReferences().forEach(entry::addCrossReferenceValue);
        dataset.getInstruments().forEach(s -> entry.addAdditionalField(DSField.Additional.INSTRUMENT.getName(), s));
        dataset.getSoftwares().forEach(s -> entry.addAdditionalField(DSField.Additional.SOFTWARE_INFO.getName(), s));
        dataset.getOtherDates().forEach(entry::addDate);
        dataset.getCrossReferences().forEach(entry::addCrossReferenceValue);
        dataset.getOtherAdditionals().forEach((k, s) -> s.forEach(sub -> entry.addAdditionalField(k, sub)));

        dataset.getDatasetFiles().forEach(s -> entry.addAdditionalField(DSField.Additional.DATASET_FILE.getName(), s));
        dataset.getTissues().forEach(s -> entry.addAdditionalField(DSField.Additional.TISSUE_FIELD.getName(), s));
        dataset.getSubmitter().forEach(s -> entry.addAdditionalField(DSField.Additional.SUBMITTER.getName(), s));
        dataset.getSubmitterEmails().forEach(
                s -> entry.addAdditionalField(DSField.Additional.SUBMITTER_EMAIL.getName(), s));
        dataset.getSubmitterAffiliations().forEach(
                s -> entry.addAdditionalField(DSField.Additional.SUBMITTER_AFFILIATION.getName(), s));

        return entry;

    }
}
