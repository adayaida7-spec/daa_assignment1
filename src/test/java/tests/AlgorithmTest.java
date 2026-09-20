package tests;

import algorithms.MergeSort;
import algorithms.QuickSelect;
import algorithms.QuickSort;
import org.junit.jupiter.api.Test;
import utils.Metrics;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTest {
    private final Random random = new Random();

    @Test
    void testMergeSortWithRandomArrays() {
        for (int test = 0; test < 100; test ++) {
            int size = random.nextInt(100)  + 1;
            int[] array = createRandomArray(size);
            int[] expected = array.clone() ;
            Arrays.sort(expected);
            Metrics metrics  = new Metrics();
            MergeSort.sort(array, metrics);
            assertArrayEquals(expected,  array);
          }
    }
    @Test
    void testQuickSortWithRandomArrays()  {
        for (int test = 0;  test < 100; test++) {
            int size = random.nextInt(100) + 1;
            int[] array = createRandomArray(size);
            int[] expected = array.clone();
            Arrays.sort(expected);
            Metrics metrics =  new Metrics();
            QuickSort.sort(array, metrics);
            assertArrayEquals(expected,  array);
        }
    }
    @Test
    void testQuickSelectWithRandomArrays() {
        for  (int test = 0; test < 100; test++) {
            int size =  random.nextInt(100) + 1;
            int[] array = createRandomArray(size );
            int[] expected =  array.clone();
            Arrays.sort(expected);
            int k =  random.nextInt(size);
            Metrics metrics = new Metrics();
            int result =  QuickSelect.select( array, k,  metrics);
            assertEquals(expected[k], result);
        }
    }
    @Test
    void  testEmptyArray() {
        int[] array = {};
        Metrics metrics = new  Metrics();
        assertDoesNotThrow(() -> MergeSort.sort(array, metrics));
        assertDoesNotThrow(() -> QuickSort.sort(array, metrics));
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(array, 0, metrics));
    }
    @Test
    void testOneElement() {
        int[]  array = {42};
        Metrics metrics = new  Metrics();
        MergeSort.sort(array, metrics);
        assertArrayEquals( new int[] {42}, array );
        QuickSort.sort(array,  metrics);
        assertArrayEquals(
                new  int[]{42},
                array
        );
        assertEquals( 42, QuickSelect.select(array, 0, metrics));
    }
    @Test
    void testAllElementsEqual() {
        int[] array = {
                5, 5, 5, 5, 5, 5, 5, 5
        };
        int[] expected  = array.clone();
        Metrics metrics = new  Metrics();
        MergeSort.sort(array, metrics);
        assertArrayEquals(expected, array);
        QuickSort.sort(array,  metrics);
        assertArrayEquals(expected, array);
        int result = QuickSelect.select(array, 3, metrics);
        assertEquals(5,  result);
    }
    @Test
    void testAlreadySortedArray()  {
        int[] array  = {
                1, 2, 3, 4, 5,
                6, 7, 8, 9, 10
        };
        int[] expected = array.clone();
        Metrics metrics = new Metrics();
        MergeSort.sort(array, metrics);
        assertArrayEquals(expected, array) ;
        QuickSort.sort(array,  metrics);
        assertArrayEquals(expected, array);
        assertEquals( 6, QuickSelect.select(array, 5, metrics));
    }
    @Test
    void testInvalidK() {
        int[] array = {1, 2, 3} ;
        Metrics metrics = new Metrics();
        assertThrows(
                IllegalArgumentException.class,
                () -> QuickSelect.select(array,  -1, metrics)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> QuickSelect.select(array, 3, metrics)
        );
    }
    private  int[] createRandomArray(int size) {
        int[] array = new  int[size];
        for (int i  = 0; i < size; i++) {
            array[i] =  random.nextInt(1000);
        }
        return array;
    }
}