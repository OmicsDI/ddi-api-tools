package uk.ac.ebi.ddi.api.readers.paxdb.ws.client;

import uk.ac.ebi.ddi.api.readers.paxdb.ws.model.PaxDBDataset;

import java.io.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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
 * Created by ypriverol (ypriverol@gmail.com) on 27/01/2017.
 */
public class PaxDBDatasetReader {

    enum PaxDBProperty {

        NAME("name"),
        SCORE("score"),
        WEIGHT("weight"),
        DESCRIPTION("description"),
        ORGAN("organ"),
        INTEGRATED("integrated"),
        COVERAGE("coverage"),
        PUBLICATION_YEAR("publication_year"),
        FILENAME("filename"),
        INTERNAL_ID("internal_id");

        private String name;

        PaxDBProperty(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static PaxDBDataset readDataset(ByteArrayOutputStream fileDataset) throws IOException {
        PaxDBDataset dataset = new PaxDBDataset();
        InputStream is = new ByteArrayInputStream(fileDataset.toByteArray());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if(line.startsWith("#")){
                Map.Entry property = readProperty(line);
                if(property != null){
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.NAME.getName()))
                        dataset.setName((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.SCORE.getName()))
                        dataset.setScore((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.WEIGHT.getName()))
                        dataset.setWeigth((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.DESCRIPTION.getName()))
                        dataset.setDescription((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.ORGAN.getName()))
                        dataset.setTissue((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.PUBLICATION_YEAR.getName()))
                        dataset.setPublicationDate((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.INTEGRATED.getName()))
                        dataset.setIntegrated((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.COVERAGE.getName()))
                        dataset.setCoverage((String) property.getValue());
                    if(property.getKey().toString().equalsIgnoreCase(PaxDBProperty.FILENAME.getName()))
                        dataset.setFileName((String) property.getValue());
                }else if(line.contains(PaxDBProperty.INTERNAL_ID.getName())){
                        Map<String, Map.Entry<String,String>> absoluteAbundances = readAbsoluteAbundances(br);
                        if(absoluteAbundances != null && absoluteAbundances.size() > 0)
                            dataset.setAbundanceProteins(absoluteAbundances);
                }
            }
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        return dataset;
    }

    private static Map<String, Map.Entry<String,String>> readAbsoluteAbundances(BufferedReader br) throws IOException {
        String line;
        Map<String,Map.Entry<String, String>> mapAbundances = new HashMap<>();
        while((line = br.readLine()) != null){
            String[] lineArr = line.split("\\s+");
            if (lineArr.length == 3){
                String idProtein = lineArr[1];
                String abundance = lineArr[2];
                mapAbundances.put(idProtein, new AbstractMap.SimpleEntry<>(abundance, null));
            }else if (lineArr.length == 4){
                String idProtein = lineArr[1];
                String abundance = lineArr[2];
                String rawSpectralCount = lineArr[3];
                mapAbundances.put(idProtein, new AbstractMap.SimpleEntry<>(abundance, rawSpectralCount));
            }
        }
        return mapAbundances;
    }

    private static Map.Entry<String, String> readProperty(String line){

        String[] lineArr = line.replaceFirst("#", "").split(":", 2);
        if(lineArr.length == 2){
            return new AbstractMap.SimpleEntry<>(lineArr[0].trim(), lineArr[1].trim());
        }
        return null;
    }

    private static Map.Entry<String, String> readFileIdentifierMap(String line){
        String[] lineArr = line.split("\\s+");
        if(lineArr.length == 2){
            return new AbstractMap.SimpleEntry<>(lineArr[0].trim(), lineArr[1].trim());
        }
        return null;
    }

    public static Map<String, String> readMapFileIdentifiers(ByteArrayOutputStream file) throws IOException {
        Map<String, String> identifierMap = new HashMap<>();
        InputStream is = new ByteArrayInputStream(file.toByteArray());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if(!line.startsWith("#")){
                Map.Entry<String, String> property = readFileIdentifierMap(line);
                if(property != null)
                    identifierMap.put(property.getKey().toString(), property.getValue().toString());
            }
        }
        return identifierMap;
    }

    public static Map<String, String> readProteinIdentifiers(ByteArrayOutputStream zipInputStreamProteinFiles) throws IOException {
        Map<String, String> proteinIds = new HashMap<>();
        InputStream is = new ByteArrayInputStream(zipInputStreamProteinFiles.toByteArray());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if(line.split("\\s+").length == 2){
                String[] idArr = line.split("\\s+");
                proteinIds.put(idArr[0], idArr[1]);
            }
        }
        return proteinIds;
    }
}
