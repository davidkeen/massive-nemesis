package com.davidkeen.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReader {

    public List<String> readHeader(String file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
            return reader.lines()
                    .limit(1)
                    .map(line -> line.split(","))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toList());
        }
    }

    public List<String> readLines(String file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
            return reader.lines().skip(1).collect(Collectors.toList());
        }
    }

    public static void main(String[] args) throws Exception {
        String path = "java/src/main/resources/test.csv";

        CsvReader csvReader = new CsvReader();
        List<String> columns = csvReader.readHeader(path);
        columns.forEach(System.out::println);

        List<String> lines = csvReader.readLines(path);
        lines.forEach(System.out::println);
    }

}
