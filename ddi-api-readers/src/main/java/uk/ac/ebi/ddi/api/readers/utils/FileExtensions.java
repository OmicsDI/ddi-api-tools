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
 * Created by ypriverol (ypriverol@gmail.com) on 24/01/2017.
 */
public enum FileExtensions {

    FTP_GPMDB_MODEL_EXTENSION("xml.gz", "GPMDB Model Gzip in FTP");

    private String extension;
    private String title;

    FileExtensions(String extension, String title) {
        this.extension = extension;
        this.title = title;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
