package algorithms;

import utils.Metrics;
import java.util.Random ;

public class  QuickSelect {
    private static final Random random = new Random();

    public static int  select(
            int[] array,
            int k ,
            Metrics metrics
    ) {
        if (array == null || array.length == 0) {
            throw new  IllegalArgumentException(
                    "array mustn't be empty"
            );
        }
        if (k < 0 ||  k >= array.length) {
            throw new IllegalArgumentException(
                    "k must be between 0 and array length 1"
            );
        }
        int left  = 0 ;
        int right  = array.length - 1;
        int depth = 1;
        while (left <= right) {
            metrics.updateDepth(depth);
            int pivotIndex =
                    left + random.nextInt(right - left + 1);

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

                    metrics.addComparison() ;
                    if (array[current] > pivot)  {
                        swap(array,  current, greater );
                        greater-- ;
                    } else  {
                        current++ ;
                    }
                }
            }
            if (k < less) {
                right =  less - 1 ;
            } else if (k > greater) {
                left =  greater + 1;
            } else  {
                return array[k];
            }
            depth++;
        }
        throw new IllegalStateException(
                "QuickSelect fail  "
        );
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