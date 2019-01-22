package uk.ac.ebi.ddi.api.readers.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
public class FileUtils {

    /**
     * Retrieve InputStream from URL
     *
     * @param url http url to retrieve the file
     * @return InputStream to be read
     * @throws IOException
     */
    private static InputStream connectToURL(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setReadTimeout(60000);
        return urlConnection.getInputStream();
    }

    /**
     * Retrieve a Map of File Name and OutputStream from
     *
     * @param url URL to retrieve a Map of String and {@link ByteArrayOutputStream}
     * @return Map with file name and {@link ByteArrayOutputStream}
     * @throws IOException
     */
    public static Map<String, ByteArrayOutputStream> doZipInputStream(String url) throws IOException {

        ZipInputStream zipInputStream = new ZipInputStream(connectToURL(url));

        ZipEntry zipEntry;
        final byte[] tempBuffer = new byte[8192 * 2];
        Map<String, ByteArrayOutputStream> entryBufMap = new HashMap<>();

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                int bytesRead = -1;
                final ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                while ((bytesRead = zipInputStream.read(tempBuffer)) != -1) {
                    streamBuilder.write(tempBuffer, 0, bytesRead);
                }
                // System.out.println(nextEntry.getName());
                entryBufMap.put(zipEntry.getName(), streamBuilder);
            }
        }
        return entryBufMap;
    }


    /**
     * Retrieve a Map of File Name and OutputStream from
     *
     * @param url URL to retrieve a Map of String and {@link ByteArrayOutputStream}
     * @return Map with file name and {@link ByteArrayOutputStream}
     * @throws IOException
     */
    public static ByteArrayOutputStream doGZipInputStream(String url) throws IOException {

        GZIPInputStream zipInputStream = new GZIPInputStream(connectToURL(url));

        final byte[] tempBuffer = new byte[8192 * 2];
        Map<String, ByteArrayOutputStream> entryBufMap = new HashMap<>();

        int bytesRead = -1;
        final ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
        while ((bytesRead = zipInputStream.read(tempBuffer)) != -1) {
            streamBuilder.write(tempBuffer, 0, bytesRead);
        }
        return streamBuilder;
    }

    /**
     * Retrieve a {@link ByteArrayOutputStream} from a URL
     *
     * @param url String URL with the file
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    public static ByteArrayOutputStream doInputStream(String url) throws IOException {
        InputStream inputStream = connectToURL(url);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            outputStream.write(data, 0, nRead);
        }
        outputStream.flush();
        return outputStream;
    }


    public static String getNameFromInternalZipPath(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }
}
