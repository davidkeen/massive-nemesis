package com.davidkeen.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Asynchronously process a collection of data using CompletableFutures.
 * We use a custom ExecutorService to avoid using the ForkJoinPool.
 */
public class FutureStream {

    /**
     * Provides a supplier function that reverses a string.
     *
     * @param s the string to reverse.
     * @return a supplier function that reverses the given string.
     */
    private Supplier<String> StringReverser(String s) {
        return () -> {
            StringBuilder sb = new StringBuilder(s);
            return sb.reverse().toString();
        };
    }

    /**
     * Asynchronously process the collection.
     */
    private void process(List<String> data, ExecutorService executorService) {

        // Note: This must be processed in two separate stream pipelines. If you join these two
        // streams into one then due to the lazy nature of stream operations it will execute sequentially.
        // First build a collection of CompletableFutures...
        List<CompletableFuture<String>> futures = data.stream()
                .map(s -> CompletableFuture.supplyAsync(StringReverser(s), executorService))
                .collect(Collectors.toList());

        // then wait for all futures to finish and build the response
        futures.stream()
                .map(CompletableFuture::join)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {

        List<String> theData = Arrays.asList("alpha", "bravo", "charlie", "delta");

        // Get a thread pool: size = number of strings to process (max threads 8)
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(theData.size(), 8));

        FutureStream fs = new FutureStream();
        fs.process(theData, executorService);

        executorService.shutdown();
    }
}
