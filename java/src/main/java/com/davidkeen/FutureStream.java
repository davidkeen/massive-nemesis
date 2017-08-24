package com.davidkeen;

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

    private static final int MAX_THREADS = 8;

    /**
     * Provides a supplier function that reverses a string.
     *
     * @param s the string to reverse.
     * @return a supplier function that reverses the given string.
     */
    private Supplier<String> stringReverser(String s) {
        return () -> {
            System.out.println(Thread.currentThread().getName() + ": reversing string '" + s + "'");
            StringBuilder sb = new StringBuilder(s);
            return sb.reverse().toString();
        };
    }

    /**
     * Asynchronously process the collection.
     */
    private List<String> process(List<String> data, ExecutorService executorService) {

        // Note: This must be processed in two separate stream pipelines. If you join these two
        // streams into one then due to the lazy nature of stream operations it will execute sequentially.
        // First build a collection of CompletableFutures...
        List<CompletableFuture<String>> futures = data.stream()
                .map(s -> CompletableFuture.supplyAsync(stringReverser(s), executorService))
                .collect(Collectors.toList());

        // then wait for all futures to finish and build the response
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> theData = Arrays.asList("alpha", "bravo", "charlie", "delta");

        // Get a thread pool: size = number of strings to process (limited to MAX_THREADS)
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(theData.size(), MAX_THREADS));

        FutureStream fs = new FutureStream();
        List<String> reversed = fs.process(theData, executorService);

        for (String s : reversed) {
            System.out.println(s);
        }

        executorService.shutdown();
    }
}
