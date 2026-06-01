import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class Main {

    public static void main(String[] args) {

        int T = StdIn.readInt();

        for (int caso = 0; caso < T; caso++) {

            int N = StdIn.readInt();
            int M = StdIn.readInt();
            int s = StdIn.readInt();
            int t = StdIn.readInt();
            int p = StdIn.readInt();

            EdgeWeightedDigraph grafo = new EdgeWeightedDigraph(N + 1);
            EdgeWeightedDigraph reverso = new EdgeWeightedDigraph(N + 1);

            ArrayList<DirectedEdge> arestas = new ArrayList<>();

            for (int i = 0; i < M; i++) {
                int u = StdIn.readInt();
                int v = StdIn.readInt();
                int c = StdIn.readInt();

                DirectedEdge e = new DirectedEdge(u, v, c);

                grafo.addEdge(e);
                reverso.addEdge(new DirectedEdge(v, u, c));

                arestas.add(e);
            }

            DijkstraSP caminhoSaindoDeS = new DijkstraSP(grafo, s);
            DijkstraSP caminhoAteT = new DijkstraSP(reverso, t);

            int resposta = -1;

            for (DirectedEdge e : arestas) {
                int u = e.from();
                int v = e.to();
                int c = (int) e.weight();

                if (caminhoSaindoDeS.hasPathTo(u) && caminhoAteT.hasPathTo(v)) {
                    double custoTotal = caminhoSaindoDeS.distTo(u) + c + caminhoAteT.distTo(v);

                    if (custoTotal <= p) {
                        resposta = Math.max(resposta, c);
                    }
                }
            }

            StdOut.println(resposta);
        }
    }


    public static class Bag<Item> implements Iterable<Item> {
        private Node<Item> first;
        private int n;

        private static class Node<Item> {
            private Item item;
            private Node<Item> next;
        }

        public Bag() {
            first = null;
            n = 0;
        }

        public boolean isEmpty() {
            return first == null;
        }

        public int size() {
            return n;
        }

        public void add(Item item) {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
            n++;
        }

        public Iterator<Item> iterator()  {
            return new LinkedIterator(first);
        }

        private class LinkedIterator implements Iterator<Item> {
            private Node<Item> current;

            public LinkedIterator(Node<Item> first) {
                current = first;
            }

            public boolean hasNext()  {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }
        }

    }

    public static class DijkstraSP {
        private double[] distTo;
        private DirectedEdge[] edgeTo;
        private IndexMinPQ<Double> pq;

        public DijkstraSP(EdgeWeightedDigraph digraph, int s) {
            for (DirectedEdge e : digraph.edges()) {
                if (e.weight() < 0)
                    throw new IllegalArgumentException("edge " + e + " has negative weight");
            }

            distTo = new double[digraph.V()];
            edgeTo = new DirectedEdge[digraph.V()];

            validateVertex(s);

            for (int v = 0; v < digraph.V(); v++)
                distTo[v] = Double.POSITIVE_INFINITY;
            distTo[s] = 0.0;

            pq = new IndexMinPQ<Double>(digraph.V());
            pq.insert(s, distTo[s]);
            while (!pq.isEmpty()) {
                int v = pq.delMin();
                for (DirectedEdge e : digraph.adj(v))
                    relax(e);
            }

            assert check(digraph, s);
        }

        private void relax(DirectedEdge e) {
            int v = e.from(), w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }

        public double distTo(int v) {
            validateVertex(v);
            return distTo[v];
        }

        public boolean hasPathTo(int v) {
            validateVertex(v);
            return distTo[v] < Double.POSITIVE_INFINITY;
        }

        public Iterable<DirectedEdge> pathTo(int v) {
            validateVertex(v);
            if (!hasPathTo(v)) return null;
            Stack<DirectedEdge> path = new Stack<DirectedEdge>();
            for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
                path.push(e);
            }
            return path;
        }

        private boolean check(EdgeWeightedDigraph digraph, int s) {

            for (DirectedEdge e : digraph.edges()) {
                if (e.weight() < 0) {
                    System.err.println("negative edge weight detected");
                    return false;
                }
            }

            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (int v = 0; v < digraph.V(); v++) {
                if (v == s) continue;
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent");
                    return false;
                }
            }
            for (int v = 0; v < digraph.V(); v++) {
                for (DirectedEdge e : digraph.adj(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }
            for (int w = 0; w < digraph.V(); w++) {
                if (edgeTo[w] == null) continue;
                DirectedEdge e = edgeTo[w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[v] + e.weight() != distTo[w]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
            return true;
        }

        private void validateVertex(int v) {
            int V = distTo.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }

    }

    public static class DirectedEdge {
        private final int v;
        private final int w;
        private final double weight;

        public DirectedEdge(int v, int w, double weight) {
            if (v < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
            if (w < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
            if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        public int from() {
            return v;
        }

        public int to() {
            return w;
        }

        public double weight() {
            return weight;
        }

        public String toString() {
            return v + "->" + w + " " + String.format("%5.2f", weight);
        }

    }

    public static class EdgeWeightedDigraph {
        private static final String NEWLINE = System.getProperty("line.separator");
        private final int V;
        private int E;
        private Bag<DirectedEdge>[] adj;
        private int[] indegree;

        public EdgeWeightedDigraph(int V) {
            if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be non-negative");
            this.V = V;
            this.E = 0;
            this.indegree = new int[V];
            adj = (Bag<DirectedEdge>[]) new Bag[V];
            for (int v = 0; v < V; v++)
                adj[v] = new Bag<DirectedEdge>();
        }

        public int V() {
            return V;
        }

        public int E() {
            return E;
        }

        private void validateVertex(int v) {
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }

        public void addEdge(DirectedEdge e) {
            int v = e.from();
            int w = e.to();
            validateVertex(v);
            validateVertex(w);
            adj[v].add(e);
            indegree[w]++;
            E++;
        }

        public Iterable<DirectedEdge> adj(int v) {
            validateVertex(v);
            return adj[v];
        }

        public int outdegree(int v) {
            validateVertex(v);
            return adj[v].size();
        }

        public int indegree(int v) {
            validateVertex(v);
            return indegree[v];
        }

        public Iterable<DirectedEdge> edges() {
            Bag<DirectedEdge> list = new Bag<DirectedEdge>();
            for (int v = 0; v < V; v++) {
                for (DirectedEdge e : adj(v)) {
                    list.add(e);
                }
            }
            return list;
        }

        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(V + " " + E + NEWLINE);
            for (int v = 0; v < V; v++) {
                s.append(v + ": ");
                for (DirectedEdge e : adj[v]) {
                    s.append(e + "  ");
                }
                s.append(NEWLINE);
            }
            return s.toString();
        }

        public String toDot() {
            StringBuilder s = new StringBuilder();
            s.append("digraph {" + NEWLINE);
            s.append("node[shape=circle, style=filled, fixedsize=true, width=0.3, fontsize=\"10pt\"]" + NEWLINE);
            s.append("edge[arrowhead=normal, fontsize=\"9pt\"]" + NEWLINE);
            for (int v = 0; v < V; v++) {
                for (DirectedEdge e : adj[v]) {
                    int w = e.to();
                    s.append(v + " -> " + w + " [label=\"" + e.weight() + "\"]" + NEWLINE);
                }
            }
            s.append("}" + NEWLINE);
            return s.toString();
        }
    }

    public static final class In {

        private static final String CHARSET_NAME = "UTF-8";

        private static final Locale LOCALE = Locale.US;

        private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");

        private static final Pattern EMPTY_PATTERN = Pattern.compile("");

        private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

        private Scanner scanner;

        public In() {
            scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }

        public In(String name) {
            if (name == null) throw new IllegalArgumentException("argument is null");
            if (name.length() == 0) throw new IllegalArgumentException("argument is the empty string");
            try {
                File file = new File(name);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
                    scanner.useLocale(LOCALE);
                    return;
                }

                URL url = getClass().getResource(name);

                if (url == null) {
                    url = getClass().getClassLoader().getResource(name);
                }

                if (url == null) {
                    URI uri = new URI(name);
                    if (uri.isAbsolute()) url = uri.toURL();
                    else throw new IllegalArgumentException("could not read: '" + name + "'");
                    url = new URL(name);
                }

                URLConnection site = url.openConnection();

                InputStream is     = site.getInputStream();
                scanner            = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
                scanner.useLocale(LOCALE);
            }
            catch (IOException | URISyntaxException e) {
                throw new IllegalArgumentException("could not read: '" + name + "'");
            }
        }

        public boolean exists()  {
            return scanner != null;
        }

        public boolean isEmpty() {
            return !scanner.hasNext();
        }

        public boolean hasNextLine() {
            return scanner.hasNextLine();
        }

        public boolean hasNextChar() {
            scanner.useDelimiter(EMPTY_PATTERN);
            boolean result = scanner.hasNext();
            scanner.useDelimiter(WHITESPACE_PATTERN);
            return result;
        }

        public String readLine() {
            String line;
            try {
                line = scanner.nextLine();
            }
            catch (NoSuchElementException e) {
                line = null;
            }
            return line;
        }

        public char readChar() {
            scanner.useDelimiter(EMPTY_PATTERN);
            try {
                String ch = scanner.next();
                assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
                        + " Please contact the authors.";
                scanner.useDelimiter(WHITESPACE_PATTERN);
                return ch.charAt(0);
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'char' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public String readAll() {
            if (!scanner.hasNextLine())
                return "";
            String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
            scanner.useDelimiter(WHITESPACE_PATTERN);
            return result;
        }

        public String readString() {
            try {
                return scanner.next();
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'String' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public int readInt() {
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read an 'int' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read an 'int' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public double readDouble() {
            try {
                return scanner.nextDouble();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'double' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read a 'double' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public float readFloat() {
            try {
                return scanner.nextFloat();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'float' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read a 'float' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public long readLong() {
            try {
                return scanner.nextLong();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'long' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read a 'long' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public short readShort() {
            try {
                return scanner.nextShort();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'short' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read a 'short' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public byte readByte() {
            try {
                return scanner.nextByte();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'byte' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read a 'byte' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public boolean readBoolean() {
            try {
                String token = readString();
                if ("true".equalsIgnoreCase(token))  return true;
                if ("false".equalsIgnoreCase(token)) return false;
                if ("1".equals(token))               return true;
                if ("0".equals(token))               return false;
                throw new InputMismatchException("attempts to read a 'boolean' value from the input stream, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'boolean' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        public String[] readAllStrings() {
            String[] tokens = WHITESPACE_PATTERN.split(readAll());
            if (tokens.length == 0 || tokens[0].length() > 0)
                return tokens;
            String[] decapitokens = new String[tokens.length-1];
            for (int i = 0; i < tokens.length-1; i++)
                decapitokens[i] = tokens[i+1];
            return decapitokens;
        }

        public String[] readAllLines() {
            ArrayList<String> lines = new ArrayList<String>();
            while (hasNextLine()) {
                lines.add(readLine());
            }
            return lines.toArray(new String[0]);
        }

        public int[] readAllInts() {
            String[] fields = readAllStrings();
            int[] vals = new int[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Integer.parseInt(fields[i]);
            return vals;
        }

        public long[] readAllLongs() {
            String[] fields = readAllStrings();
            long[] vals = new long[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Long.parseLong(fields[i]);
            return vals;
        }

        public double[] readAllDoubles() {
            String[] fields = readAllStrings();
            double[] vals = new double[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Double.parseDouble(fields[i]);
            return vals;
        }

        public void close() {
            scanner.close();
        }

    }

    public static class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
        private int maxN;
        private int n;
        private int[] pq;
        private int[] qp;
        private Key[] keys;

        public IndexMinPQ(int maxN) {
            if (maxN < 0) throw new IllegalArgumentException();
            this.maxN = maxN;
            n = 0;
            keys = (Key[]) new Comparable[maxN + 1];
            pq   = new int[maxN + 1];
            qp   = new int[maxN + 1];
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }

        public boolean isEmpty() {
            return n == 0;
        }

        public boolean contains(int i) {
            validateIndex(i);
            return qp[i] != -1;
        }

        public int size() {
            return n;
        }

        public void insert(int i, Key key) {
            validateIndex(i);
            if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
            n++;
            qp[i] = n;
            pq[n] = i;
            keys[i] = key;
            swim(n);
        }

        public int minIndex() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        public Key minKey() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return keys[pq[1]];
        }

        public int delMin() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            int min = pq[1];
            exch(1, n--);
            sink(1);
            assert min == pq[n+1];
            qp[min] = -1;
            keys[min] = null;
            pq[n+1] = -1;
            return min;
        }

        public Key keyOf(int i) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            else return keys[i];
        }

        public void changeKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            keys[i] = key;
            swim(qp[i]);
            sink(qp[i]);
        }

        public void decreaseKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) == 0)
                throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
            if (keys[i].compareTo(key) < 0)
                throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
            keys[i] = key;
            swim(qp[i]);
        }

        public void increaseKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) == 0)
                throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
            if (keys[i].compareTo(key) > 0)
                throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");
            keys[i] = key;
            sink(qp[i]);
        }

        public void delete(int i) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            int index = qp[i];
            exch(index, n--);
            swim(index);
            sink(index);
            keys[i] = null;
            qp[i] = -1;
        }

        private void validateIndex(int i) {
            if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
            if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
        }

        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }

        private void swim(int k) {
            while (k > 1 && greater(k/2, k)) {
                exch(k, k/2);
                k = k/2;
            }
        }

        private void sink(int k) {
            while (2*k <= n) {
                int j = 2*k;
                if (j < n && greater(j, j+1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
            }
        }

        public Iterator<Integer> iterator() { return new HeapIterator(); }

        private class HeapIterator implements Iterator<Integer> {

            private IndexMinPQ<Key> copy;
            public HeapIterator() {
                copy = new IndexMinPQ<Key>(pq.length - 1);
                for (int i = 1; i <= n; i++)
                    copy.insert(pq[i], keys[pq[i]]);
            }

            public boolean hasNext()  { return !copy.isEmpty();                     }
            public void remove()      { throw new UnsupportedOperationException();  }

            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return copy.delMin();
            }
        }
    }

    public static class Stack<Item> implements Iterable<Item> {
        private Node<Item> first;
        private int n;

        private static class Node<Item> {
            private Item item;
            private Node<Item> next;
        }

        public Stack() {
            first = null;
            n = 0;
        }

        public boolean isEmpty() {
            return first == null;
        }

        public int size() {
            return n;
        }

        public void push(Item item) {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
            n++;
        }

        public Item pop() {
            if (isEmpty()) throw new NoSuchElementException("Stack underflow");
            Item item = first.item;
            first = first.next;
            n--;
            return item;
        }

        public Item peek() {
            if (isEmpty()) throw new NoSuchElementException("Stack underflow");
            return first.item;
        }

        public String toString() {
            StringBuilder s = new StringBuilder();
            for (Item item : this) {
                s.append(item);
                s.append(' ');
            }
            return s.toString();
        }

        public Iterator<Item> iterator() {
            return new LinkedIterator(first);
        }

        private class LinkedIterator implements Iterator<Item> {
            private Node<Item> current;

            public LinkedIterator(Node<Item> first) {
                current = first;
            }

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }
        }
    }

    public static final class StdIn {

        private static final String CHARSET_NAME = "UTF-8";
        private static final Locale LOCALE = Locale.US;
        private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");
        private static final Pattern EMPTY_PATTERN = Pattern.compile("");
        private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");
        private static Scanner scanner;
        private StdIn() { }

        public static boolean isEmpty() {
            return !scanner.hasNext();
        }

        public static boolean hasNextLine() {
            return scanner.hasNextLine();
        }

        public static boolean hasNextChar() {
            scanner.useDelimiter(EMPTY_PATTERN);
            boolean result = scanner.hasNext();
            scanner.useDelimiter(WHITESPACE_PATTERN);
            return result;
        }

        public static String readLine() {
            String line;
            try {
                line = scanner.nextLine();
            }
            catch (NoSuchElementException e) {
                line = null;
            }
            return line;
        }

        public static char readChar() {
            try {
                scanner.useDelimiter(EMPTY_PATTERN);
                String ch = scanner.next();
                assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
                        + " Please contact the authors.";
                scanner.useDelimiter(WHITESPACE_PATTERN);
                return ch.charAt(0);
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'char' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        public static String readAll() {
            if (!scanner.hasNextLine())
                return "";

            String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
            scanner.useDelimiter(WHITESPACE_PATTERN);
            return result;
        }

        public static String readString() {
            try {
                return scanner.next();
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'String' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        public static int readInt() {
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read an 'int' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attemps to read an 'int' value from standard input, "
                        + "but no more tokens are available");
            }

        }

        public static double readDouble() {
            try {
                return scanner.nextDouble();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'double' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'double' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        public static float readFloat() {
            try {
                return scanner.nextFloat();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'float' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'float' value from standard input, "
                        + "but there no more tokens are available");
            }
        }

        public static long readLong() {
            try {
                return scanner.nextLong();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'long' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'long' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        public static short readShort() {
            try {
                return scanner.nextShort();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'short' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'short' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        public static byte readByte() {
            try {
                return scanner.nextByte();
            }
            catch (InputMismatchException e) {
                String token = scanner.next();
                throw new InputMismatchException("attempts to read a 'byte' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'byte' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        public static boolean readBoolean() {
            try {
                String token = readString();
                if ("true".equalsIgnoreCase(token))  return true;
                if ("false".equalsIgnoreCase(token)) return false;
                if ("1".equals(token))               return true;
                if ("0".equals(token))               return false;
                throw new InputMismatchException("attempts to read a 'boolean' value from standard input, "
                        + "but the next token is \"" + token + "\"");
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'boolean' value from standard input, "
                        + "but no more tokens are available");
            }

        }

        public static String[] readAllStrings() {
            String[] tokens = WHITESPACE_PATTERN.split(readAll());
            if (tokens.length == 0 || tokens[0].length() > 0)
                return tokens;
            String[] decapitokens = new String[tokens.length-1];
            for (int i = 0; i < tokens.length - 1; i++)
                decapitokens[i] = tokens[i+1];
            return decapitokens;
        }

        public static String[] readAllLines() {
            ArrayList<String> lines = new ArrayList<String>();
            while (hasNextLine()) {
                lines.add(readLine());
            }
            return lines.toArray(new String[0]);
        }

        public static int[] readAllInts() {
            String[] fields = readAllStrings();
            int[] vals = new int[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Integer.parseInt(fields[i]);
            return vals;
        }

        public static long[] readAllLongs() {
            String[] fields = readAllStrings();
            long[] vals = new long[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Long.parseLong(fields[i]);
            return vals;
        }

        public static double[] readAllDoubles() {
            String[] fields = readAllStrings();
            double[] vals = new double[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Double.parseDouble(fields[i]);
            return vals;
        }

        static {
            resync();
        }

        private static void resync() {
            setScanner(new Scanner(new java.io.BufferedInputStream(System.in), CHARSET_NAME));
        }

        private static void setScanner(Scanner scanner) {
            StdIn.scanner = scanner;
            StdIn.scanner.useLocale(LOCALE);
        }
    }

    public static final class StdOut {

        private static final Charset CHARSET = StandardCharsets.UTF_8;
        private static final Locale LOCALE = Locale.US;
        private static PrintWriter out;
        static {
            out = new PrintWriter(new OutputStreamWriter(System.out, CHARSET), true);
        }
        private StdOut() { }

        public static void println() {
            out.println();
        }

        public static void println(Object x) {
            out.println(x);
        }

        public static void println(boolean x) {
            out.println(x);
        }

        public static void println(char x) {
            out.println(x);
        }

        public static void println(double x) {
            out.println(x);
        }

        public static void println(float x) {
            out.println(x);
        }

        public static void println(int x) {
            out.println(x);
        }

        public static void println(long x) {
            out.println(x);
        }

        public static void println(short x) {
            out.println(x);
        }

        public static void println(byte x) {
            out.println(x);
        }

        public static void print() {
            out.flush();
        }

        public static void print(Object x) {
            out.print(x);
            out.flush();
        }

        public static void print(boolean x) {
            out.print(x);
            out.flush();
        }

        public static void print(char x) {
            out.print(x);
            out.flush();
        }

        public static void print(double x) {
            out.print(x);
            out.flush();
        }

        public static void print(float x) {
            out.print(x);
            out.flush();
        }

        public static void print(int x) {
            out.print(x);
            out.flush();
        }

        public static void print(long x) {
            out.print(x);
            out.flush();
        }

        public static void print(short x) {
            out.print(x);
            out.flush();
        }

        public static void print(byte x) {
            out.print(x);
            out.flush();
        }

        public static void printf(String format, Object... args) {
            out.printf(LOCALE, format, args);
            out.flush();
        }

        public static void printf(Locale locale, String format, Object... args) {
            out.printf(locale, format, args);
            out.flush();
        }

    }
}
