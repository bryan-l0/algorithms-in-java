import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node front, back;
    private int size = 0;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        front = null;
        back = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return front == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldfront = front;
        front = new Node();
        front.item = item;
        front.next = null;
        front.prev = oldfront;
        size++;
        if (size == 1) {
            back = front;
        }
        else {
            oldfront.next = front;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldback = back;
        back = new Node();
        back.item = item;
        back.next = oldback;
        back.prev = null;
        size++;
        if (size == 1) {
            front = back;
        }
        else {
            oldback.prev = back;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item output = front.item;
        front = front.prev;
        size--;
        if (size == 1) {
            front = back;
            front.next = null;
        }
        else if (size == 0) {
            front = null;
            back = null;
        }
        else {
            front.next = null;
        }
        return output;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item output = back.item;
        back = back.next;
        size--;
        if (size == 1) {
            back = front;
            back.prev = null;
        }
        else if (size == 0) {
            front = null;
            back = null;
        }
        else {
            back.prev = null;
        }
        return output;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        if (!(size == 0)) {
            back.prev = null;
        }
        return new DoubleLinkedIterator();
    }

    private class DoubleLinkedIterator implements Iterator<Item> {
        private Node current = front;

        public boolean hasNext() {
            return (current != null);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.prev;
            return item;
        }
    }

    public static void main(String[] args) {
        
    }
}
