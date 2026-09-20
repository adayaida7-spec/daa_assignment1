package benchmark;

import algorithms.MergeSort;
import algorithms.QuickSelect;
import algorithms.QuickSort;
import utils.Metrics;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] SIZES = {
            1_000,
            10_000,
            100_000,
            1_000_000
    };

    private static final String[] TYPES = {
            "random",
            "sorted",
            "duplicates"
    };

    private static final int RUNS = 5;

    public static void main(String[] args) {
        String fileName = "results.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("algorithm,n,input_type,time_ms,comparisons,max_depth\n");

            for (int n : SIZES) {
                for (String type : TYPES) {
                    System.out.println("\nRunning n = " + n + ", type = " + type);
                    int[] original = createArray(n, type);

                    runMergeSort(original, n, type, writer);
                    runQuickSort(original, n, type, writer);
                    runQuickSelect(original, n, type, writer);
                }
            }
            System.out.println("\nBenchmark finished.");
            System.out.println("Results saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int[] createArray(
            int n,
            String type
    ) {
        int[] array = new int[n];
        Random random = new Random(42);
        if (type.equals("random")) {
            for (int i = 0; i < n; i++) {
                array[i] = random.nextInt(n);
            }
        } else if (type.equals("sorted")) {
            for (int i = 0; i < n; i++) {
                array[i] = i;
            }

        } else if (type.equals("duplicates")) {
            for (int i = 0; i < n; i++) {
                array[i] = random.nextInt(10);
            }
        }
        return array;
    }
    private static void runMergeSort(
            int[] original,
            int n,
            String type,
            FileWriter writer
    ) throws IOException {

        long[] times = new long[RUNS];
        long bestComparisons = 0;
        int bestDepth = 0;

        MergeSort.sort(original.clone(), new Metrics());
        for (int run = 0; run < RUNS; run++) {
            int[] array = original.clone();
            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            MergeSort.sort(array, metrics);

            long end = System.nanoTime();
            times[run] = end - start;
            bestComparisons = metrics.getComparisons();
            bestDepth = metrics.getMaxDepth();
        }
        long median = calculateMedian(times);
        writeResult(
                writer,
                "MergeSort",
                n,
                type,
                median,
                bestComparisons,
                bestDepth
        );
    }
    private static void runQuickSort(
            int[] original,
            int n,
            String type,
            FileWriter writer
    ) throws IOException {
        long[] times = new long[RUNS];

        long comparisons = 0;
        int depth = 0;

        QuickSort.sort(original.clone(), new Metrics());
        for (int run = 0; run < RUNS; run++) {
            int[] array = original.clone();
            Metrics metrics = new Metrics();

            long start = System.nanoTime();
            QuickSort.sort(array, metrics);
            long end = System.nanoTime();
            times[run] = end - start;
            comparisons = metrics.getComparisons();
            depth = metrics.getMaxDepth();
        }
        long median = calculateMedian(times);
        writeResult(
                writer,
                "QuickSort",
                n,
                type,
                median,
                comparisons,
                depth
        );
    }
    private static void runQuickSelect(
            int[] original,
            int n,
            String type,
            FileWriter writer
    ) throws IOException {

        long[] times = new long[RUNS];
        long comparisons = 0;
        int depth = 0;
        int k = n / 2;

        QuickSelect.select(original.clone(), k, new Metrics());

        for (int run = 0; run < RUNS; run++) {
            int[] array = original.clone();
            Metrics metrics = new Metrics();
            long start = System.nanoTime();
            int result = QuickSelect.select(array, k, metrics);
            long end = System.nanoTime();
            times[run] = end - start;
            comparisons = metrics.getComparisons();
            depth = metrics.getMaxDepth();


            int[] expected = original.clone();
            Arrays.sort(expected);
            if (result != expected[k]) {
                throw new IllegalStateException("QuickSelect returned wrong result");
            }
        }
        long median = calculateMedian(times);
        writeResult(
                writer,
                "QuickSelect",
                n,
                type,
                median,
                comparisons,
                depth
        );
    }
    private static long calculateMedian(long[] values) {
        long[] sorted = values.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }
    private static void writeResult(
            FileWriter writer,
            String algorithm,
            int n,
            String type,
            long timeNs,
            long comparisons,
            int depth
    ) throws IOException {
        double timeMs = timeNs / 1_000_000.0;

        writer.write(
                algorithm + "," +
                        n + "," +
                        type + "," +
                        timeMs + "," +
                        comparisons + "," +
                        depth +
                        "\n"
        );
        System.out.printf("%s | n=%d | %s | %.4f ms | comparisons=%d | depth=%d%n",
                algorithm,
                n,
                type,
                timeMs,
                comparisons,
                depth
        );
    }
}