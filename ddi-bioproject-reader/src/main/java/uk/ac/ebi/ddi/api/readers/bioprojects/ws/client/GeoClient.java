package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by azorin on 28/11/2017.
 */
public class GeoClient {
    String filePath;

    public GeoClient(String filePath){
        this.filePath = filePath;
    }

    /** public GeoDataset getOne(){
        return new GeoDataset
    } **/

    private File getSoftFile(String id) throws Exception{
        File f = new File(filePath + "/" + id + ".soft");
        if(!f.exists()){
            URL website = new URL("https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc="+id+"&targ=self&form=text&view=full");
            try (InputStream in = website.openStream()) {
                Path targetPath = f.toPath();
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return f;
    }

    public SeriesFile getSeries(String id) throws Exception{
        File f = getSoftFile(id);
        if(f.exists()) {
            return new SeriesFile(f);
        }
        return null;
    }

    public PlatformFile getPlatform(String id) throws Exception{
        File f = getSoftFile(id);
        if(f.exists()) {
            return new PlatformFile(f);
        }
        return null;
    }

    public SampleFile getSample(String id) throws Exception{
        File f = getSoftFile(id);
        if(f.exists()) {
            return new SampleFile(f);
        }
        return null;
    }
}
