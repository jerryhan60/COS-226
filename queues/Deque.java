import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    /* @ citation Adapted from: https://algs4.cs.princeton.edu/13stacks/LinkedStack.java.html
     */
    private int n; // number of elements
    private Node first, last; // first and last Nodes

    private class Node {
        private Item item; // item
        private Node next; // pointer to next
        private Node prev; // point to previous
    }

    // construct an empty deque
    public Deque() {
        n = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldFirst;
        if (isEmpty()) last = first;
        else oldFirst.prev = first;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        n--;
        Item item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        n--;
        Item item = last.item;
        last = last.prev;
        if (isEmpty()) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // deque iterator
    private class DequeIterator implements Iterator<Item> {
        // cur node
        private Node cur = first;

        public boolean hasNext() {
            return cur != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<String>();
        dq.addFirst("a");
        dq.addLast("b");
        for (String s : dq) {
            StdOut.println(s);
        }
        StdOut.println(dq.removeFirst());
        StdOut.println(dq.size());
        StdOut.println(dq.removeLast());
        StdOut.println(dq.isEmpty());
    }

}
