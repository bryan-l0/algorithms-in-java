import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.HashMap;

public class SAP {

    private final Digraph d;
    private final HashMap<Double, int[]> cache = new HashMap<>();
    private int[] cache2;
    private Iterable<Integer> lastV;
    private Iterable<Integer> lastW;

    private class Node {

        private int vlength;
        private int wlength;
        private int key;
        private int root;
        private Node prev;

        private Node() {
            vlength = Integer.MAX_VALUE;
            wlength = Integer.MAX_VALUE;
            prev = null;
        }

    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        d = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // checks whether v equals to w
        if (v == w) {
            return 0;
        }

        // checks whether the length is cached
        else {
            double pairing = pairing(v, w);
            if (cache.containsKey(pairing)) {
                return cache.get(pairing)[0];
            }

            // computes length and ancestor otherwise
            else {
                int[] computation = computation(v, w);
                cache.put(pairing, computation);
                return computation[0];
            }
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // checks whether v equals to w
        if (v == w) {
            return v;
        }

        // checks whether the length is cached
        else {
            double pairing = pairing(v, w);
            if (cache.containsKey(pairing)) {
                return cache.get(pairing)[1];
            }

            // computes length and ancestor otherwise
            else {
                int[] computation = computation(v, w);
                cache.put(pairing, computation);
                return computation[1];
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // checks whether input is valid
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        // checks whether there is a v that equals to a w
        try {
            for (int i : v) {
                for (int j : w) {
                    if (i == j) {
                        return 0;
                    }
                }
            }
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }


        // checks whether the length is cached
        if (v.equals(lastV) && w.equals(lastW)) {
            return cache2[0];
        }

        // computes length and ancestor otherwise
        else {
            cache2 = computation(v, w);
            lastV = v;
            lastW = w;
            return cache2[0];
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // checks whether input is valid
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        // checks whether there is a v that equals to a w
        try {
            for (int i : v) {
                for (int j : w) {
                    if (i == j) {
                        return i;
                    }
                }
            }
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
        // checks whether the length is cached
        if (v.equals(lastV) && w.equals(lastW)) {
            return cache2[1];
        }

        // computes length and ancestor otherwise
        else {
            cache2 = computation(v, w);
            lastV = v;
            lastW = w;
            return cache2[1];
        }
    }

    // pairing function for v and w
    private double pairing(int v, int w) {
        double x1 = v + w;
        double output = x1 * (x1 + 1) / 2 + w;
        return output;
    }

    // computes length and ancestor of v and w
    private int[] computation(int v, int w) {

        // initiate queues and initial v, w nodes
        Queue<Node> queue = new Queue<Node>();
        Queue<Node> newQueue = new Queue<Node>();
        HashMap<Integer, Node> h = new HashMap<>();
        Node nodeV = new Node();
        nodeV.vlength = 0;
        nodeV.key = v;
        nodeV.root = v;
        queue.enqueue(nodeV);
        Node nodeW = new Node();
        nodeW.wlength = 0;
        nodeW.key = w;
        nodeW.root = w;
        queue.enqueue(nodeW);
        h.put(v, nodeV);
        h.put(w, nodeW);

        // initiates return values and while loop trigger
        int outputLength = Integer.MAX_VALUE;
        int outputAncestor = Integer.MIN_VALUE;

        // main method body
        while (!queue.isEmpty()) {
            while (!queue.isEmpty()) {
                // removes a node from the queue and finds all adjacent vertices
                Node current = queue.dequeue();
                int currentKey = current.key;
                Iterable<Integer> temp = d.adj(currentKey);
                // iterates through all adjacent vertices
                for (int i : temp) {
                    try {
                        // decides picked node's length & checks whether i = v or w
                        int currentRoot = current.root;
                        if (currentRoot == v && i == w && outputLength > current.vlength + 1) {
                            outputLength = current.vlength + 1;
                            outputAncestor = i;
                            continue;
                        }
                        else if (currentRoot == w && i == v && outputLength > current.wlength + 1) {
                            outputLength = current.wlength + 1;
                            outputAncestor = i;
                            continue;
                        }

                        // checks whether pointer has been accessed already
                        int pointerLength, currentLength;
                        if (h.containsKey(i)) {
                            int pointerRoot = h.get(i).root;
                            if (pointerRoot == -1) {
                                if (currentRoot == -1) {
                                    int vRootLength = h.get(i).vlength + current.wlength + 1;
                                    int wRootLength = h.get(i).wlength + current.vlength + 1;
                                    if (outputLength > vRootLength) {
                                        outputLength = vRootLength;
                                        outputAncestor = i;
                                    }
                                    else if (outputLength > wRootLength) {
                                        outputLength = wRootLength;
                                        outputAncestor = i;
                                    }
                                    continue;
                                }
                                else if (currentRoot == v) {
                                    pointerLength = h.get(i).wlength;
                                    currentLength = current.vlength + 1;
                                }
                                else {
                                    pointerLength = h.get(i).vlength;
                                    currentLength = current.wlength + 1;
                                }
                            }
                            else if (pointerRoot != currentRoot) {

                                if (pointerRoot == v) {
                                    pointerLength = h.get(i).vlength;
                                    currentLength = current.wlength + 1;
                                    Node pointer = h.get(i);
                                    pointer.wlength = currentLength;
                                    pointer.root = -1;
                                    h.put(i, pointer);
                                    newQueue.enqueue(pointer);
                                }
                                else {
                                    pointerLength = h.get(i).wlength;
                                    currentLength = current.vlength + 1;
                                    Node pointer = h.get(i);
                                    pointer.vlength = currentLength;
                                    pointer.root = -1;
                                    h.put(i, pointer);
                                    newQueue.enqueue(pointer);
                                }
                            }
                            else if (i != current.prev.key && (h.get(i).vlength > current.vlength
                                    || h.get(i).wlength > current.wlength)) {

                                if (current.root == v) {
                                    Node tempNode = new Node();
                                    tempNode.key = i;
                                    tempNode.prev = current;
                                    tempNode.vlength = current.vlength + 1;
                                    tempNode.root = currentRoot;
                                    newQueue.enqueue(tempNode);
                                    h.put(i, tempNode);
                                }
                                else if (current.root == w) {
                                    Node tempNode = new Node();
                                    tempNode.key = i;
                                    tempNode.prev = current;
                                    tempNode.wlength = current.wlength + 1;
                                    tempNode.root = currentRoot;
                                    newQueue.enqueue(tempNode);
                                    h.put(i, tempNode);
                                }
                                else {
                                    Node tempNode = new Node();
                                    tempNode.key = i;
                                    tempNode.prev = current;
                                    tempNode.wlength = current.wlength + 1;
                                    tempNode.vlength = current.vlength + 1;
                                    tempNode.root = currentRoot;
                                    newQueue.enqueue(tempNode);
                                    h.put(i, tempNode);
                                }
                                continue;
                            }
                            else {
                                continue;
                            }
                        }
                        else if (i != current.prev.key) {
                            if (current.root == v) {
                                Node tempNode = new Node();
                                tempNode.key = i;
                                tempNode.prev = current;
                                tempNode.vlength = current.vlength + 1;
                                tempNode.root = currentRoot;
                                newQueue.enqueue(tempNode);
                                h.put(i, tempNode);
                            }
                            else if (current.root == w) {
                                Node tempNode = new Node();
                                tempNode.key = i;
                                tempNode.prev = current;
                                tempNode.wlength = current.wlength + 1;
                                tempNode.root = currentRoot;
                                newQueue.enqueue(tempNode);
                                h.put(i, tempNode);
                            }
                            else {
                                Node tempNode = new Node();
                                tempNode.key = i;
                                tempNode.prev = current;
                                tempNode.wlength = current.wlength + 1;
                                tempNode.vlength = current.vlength + 1;
                                tempNode.root = currentRoot;
                                newQueue.enqueue(tempNode);
                                h.put(i, tempNode);
                            }
                            continue;
                        }
                        else {
                            continue;
                        }
                        if (outputLength > pointerLength + currentLength) {
                            outputLength = pointerLength + currentLength;
                            outputAncestor = i;
                        }
                    }
                    catch (NullPointerException e) {
                        if (current.root == v) {
                            Node tempNode = new Node();
                            tempNode.key = i;
                            tempNode.prev = current;
                            tempNode.vlength = current.vlength + 1;
                            tempNode.root = current.root;
                            newQueue.enqueue(tempNode);
                            h.put(i, tempNode);
                        }
                        else if (current.root == w) {
                            Node tempNode = new Node();
                            tempNode.key = i;
                            tempNode.prev = current;
                            tempNode.wlength = current.wlength + 1;
                            tempNode.root = current.root;
                            newQueue.enqueue(tempNode);
                            h.put(i, tempNode);
                        }
                        else {
                            Node tempNode = new Node();
                            tempNode.key = i;
                            tempNode.prev = current;
                            tempNode.wlength = current.wlength + 1;
                            tempNode.vlength = current.vlength + 1;
                            tempNode.root = current.root;
                            newQueue.enqueue(tempNode);
                            h.put(i, tempNode);
                        }
                    }
                }
            }
            queue = newQueue;
            newQueue = new Queue<Node>();
        }
        int[] output;
        if (outputLength == Integer.MAX_VALUE) {
            output = new int[] { -1, -1 };
        }
        else {
            output = new int[] { outputLength, outputAncestor };
        }
        return output;
    }

    private int[] computation(Iterable<Integer> v, Iterable<Integer> w) {

        // initiate queues and initial v, w nodes
        Queue<Node> queue = new Queue<Node>();
        Queue<Node> newQueue = new Queue<Node>();
        HashMap<Integer, Node> h = new HashMap<>();
        HashMap<Integer, Node> originalV = new HashMap<>();
        HashMap<Integer, Node> originalW = new HashMap<>();
        for (int i : v) {
            Node node = new Node();
            node.vlength = 0;
            node.key = i;
            node.root = -2;
            queue.enqueue(node);
            h.put(i, node);
            originalV.put(i, node);
        }
        for (int i : w) {
            Node node = new Node();
            node.wlength = 0;
            node.key = i;
            node.root = -3;
            queue.enqueue(node);
            h.put(i, node);
            originalW.put(i, node);
        }


        // initiates return values and while loop trigger
        int outputLength = Integer.MAX_VALUE;
        int outputAncestor = Integer.MIN_VALUE;

        // main method body
        while (!queue.isEmpty()) {
            while (!queue.isEmpty()) {
                // removes a node from the queue and finds all adjacent vertices
                Node current = queue.dequeue();
                int currentKey = current.key;
                Iterable<Integer> temp = d.adj(currentKey);
                // iterates through all adjacent vertices
                for (int i : temp) {
                    try {
                        // decides picked node's length & checks whether i = v or w
                        int currentRoot = current.root;
                        if (currentRoot == -2 && originalW.containsKey(i)
                                && outputLength > current.vlength + 1) {
                            outputLength = current.vlength + 1;
                            outputAncestor = i;
                            continue;
                        }
                        else if (currentRoot == -3 && originalV.containsKey(i)
                                && outputLength > current.wlength + 1) {
                            outputLength = current.wlength + 1;
                            outputAncestor = i;
                            continue;
                        }

                        // checks whether pointer has been accessed already
                        int pointerLength, currentLength;
                        if (h.containsKey(i)) {
                            int pointerRoot = h.get(i).root;
                            if (pointerRoot == -1) {
                                if (currentRoot == -1) {
                                    int vRootLength = h.get(i).vlength + current.wlength + 1;
                                    int wRootLength = h.get(i).wlength + current.vlength + 1;
                                    if (outputLength > vRootLength) {
                                        outputLength = vRootLength;
                                        outputAncestor = i;
                                    }
                                    else if (outputLength > wRootLength) {
                                        outputLength = wRootLength;
                                        outputAncestor = i;
                                    }
                                    continue;
                                }
                                else if (currentRoot == -2) {
                                    pointerLength = h.get(i).wlength;
                                    currentLength = current.vlength + 1;
                                }
                                else {
                                    pointerLength = h.get(i).vlength;
                                    currentLength = current.wlength + 1;
                                }
                            }
                            else if (pointerRoot != currentRoot) {
                                if (pointerRoot == -2) {
                                    pointerLength = h.get(i).vlength;
                                    currentLength = current.wlength + 1;
                                    Node pointer = h.get(i);
                                    pointer.wlength = currentLength;
                                    pointer.root = -1;
                                    h.put(i, pointer);
                                    newQueue.enqueue(pointer);
                                }
                                else {
                                    pointerLength = h.get(i).wlength;
                                    currentLength = current.vlength + 1;
                                    Node pointer = h.get(i);
                                    pointer.vlength = currentLength;
                                    pointer.root = -1;
                                    h.put(i, pointer);
                                    newQueue.enqueue(pointer);
                                }
                            }
                            else if (i != current.prev.key && (h.get(i).vlength > current.vlength
                                    || h.get(i).wlength > current.wlength)) {

                                if (currentRoot == -2) {
                                    Node tempNode = new Node();
                                    tempNode.key = i;
                                    tempNode.prev = current;
                                    tempNode.vlength = current.vlength + 1;
                                    tempNode.root = currentRoot;
                                    newQueue.enqueue(tempNode);
                                    h.put(i, tempNode);
                                }
                                else if (currentRoot == -3) {
                                    Node tempNode = new Node();
                                    tempNode.key = i;
                                    tempNode.prev = current;
                                    tempNode.wlength = current.wlength + 1;
                                    tempNode.root = currentRoot;
                                    newQueue.enqueue(tempNode);
                                    h.put(i, tempNode);
                                }
                                else {
                                    Node tempNode = new Node();
                                    tempNode.key = i;
                                    tempNode.prev = current;
                                    tempNode.wlength = current.wlength + 1;
                                    tempNode.vlength = current.vlength + 1;
                                    tempNode.root = currentRoot;
                                    newQueue.enqueue(tempNode);
                                    h.put(i, tempNode);
                                }
                                continue;
                            }
                            else {
                                continue;
                            }
                        }
                        else if (i != current.prev.key) {
                            if (currentRoot == -2) {
                                Node tempNode = new Node();
                                tempNode.key = i;
                                tempNode.prev = current;
                                tempNode.vlength = current.vlength + 1;
                                tempNode.root = currentRoot;
                                newQueue.enqueue(tempNode);
                                h.put(i, tempNode);
                            }
                            else if (currentRoot == -3) {
                                Node tempNode = new Node();
                                tempNode.key = i;
                                tempNode.prev = current;
                                tempNode.wlength = current.wlength + 1;
                                tempNode.root = currentRoot;
                                newQueue.enqueue(tempNode);
                                h.put(i, tempNode);
                            }
                            else {
                                Node tempNode = new Node();
                                tempNode.key = i;
                                tempNode.prev = current;
                                tempNode.wlength = current.wlength + 1;
                                tempNode.vlength = current.vlength + 1;
                                tempNode.root = currentRoot;
                                newQueue.enqueue(tempNode);
                                h.put(i, tempNode);
                            }
                            continue;
                        }
                        else {
                            continue;
                        }
                        if (outputLength > pointerLength + currentLength) {
                            outputLength = pointerLength + currentLength;
                            outputAncestor = i;
                        }
                    }
                    catch (NullPointerException e) {
                        if (current.root == -2) {
                            Node tempNode = new Node();
                            tempNode.key = i;
                            tempNode.prev = current;
                            tempNode.vlength = current.vlength + 1;
                            tempNode.root = current.root;
                            newQueue.enqueue(tempNode);
                            h.put(i, tempNode);
                        }
                        else if (current.root == -3) {
                            Node tempNode = new Node();
                            tempNode.key = i;
                            tempNode.prev = current;
                            tempNode.wlength = current.wlength + 1;
                            tempNode.root = current.root;
                            newQueue.enqueue(tempNode);
                            h.put(i, tempNode);
                        }
                        else {
                            Node tempNode = new Node();
                            tempNode.key = i;
                            tempNode.prev = current;
                            tempNode.wlength = current.wlength + 1;
                            tempNode.vlength = current.vlength + 1;
                            tempNode.root = current.root;
                            newQueue.enqueue(tempNode);
                            h.put(i, tempNode);
                        }
                    }
                }
            }
            queue = newQueue;
            newQueue = new Queue<Node>();
        }
        int[] output;
        if (outputLength == Integer.MAX_VALUE) {
            output = new int[] { -1, -1 };
        }
        else {
            output = new int[] { outputLength, outputAncestor };
        }
        return output;
    }
}

