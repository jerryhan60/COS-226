import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException();
        }
        // stores length of array
        int n = a.length;
        if (n == 0) return -1;
        // key exceeds last element in array
        if (comparator.compare(a[n - 1], key) < 0) {
            return -1;
        }
        // set first to -1 and last to n-1 so to find the first
        // in the binary search
        int first = -1;
        int last = n - 1;
        while (last - first > 1) {
            int mid = (first + last) >>> 1;
            int res = comparator.compare(a[mid], key);
            if (res < 0) first = mid;
            else last = mid;
        }
        if (comparator.compare(a[last], key) != 0) return -1;
        return last;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException();
        }
        // stores the length of the array
        int n = a.length;
        if (n == 0) return -1;
        // key less than first element in array
        if (comparator.compare(key, a[0]) < 0) {
            return -1;
        }
        // set first to 0 and last to n so to find the last in the binary search
        int first = 0;
        int last = n;
        while (last - first > 1) {
            int mid = (first + last) >>> 1;
            int res = comparator.compare(a[mid], key);
            if (res <= 0) first = mid;
            else last = mid;
        }
        if (comparator.compare(a[first], key) != 0) return -1;
        return first;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term[] arr = new Term[10];
        arr[0] = new Term("ab", 0);
        arr[1] = new Term("bc", 3);
        arr[2] = new Term("ba", 1);
        arr[3] = new Term("ab", 8);
        arr[4] = new Term("ac", 5);
        arr[5] = new Term("ab", 9);
        arr[6] = new Term("bc", 2);
        arr[7] = new Term("ab", 10);
        arr[8] = new Term("bd", 7);
        arr[9] = new Term("ab", 6);


        Comparator<Term> revOrder = Term.byReverseWeightOrder();
        Comparator<Term> prefixOrder = Term.byPrefixOrder(2);
        Arrays.sort(arr, revOrder);
        StdOut.println("This is the array sorted by reverse weight");
        for (int i = 0; i < 10; i++) {
            StdOut.println(arr[i].toString());
        }
        StdOut.println("This is the array sorted by 2-prefix ");
        Arrays.sort(arr, prefixOrder);

        for (int i = 0; i < 10; i++) {
            StdOut.println(arr[i].toString());
        }
        StdOut.println("first index " + BinarySearchDeluxe.firstIndexOf(arr, arr[0], prefixOrder));
        StdOut.println("last index " + BinarySearchDeluxe.lastIndexOf(arr, arr[0], prefixOrder));
    }
}
