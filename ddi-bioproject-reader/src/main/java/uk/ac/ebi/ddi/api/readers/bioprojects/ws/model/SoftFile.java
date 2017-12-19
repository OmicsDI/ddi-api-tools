package uk.ac.ebi.ddi.api.readers.bioprojects.ws.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by azorin on 28/11/2017.
 */
public class SoftFile {

    protected String Type; //SAMPLE,SERIES,..
    protected String Id;
    protected Map<String, List<String>> Attributes = new HashMap<String, List<String>>();

    public SoftFile(File file) throws IOException
    {
        BufferedReader reader=new BufferedReader(new FileReader(file));

        String line;
        while((line=reader.readLine())!=null)
        {
            if(line.startsWith("^"))
            {
                SoftFileEntry entry = parseLine(line);
                this.Type = entry.Key;
                this.Id = entry.Value;
            }
            else if(line.startsWith("!"))
            {
                SoftFileEntry entry = parseLine(line);

                List<String> values = this.Attributes.get(entry.Key);
                if(null==values){
                    values = new ArrayList<String>();
                    this.Attributes.put(entry.Key,values);
                }

                values.add(entry.Value);
            }
            else if(line.startsWith("#"))
            {
                //TODO: data header line
            }
            else //Data line
            {
                //TODO: data line
            }
        }
        reader.close();
    }

    SoftFileEntry parseLine(String line){
        SoftFileEntry result = new SoftFileEntry();

        String[] val = line.split("=");

        if(val.length>0) {
            result.Key = val[0].substring(1).trim();
        }
        if(val.length>1) {
            result.Value = val[1].trim();
        }
        return result;
    }

    public String getFirstAttribute(String key){
        if (this.Attributes.containsKey(key)){
            if(null!=this.Attributes.get(key)){
                if(this.Attributes.get(key).size()>0){
                    return this.Attributes.get(key).get(0);
                }
            }
        }
        return null;
    }

    /***
     find value for given attribute, started with "prefix:"
     ***/
    public String findAttributeValue(String key, String prefix){
        if (this.Attributes.containsKey(key)){
            if(null!=this.Attributes.get(key)){
                for(String value : this.Attributes.get(key)){
                    if(value.startsWith(prefix+":")){
                        return value.replace(prefix+":","").trim();
                    }
                }
            }
        }
        return null;
    }
}
