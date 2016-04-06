package spark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Java 8 versions of the Apache Spark examples at http://spark.apache.org/docs/latest/quick-start.html
 */
public class SparkExamples {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get(SparkExamples.class.getResource("/README.md").toURI());

        longestLine(path);

        wordCount(path);
    }

    /**
     * Finds the longest line in a file.
     *
     * @param path path to input file.
     */
    public static void longestLine(Path path) throws IOException, URISyntaxException {
        Files.lines(path)
                .mapToInt(line -> line.split(" ").length)
                .reduce(Math::max)
                .ifPresent(System.out::println);
    }

    /**
     * Gets the counts of each word.
     *
     * @param path path to input file.
     */
    public static void wordCount(Path path) throws IOException, URISyntaxException {
        Files.lines(path)
                .limit(10)
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .collect(Collectors.groupingBy(Function.<String>identity(), Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}
