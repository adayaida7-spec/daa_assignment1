package utils;

public class Metrics {
    private long comparisons;
    private int maxDepth;
    private int iterations;
    private long startTime;
    private long endTime;

    public Metrics() {
        reset();
    }

    public void addComparison() {
        comparisons++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void updateDepth(int depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void addIteration() {
        iterations++;
    }

    public int getIterations() {
        return iterations;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getTime() {
        return endTime - startTime;
    }

    public void reset() {
        comparisons = 0;
        maxDepth = 0;
        iterations = 0;
        startTime = 0;
        endTime = 0;
    }
}