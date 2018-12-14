package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.springframework.http.ResponseEntity;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Created by azorin on 28/11/2017.
 */
public class GeoClient {
    private String filePath;
    private static final int RETRIES = 5;
    private RestTemplate restTemplate = new RestTemplate();
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

    private String downloadSoftFile(String id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NCBI_ENDPOINT)
                .queryParam("acc", id)
                .queryParam("targ", "self")
                .queryParam("form", "text")
                .queryParam("view", "full");
        return template.execute(context -> {
            ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toString(), String.class);
            return response.getBody();
        });
    }

    private File getSoftFile(String id) throws Exception{
        File f = new File(filePath + "/" + id + ".soft");
        if(!f.exists()){
            String softFile = downloadSoftFile(id);
            try (PrintWriter out = new PrintWriter(new FileOutputStream(f, false))) {
                out.print(softFile);
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
