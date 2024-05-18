package org.assessment;

public class Main {
    public static void main(String[] args) {
        int[] arr = {1,3,6,4,1,2};
        int[] arr1 = {5, -1, -3};
        System.out.println(smallestNonOccurringNumber(arr));
        System.out.println(smallestNonOccurringNumber(arr1));
    }

    static int smallestNonOccurringNumber(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;
        }
        int[] occurrence = new int[arr.length + 1];
        for (int num : arr) {
            if (num > 0 && num <= arr.length) {
                occurrence[num] = num;
            }
        }

        for (int i = 1; i < occurrence.length; i++) {
            if (occurrence[i] == 0) {
                return i;
            }
        }

        return arr.length + 1;
    }
}