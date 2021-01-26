public class Outcast {

    private final WordNet input;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        input = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int greatestLength = Integer.MIN_VALUE;
        String noun = null;
        for (String i : nouns) {
            int length = 0;
            for (String j : nouns) {
                if (!i.equals(j)) {
                    int distance = input.distance(i, j);
                    length += distance;
                }
            }
            if (length > greatestLength) {
                greatestLength = length;
                noun = i;
            }
        }
        return noun;
    }
}
