package algorithms;

import utils.Metrics;
import java.util.Random;

public class QuickSort {
    private static final Random random = new Random();

    public static void sort(int[] array, Metrics metrics) {
        quickSort(array, 0, array.length - 1, metrics, 1);
    }
    private static void quickSort(
            int[] array,
            int left,
            int right,
            Metrics metrics,
            int depth
    ) {
        while (left < right) {
            metrics.updateDepth(depth);
            int pivotIndex = left + random.nextInt(right - left + 1);
            int pivot = array[pivotIndex];

            int less = left;
            int current = left;
            int greater = right;
            while (current <= greater) {

                metrics.addComparison();
                if (array[current] < pivot) {

                    swap(array, less, current);

                    less++;
                    current++;
                } else {

                    metrics.addComparison();
                    if (array[current] > pivot) {
                        swap(array, current, greater);
                        greater--;
                    } else {
                        current++;

                    }
                }
            }
            int leftSize = less - left;
            int rightSize = right - greater;

            if (leftSize < rightSize) {
                if (left < less - 1) {
                    quickSort(
                            array,
                            left,
                            less - 1,
                            metrics,
                            depth + 1
                    );

                }
                left = greater + 1;
            } else {
                if (greater + 1 < right) {
                    quickSort(
                            array,
                            greater + 1,
                            right,
                            metrics,
                            depth + 1
                    );
                }
                right = less - 1;
            }
        }
    }
    private static void swap(
            int[] array,
            int first,
            int second
    ) {
        int temp = array[first];

        array[first] = array[second];

        array[second] = temp;
    }

}