package com.davidkeen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvProcessor {

    public static void main(String[] args) throws Exception {
        Path in = Paths.get(CsvProcessor.class.getResource("/test.csv").toURI());
        Path out = Paths.get("target/out");
        Files.deleteIfExists(out);
        Files.createFile(out);

        try (BufferedReader reader = Files.newBufferedReader(in, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                String name = record.get("name").trim().toUpperCase();
                String age = record.get("age").trim();
                writer.write(String.format("%s: %s%n", name, age));
            }
        }
    }
}
