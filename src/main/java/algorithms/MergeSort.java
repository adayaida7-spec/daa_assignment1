package algorithms;

import utils.Metrics;

public class MergeSort {

    private static final int INSERTION_LIMIT = 15;


    public static void sort(int[] array, Metrics metrics){

        int[] buffer = new int[array.length];

        mergeSort(array, buffer, 0, array.length - 1, metrics, 1);

    }

    private static void mergeSort(
            int[] array,
            int[] buffer,
            int left,
            int right,
            Metrics metrics,
            int depth
    ) {
        metrics.updateDepth(depth);

        if (left >= right) {
            return;
        }

        if (right - left + 1 <= INSERTION_LIMIT) {

            insertionSort(array, left, right, metrics);

            return;

        }

        int middle = (left + right) / 2;


        mergeSort(
                array,
                buffer,
                left,
                middle,
                metrics,
                depth + 1
        );


        mergeSort(
                array,
                buffer,
                middle + 1,
                right,
                metrics,
                depth + 1
        );


        merge(
                array,
                buffer,
                left,
                middle,
                right,
                metrics
        );
    }

        private static void merge(
        int[] array,
        int[] buffer,
        int left,
        int middle,
        int right,
        Metrics metrics
){

            int i = left;
            int j = middle + 1;
            int k = left;


            while(i <= middle && j <= right){

                metrics.addComparison();


                if(array[i] <= array[j]){

                    buffer[k] = array[i];
                    i++;

                }else{

                    buffer[k] = array[j];
                    j++;

                }

                k++;

            }


            while(i <= middle){

                buffer[k] = array[i];
                i++;
                k++;

            }


            while(j <= right){

                buffer[k] = array[j];
                j++;
                k++;

            }


            for(int index = left; index <= right; index++){

                array[index] = buffer[index];

            }

        }
    private static void insertionSort(
            int[] array,
            int left,
            int right,
            Metrics metrics
    ) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= left) {
                metrics.addComparison();

                if (array[j] <= key) {
                    break;
                }

                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
    }    }