package pers.zr.opensource.magic.kit.sort;

import java.util.Random;

/**
 *
 * 快速排序，时间复杂度：理想O(N*logN),最差O(N^2),速度比堆排序快。
 * Created by zhurong on 2016-7-6.
 */
public class QuickSort {

    public static void quickSort(int[] array) {
        quickSort(array, 0, array.length-1);
    }

    public static void quickSort(int[] array, int start, int end) {
        int i = start, j = end;
        // Get the pivot element from the middle of the list
        int pivot = array[start + (end-start)/2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (array[i] < pivot) {
                i++;
            }
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (array[j] > pivot) {
                j--;
            }

            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(array, i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (start < j)
            quickSort(array, start, j);
        if (i < end)
            quickSort(array, i, end);
    }

    private static void exchange(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String args[]) {
        int count = 1000000;
        int [] array = new int[count];
        for(int i=0; i<count; i++) {
            array[i] = (new Random()).nextInt(10000000);
        }

        long start = System.currentTimeMillis();
        quickSort(array);
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        start = System.currentTimeMillis();
        HeapSort.heapSort(array);
        end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
