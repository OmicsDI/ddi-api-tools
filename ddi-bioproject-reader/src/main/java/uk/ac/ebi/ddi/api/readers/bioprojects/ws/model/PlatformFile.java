package uk.ac.ebi.ddi.api.readers.bioprojects.ws.model;

import java.io.File;

/**
 * Created by azorin on 04/12/2017.
 */
public class PlatformFile extends SoftFile{
    static final String PLATFORM_TYPE = "PLATFORM";

    public PlatformFile(File file) throws Exception{
        super(file);

        if(!this.Type.equals(PLATFORM_TYPE)){
            throw new Exception("expected PLATFORM, received " + this.Type);
        }
    }

    public String get_Title(){
        return this.getFirstAttribute(PlatformAttribute.Platform_title.getName());
    }
}
