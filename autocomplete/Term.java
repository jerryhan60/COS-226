import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term> {
    // instance variable query of class Term
    private String query;
    // instance variable weight of class Term
    private long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null || weight < 0) throw new IllegalArgumentException();
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        // defines compare method of interface Comparator
        return new RevOrder();
    }

    private static class RevOrder implements Comparator<Term> {
        public int compare(Term v, Term w) {
            if (v.weight > w.weight) return -1;
            if (v.weight < w.weight) return 1;
            return 0;
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        return new PrefixOrder(r);
    }

    private static class PrefixOrder implements Comparator<Term> {
        // first r prefix
        int r;

        // constructor
        public PrefixOrder(int r) {
            if (r < 0) throw new IllegalArgumentException();
            this.r = r;
        }

        public int compare(Term v, Term w) {
            int tmp = Math.min(v.query.length(), w.query.length());
            tmp = Math.min(tmp, r);
            for (int i = 0; i < tmp; i++) {
                if (v.query.charAt(i) < w.query.charAt(i)) return -1;
                if (v.query.charAt(i) > w.query.charAt(i)) return 1;
            }
            if (tmp == r) return 0;
            if (v.query.length() < w.query.length()) return -1;
            if (v.query.length() > w.query.length()) return 1;
            return 0;
        }
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        String s = this.weight + "\t" + this.query;
        return s;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term[] arrayTerms = new Term[4];
        arrayTerms[0] = new Term("acc", 2);
        arrayTerms[1] = new Term("bc", 1);
        arrayTerms[2] = new Term("adc", 8);
        arrayTerms[3] = new Term("aa", 3);
        Arrays.sort(arrayTerms, byReverseWeightOrder());
        for (int i = 0; i < 4; i++) {
            System.out.println(arrayTerms[i].toString());
        }
        Arrays.sort(arrayTerms, byPrefixOrder(2));
        for (int i = 0; i < 4; i++) {
            System.out.println(arrayTerms[i].toString());
        }
    }
}
