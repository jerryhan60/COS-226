import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private Digraph G; // digraph
    private RedBlackBST<String, Queue<Integer>> rbBST; // red-black BST
    private ShortestCommonAncestor sca; // shortest common ancestor
    private String[] syn; // synsets

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In in = new In(synsets);
        String[] nodes = in.readAllLines();
        int n = nodes.length;
        G = new Digraph(n);
        rbBST = new RedBlackBST<String, Queue<Integer>>();
        syn = new String[n];
        for (int i = 0; i < n; i++) {
            String[] split = nodes[i].split(",");
            int idx = Integer.parseInt(split[0]);
            syn[idx] = split[1];
            String[] nouns = split[1].split(" ");
            for (int j = 0; j < nouns.length; j++) {
                Queue<Integer> prev = new Queue<Integer>();
                if (rbBST.contains(nouns[j])) prev = rbBST.get(nouns[j]);
                prev.enqueue(idx);
                rbBST.put(nouns[j], prev);
            }
        }

        In in2 = new In(hypernyms);
        String[] edges = in2.readAllLines();
        for (int i = 0; i < edges.length; i++) {
            String[] split = edges[i].split(",");
            if (split.length == 1) continue;
            int cur = Integer.parseInt(split[0]);
            for (int j = 1; j < split.length; j++) {
                int par = Integer.parseInt(split[j]);
                G.addEdge(cur, par);
            }
        }

        sca = new ShortestCommonAncestor(G);
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return rbBST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return rbBST.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) throw new IllegalArgumentException();
        if (!isNoun(noun1) || !isNoun(noun2)) throw new IllegalArgumentException();
        Queue<Integer> subset1 = rbBST.get(noun1);
        Queue<Integer> subset2 = rbBST.get(noun2);
        return syn[sca.ancestorSubset(subset1, subset2)];
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) throw new IllegalArgumentException();
        if (!isNoun(noun1) || !isNoun(noun2)) throw new IllegalArgumentException();
        Queue<Integer> subset1 = rbBST.get(noun1);
        Queue<Integer> subset2 = rbBST.get(noun2);
        return sca.lengthSubset(subset1, subset2);
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet w = new WordNet("synsets11.txt", "hypernyms11AmbiguousAncestor.txt");
        StdOut.println(w.sca("a", "b"));
        StdOut.println(w.distance("a", "b"));
        StdOut.println(w.isNoun("a"));
        StdOut.println(w.nouns());

    }

}
