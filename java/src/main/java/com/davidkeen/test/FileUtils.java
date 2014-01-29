package com.davidkeen.test;

import com.google.common.io.Files;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;


public class FileUtils {

    /**
     * Uses Spring Resource interface and Guava Files to read a file from the classpath into a String
     * using the default charset of this Java virtual machine.
     *
     * @param fileName the name of the file to read.
     * @return String contents of the file.
     * @throws java.io.IOException
     */
    public static String readFileFromClasspath(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        return Files.toString(resource.getFile(), Charset.defaultCharset());
    }
}
