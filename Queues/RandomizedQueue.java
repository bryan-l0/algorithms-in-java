import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int n = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void refactor(int i) {
        System.arraycopy(s, i + 1, s, i, n - i - 1);
        s[n - 1] = null;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(s, 0, copy, 0, n);
        s = copy;
    }

    private int random(int range) {
        return StdRandom.uniform(range);
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (n == s.length) {
            resize(2 * s.length);
        }
        s[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) {
            throw new NoSuchElementException();
        }
        int index = random(n);
        Item output = s[index];
        s[index] = null;
        if (!(index == n - 1)) {
            refactor(index);
        }
        n--;
        if (n > 0 && (n == s.length / 4)) {
            resize(s.length / 2);
        }
        return output;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (n == 0) {
            throw new NoSuchElementException();
        }
        int index = random(n);
        Item output = s[index];
        return output;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {

        private int arraySize;
        Item[] iterateArray = (Item[]) new Object[n];

        public RandomQueueIterator() {
            arraySize = n;
            System.arraycopy(s, 0, iterateArray, 0, n);
        }

        private void refactorIterator(int i) {
            System.arraycopy(iterateArray, i + 1, iterateArray, i, arraySize - i);
            iterateArray[arraySize] = null;
        }

        public boolean hasNext() {
            return arraySize != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                int index = random(arraySize--);
                Item output = iterateArray[index];
                if (!(index == arraySize)) {
                    refactorIterator(index);
                }
                return output;
            }

        }
    }
    
    public static void main(String[] args) {
    }
}
