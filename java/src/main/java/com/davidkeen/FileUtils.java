package com.davidkeen;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileUtils {

    public static void main(String[] args) throws Exception {
        System.out.println(readFileFromClasspathSpring("test.txt"));

        // Using the class's class loader means we need to use an absolute path here.
        System.out.println(readFileFromClasspathJava("/test.txt"));
    }

    /**
     * Uses Spring Resource interface and Guava Files to read a file from the classpath into a String
     * using the default charset of this Java virtual machine.
     *
     * @param fileName the name of the file to read.
     * @return String contents of the file.
     */
    public static String readFileFromClasspathSpring(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        return com.google.common.io.Files.toString(resource.getFile(), Charset.defaultCharset());
    }

    /**
     * Uses {@link java.nio.file.Files} and {@link java.nio.file.Paths} to read a file from the classpath.
     *
     * @param fileName the absolute path to the file.
     * @return String contents of the file.
     */
    public static String readFileFromClasspathJava(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(FileUtils.class.getResource(fileName).toURI());
        return new String(java.nio.file.Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
