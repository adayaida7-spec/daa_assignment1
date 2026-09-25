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
            1_000, 10_000, 100_000, 1_000_000
    };

    private static final String[] TYPES = {
            "random", "sorted", "duplicates"
    };

    private static final String[] ALGORITHMS = {
            "MergeSort", "QuickSort", "QuickSelect"
    };

    private static final int WARMUP_RUNS = 5;
    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        try (
                FileWriter summary = new FileWriter("results.csv");
                FileWriter raw = new FileWriter("results_raw.csv");
                FileWriter environment =
                        new FileWriter("benchmark_environment.txt")
        ) {
            summary.write(
                    "algorithm,n,input_type,time_ms,"
                            + "comparisons,max_depth,iterations\n"
            );

            raw.write(
                    "algorithm,n,input_type,run,time_ns,"
                            + "comparisons,max_depth,iterations\n"
            );

            environment.write(
                    "Measured at: " + java.time.Instant.now() + "\n"
                            + "Java: " + System.getProperty("java.version") + "\n"
                            + "VM: " + System.getProperty("java.vm.name") + "\n"
                            + "OS: " + System.getProperty("os.name") + " "
                            + System.getProperty("os.version") + " "
                            + System.getProperty("os.arch") + "\n"
                            + "Warmup runs per case: " + WARMUP_RUNS + "\n"
                            + "Measured runs per case: " + RUNS + "\n"
                            + "Input seed: 42; pivot seeds: not fixed\n"
                            + "Summary: independent median of each metric\n"
            );

            for (int n : SIZES) {
                for (String type : TYPES) {
                    int[] original = createArray(n, type);

                    int[] expected = original.clone();
                    Arrays.sort(expected);

                    for (String algorithm : ALGORITHMS) {
                        runCase(
                                algorithm,
                                original,
                                expected,
                                type,
                                summary,
                                raw
                        );
                    }
                }
            }
        }

        System.out.println(
                "Saved results.csv, results_raw.csv "
                        + "and benchmark_environment.txt"
        );
    }

    private static int[] createArray(int n, String type) {
        int[] array = new int[n];
        Random random = new Random(42);

        for (int i = 0; i < n; i++) {
            array[i] = switch (type) {
                case "random" -> random.nextInt(n);
                case "sorted" -> i;
                case "duplicates" -> random.nextInt(10);
                default -> throw new IllegalArgumentException(
                        "Unknown input type: " + type
                );
            };
        }

        return array;
    }

    private static int execute(
            String algorithm,
            int[] array,
            Metrics metrics
    ) {
        switch (algorithm) {
            case "MergeSort":
                MergeSort.sort(array, metrics);
                return 0;

            case "QuickSort":
                QuickSort.sort(array, metrics);
                return 0;

            case "QuickSelect":
                return QuickSelect.select(
                        array, array.length / 2, metrics
                );

            default:
                throw new IllegalArgumentException(
                        "Unknown algorithm: " + algorithm
                );
        }
    }

    private static void runCase(
            String algorithm,
            int[] original,
            int[] expected,
            String type,
            FileWriter summary,
            FileWriter raw
    ) throws IOException {
        int n = original.length;

        long[] times = new long[RUNS];
        long[] comparisons = new long[RUNS];
        long[] depths = new long[RUNS];
        long[] iterations = new long[RUNS];

     
        for (int run = 0; run < WARMUP_RUNS; run++) {
            execute(algorithm, original.clone(), new Metrics());
        }

        for (int run = 0; run < RUNS; run++) {
            int[] array = original.clone();
            Metrics metrics = new Metrics();

            long start = System.nanoTime();
            int result = execute(algorithm, array, metrics);
            times[run] = System.nanoTime() - start;

            comparisons[run] = metrics.getComparisons();
            depths[run] = metrics.getMaxDepth();
            iterations[run] = metrics.getIterations();


            boolean correct = algorithm.equals("QuickSelect")
                    ? result == expected[n / 2]
                    : Arrays.equals(array, expected);

            if (!correct) {
                throw new IllegalStateException(
                        algorithm + " returned wrong result"
                );
            }

            raw.write(
                    algorithm + "," + n + "," + type + ","
                            + (run + 1) + ","
                            + times[run] + ","
                            + comparisons[run] + ","
                            + depths[run] + ","
                            + iterations[run] + "\n"
            );
        }


        summary.write(
                algorithm + "," + n + "," + type + ","
                        + calculateMedian(times) / 1_000_000.0 + ","
                        + calculateMedian(comparisons) + ","
                        + calculateMedian(depths) + ","
                        + calculateMedian(iterations) + "\n"
        );

        System.out.println(
                algorithm + " | n=" + n + " | " + type
                        + " | verified " + RUNS + " runs"
        );
    }

    private static long calculateMedian(long[] values) {
        long[] sorted = values.clone();
        Arrays.sort(sorted);

        return sorted[sorted.length / 2];
    }
}