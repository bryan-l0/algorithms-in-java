import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import java.util.ArrayList;
import java.util.HashMap;

public class WordNET {

    private final HashMap<String, Queue<Integer>> synset = new HashMap<>();
    private final ArrayList<String> synsetArray = new ArrayList<String>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        In synsetRaw = new In(synsets);
        In hypernymRaw = new In(hypernyms);
        int index = 0;
        // store synset in an arraylist
        while (synsetRaw.hasNextLine()) {
            String temp = synsetRaw.readLine();
            String[] tempArray = temp.split(",");
            String[] choppedArray = tempArray[1].split("\\s+");
            for (String i : choppedArray) {
                if (synset.containsKey(i)) {
                    Queue<Integer> intArray = synset.remove(i);
                    intArray.enqueue(index);
                    synset.put(i, intArray);
                }
                else {
                    Queue<Integer> intArray = new Queue<Integer>();
                    intArray.enqueue(index);
                    synset.put(i, intArray);
                }
            }
            synsetArray.add(tempArray[1]);
            index++;
        }
        Digraph digraph = new Digraph(index);
        // create digraph
        while (hypernymRaw.hasNextLine()) {
            String temp = hypernymRaw.readLine();
            String[] tempArray = temp.split(",");
            int v = Integer.parseInt(tempArray[0]);
            for (int i = 1; i < tempArray.length; i++) {
                digraph.addEdge(v, Integer.parseInt(tempArray[i]));
            }
        }
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synset.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return synset.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        // turns both nouns into iterable objects
        Iterable<Integer> a = synset.get(nounA);
        Iterable<Integer> b = synset.get(nounB);
        int output = sap.length(a, b);
        return output;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        // turns both nouns into iterable objects
        Iterable<Integer> a = synset.get(nounA);
        Iterable<Integer> b = synset.get(nounB);
        int ancestor = sap.ancestor(a, b);
        String output = synsetArray.get(ancestor);
        return output;
    }
}
