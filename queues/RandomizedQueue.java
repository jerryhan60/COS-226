import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    /* @ citation Adapted from: https://algs4.cs.princeton.edu/13stacks/ResizingArrayStack.java.html
     */
    private static final int INITCAP = 8; // initial capacity of the resizing array
    private Item[] q; // randomized queue elements
    private int n; // number of elements in queue
    private int last; // index after last element

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[INITCAP];
        n = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the array to capacity cap
    private void resize(int cap) {
        if (cap < n) throw new IllegalArgumentException("Capacity is too small");
        if (cap < 1) return;
        Item[] copy = (Item[]) new Object[cap];
        for (int i = 0; i < n; i++) {
            copy[i] = q[i];
        }
        q = copy;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (n == q.length) resize(2 * q.length);
        q[last++] = item;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int idx = StdRandom.uniformInt(n); // find random index
        Item res = q[idx];
        q[idx] = q[last - 1]; // move last element to fill the hole
        q[last - 1] = null; // delete last element
        last--;
        n--;
        if (n > 0 && n <= q.length / 4) resize(q.length / 2);
        return res;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int idx = StdRandom.uniformInt(n);
        return q[idx];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] order; // randomized order
        private int i = 0; // array index

        // constructor function
        public RandomizedQueueIterator() {
            if (n == 0) return;
            order = new int[n];
            for (int j = 0; j < n; j++) order[j] = j;
            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return (i < n);
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return q[order[i++]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        q.enqueue("a");
        q.enqueue("b");
        for (String s : q) {
            StdOut.println(s);
        }
        StdOut.println(q.sample());
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println(q.isEmpty());
        StdOut.println(q.size());
    }

}
