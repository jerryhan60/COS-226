import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;


public class Autocomplete {
    // Array of terms
    private Term[] terms;

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) throw new IllegalArgumentException();
        this.terms = new Term[terms.length];

        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) throw new IllegalArgumentException();
            this.terms[i] = terms[i];
        }

        Arrays.sort(this.terms);
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null) throw new IllegalArgumentException();
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        Comparator<Term> revOrder = Term.byReverseWeightOrder();

        Term aux = new Term(prefix, 0);
        int first = BinarySearchDeluxe.firstIndexOf(terms, aux, prefixOrder);
        int last = BinarySearchDeluxe.lastIndexOf(terms, aux, prefixOrder);
        if (first == -1 || last == -1) return new Term[0];

        Term[] matches = new Term[last - first + 1];
        for (int i = first; i <= last; i++) {
            matches[i - first] = terms[i];
        }
        Arrays.sort(matches, revOrder);
        return matches;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) throw new IllegalArgumentException();
        Term aux = new Term(prefix, 0);
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        int first = BinarySearchDeluxe.firstIndexOf(terms, aux, prefixOrder);
        int last = BinarySearchDeluxe.lastIndexOf(terms, aux, prefixOrder);
        if (first == -1 || last == -1) return 0;
        return last - first + 1;
    }

    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        // StdOut.println(1);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            // extra test
            // StdOut.println(prefix);
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++) {
                // why does it print in pairs?
                StdOut.println(results[i]);
            }
        }
    }

}
