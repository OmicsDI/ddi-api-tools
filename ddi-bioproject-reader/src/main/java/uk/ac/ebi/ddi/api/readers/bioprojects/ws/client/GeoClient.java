package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;

import java.io.File;
import java.net.URL;
import java.util.Collections;

/**
 * Created by azorin on 28/11/2017.
 */
public class GeoClient {
    private String filePath;
    private static final int RETRIES = 10;
    private RetryTemplate template = new RetryTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(GeoClient.class);
    private static final String NCBI_ENDPOINT = "https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi";

    public GeoClient(String filePath) {
        this.filePath = filePath;
        SimpleRetryPolicy policy =
                new SimpleRetryPolicy(RETRIES, Collections.singletonMap(Exception.class, true));
        template.setRetryPolicy(policy);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(1.6);
        template.setBackOffPolicy(backOffPolicy);
    }

    /**
     * public GeoDataset getOne(){
     * return new GeoDataset
     * }
     **/

    private File getSoftFile(String id) throws Exception {
        File f = new File(filePath + "/" + id + ".soft");
        if (!f.exists()) {
            try {
                template.execute(context -> {
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NCBI_ENDPOINT)
                            .queryParam("acc", id)
                            .queryParam("targ", "self")
                            .queryParam("form", "text")
                            .queryParam("view", "full");
                    URL url = builder.build().toUri().toURL();
                    FileUtils.copyURLToFile(url, f);
                    return f;
                });
            } catch (Exception e) {
                LOGGER.error("Exception occurred when trying to fetch file {}, ", id, e);
                throw e;
            }
        }
        return f;
    }

    public SeriesFile getSeries(String id) throws Exception {
        File f = getSoftFile(id);
        if (f.exists()) {
            return new SeriesFile(f);
        }
        return null;
    }

    public PlatformFile getPlatform(String id) throws Exception {
        File f = getSoftFile(id);
        if (f.exists()) {
            return new PlatformFile(f);
        }
        return null;
    }

    public SampleFile getSample(String id) throws Exception {
        File f = getSoftFile(id);
        if (f.exists()) {
            return new SampleFile(f);
        }
        return null;
    }
}
