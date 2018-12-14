package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;

/**
 * Created by azorin on 28/11/2017.
 */
public class GeoClient {
    private String filePath;
    private static final int RETRIES = 5;
    private RetryTemplate template = new RetryTemplate();
    private static final String NCBI_ENDPOINT = "https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi";

    public GeoClient(String filePath){
        this.filePath = filePath;
        SimpleRetryPolicy policy =
                new SimpleRetryPolicy(RETRIES, Collections.singletonMap(Exception.class, true));
        template.setRetryPolicy(policy);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(1.6);
        template.setBackOffPolicy(backOffPolicy);
    }

    /** public GeoDataset getOne(){
        return new GeoDataset
    } **/

    private File getSoftFile(String id) throws Exception {
        File f = new File(filePath + "/" + id + ".soft");
        if(!f.exists()){
            template.execute(context -> {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NCBI_ENDPOINT)
                        .queryParam("acc", id)
                        .queryParam("targ", "self")
                        .queryParam("form", "text")
                        .queryParam("view", "full");
                try (BufferedInputStream inputStream = new BufferedInputStream(builder.build().toUri().toURL().openStream());
                     FileOutputStream fileOS = new FileOutputStream(f)) {
                    byte[] data = new byte[1024];
                    int byteContent;
                    while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                        fileOS.write(data, 0, byteContent);
                    }
                }
                return f;
            });
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
