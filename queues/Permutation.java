import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        // read input k
        int k = Integer.parseInt(args[0]);
        // init randomized queue
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        int cnt = 0;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            cnt++;
            // if we have space, enqueue item
            if (q.size() < k) q.enqueue(item);
            else {
                // swap new item with item in queue with probability k/cnt
                int rand = StdRandom.uniformInt(cnt);
                if (rand < k) {
                    q.dequeue();
                    q.enqueue(item);
                }
            }
        }
        // print out all items in queue
        while (!q.isEmpty()) {
            StdOut.println(q.dequeue());
        }
    }
}
