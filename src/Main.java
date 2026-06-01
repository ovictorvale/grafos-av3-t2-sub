import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
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
import java.util.Random;
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

        public static void main(String[] args) {
            Bag<String> bag = new Bag<String>();
            while (!StdIn.isEmpty()) {
                String item = StdIn.readString();
                bag.add(item);
            }

            StdOut.println("size of bag = " + bag.size());
            for (String s : bag) {
                StdOut.println(s);
            }
        }

    }

    public final class BinaryStdIn {
        private static final int EOF = -1;

        private static BufferedInputStream in;
        private static int buffer;
        private static int n;
        private static boolean isInitialized;

        private BinaryStdIn() { }

        private static void initialize() {
            in = new BufferedInputStream(System.in);
            buffer = 0;
            n = 0;
            fillBuffer();
            isInitialized = true;
        }

        private static void fillBuffer() {
            try {
                buffer = in.read();
                n = 8;
            }
            catch (IOException e) {
                System.out.println("EOF");
                buffer = EOF;
                n = -1;
            }
        }

        public static void close() {
            if (!isInitialized) initialize();
            try {
                in.close();
                isInitialized = false;
            }
            catch (IOException ioe) {
                throw new IllegalStateException("Could not close BinaryStdIn", ioe);
            }
        }

        public static boolean isEmpty() {
            if (!isInitialized) initialize();
            return buffer == EOF;
        }

        public static boolean readBoolean() {
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
            n--;
            boolean bit = ((buffer >> n) & 1) == 1;
            if (n == 0) fillBuffer();
            return bit;
        }
        public static char readChar() {
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

            if (n == 8) {
                int x = buffer;
                fillBuffer();
                return (char) (x & 0xff);
            }


            int x = buffer;
            x <<= (8 - n);
            int oldN = n;
            fillBuffer();
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
            n = oldN;
            x |= (buffer >>> n);
            return (char) (x & 0xff);

        }
        public static char readChar(int r) {
            if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value of r = " + r);

            if (r == 8) return readChar();

            char x = 0;
            for (int i = 0; i < r; i++) {
                x <<= 1;
                boolean bit = readBoolean();
                if (bit) x |= 1;
            }
            return x;
        }
        public static String readString() {
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

            StringBuilder sb = new StringBuilder();
            while (!isEmpty()) {
                char c = readChar();
                sb.append(c);
            }
            return sb.toString();
        }
        public static short readShort() {
            short x = 0;
            for (int i = 0; i < 2; i++) {
                char c = readChar();
                x <<= 8;
                x |= c;
            }
            return x;
        }
        public static int readInt() {
            int x = 0;
            for (int i = 0; i < 4; i++) {
                char c = readChar();
                x <<= 8;
                x |= c;
            }
            return x;
        }
        public static int readInt(int r) {
            if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value of r = " + r);

            if (r == 32) return readInt();

            int x = 0;
            for (int i = 0; i < r; i++) {
                x <<= 1;
                boolean bit = readBoolean();
                if (bit) x |= 1;
            }
            return x;
        }
        public static long readLong() {
            long x = 0;
            for (int i = 0; i < 8; i++) {
                char c = readChar();
                x <<= 8;
                x |= c;
            }
            return x;
        }
        public static double readDouble() {
            return Double.longBitsToDouble(readLong());
        }
        public static float readFloat() {
            return Float.intBitsToFloat(readInt());
        }
        public static byte readByte() {
            char c = readChar();
            return (byte) (c & 0xff);
        }
        public static void main(String[] args) {
            while (!BinaryStdIn.isEmpty()) {
                char c = BinaryStdIn.readChar();
                BinaryStdOut.write(c);
            }
            BinaryStdOut.flush();
        }
    }

    public final class BinaryStdOut {
        private static BufferedOutputStream out;
        private static int buffer;
        private static int n;
        private static boolean isInitialized;

        private BinaryStdOut() { }

        private static void initialize() {
            out = new BufferedOutputStream(System.out);
            buffer = 0;
            n = 0;
            isInitialized = true;
        }
        private static void writeBit(boolean bit) {
            if (!isInitialized) initialize();

            buffer <<= 1;
            if (bit) buffer |= 1;

            n++;
            if (n == 8) clearBuffer();
        }
        private static void writeByte(int x) {
            if (!isInitialized) initialize();

            assert x >= 0 && x < 256;
            if (n == 0) {
                try {
                    out.write(x);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }


            for (int i = 0; i < 8; i++) {
                boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
                writeBit(bit);
            }
        }
        private static void clearBuffer() {
            if (!isInitialized) initialize();

            if (n == 0) return;
            if (n > 0) buffer <<= (8 - n);
            try {
                out.write(buffer);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            n = 0;
            buffer = 0;
        }
        public static void flush() {
            clearBuffer();
            try {
                out.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void close() {
            flush();
            try {
                out.close();
                isInitialized = false;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void write(boolean x) {
            writeBit(x);
        }
        public static void write(byte x) {
            writeByte(x & 0xff);
        }
        public static void write(int x) {
            writeByte((x >>> 24) & 0xff);
            writeByte((x >>> 16) & 0xff);
            writeByte((x >>>  8) & 0xff);
            writeByte((x >>>  0) & 0xff);
        }
        public static void write(int x, int r) {
            if (r == 32) {
                write(x);
                return;
            }
            if (r < 1 || r > 32)        throw new IllegalArgumentException("Illegal value for r = " + r);
            if (x < 0 || x >= (1 << r)) throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
            for (int i = 0; i < r; i++) {
                boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
                writeBit(bit);
            }
        }
        public static void write(double x) {
            write(Double.doubleToRawLongBits(x));
        }
        public static void write(long x) {
            writeByte((int) ((x >>> 56) & 0xff));
            writeByte((int) ((x >>> 48) & 0xff));
            writeByte((int) ((x >>> 40) & 0xff));
            writeByte((int) ((x >>> 32) & 0xff));
            writeByte((int) ((x >>> 24) & 0xff));
            writeByte((int) ((x >>> 16) & 0xff));
            writeByte((int) ((x >>>  8) & 0xff));
            writeByte((int) ((x >>>  0) & 0xff));
        }
        public static void write(float x) {
            write(Float.floatToRawIntBits(x));
        }
        public static void write(short x) {
            writeByte((x >>>  8) & 0xff);
            writeByte((x >>>  0) & 0xff);
        }
        public static void write(char x) {
            if (x < 0 || x >= 256) throw new IllegalArgumentException("Illegal 8-bit char = " + x);
            writeByte(x);
        }
        public static void write(char x, int r) {
            if (r == 8) {
                write(x);
                return;
            }
            if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value for r = " + r);
            if (x >= (1 << r))   throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
            for (int i = 0; i < r; i++) {
                boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
                writeBit(bit);
            }
        }
        public static void write(String s) {
            for (int i = 0; i < s.length(); i++)
                write(s.charAt(i));
        }
        public static void write(String s, int r) {
            for (int i = 0; i < s.length(); i++)
                write(s.charAt(i), r);
        }
        public static void main(String[] args) {
            int m = Integer.parseInt(args[0]);

            for (int i = 0; i < m; i++) {
                BinaryStdOut.write(i);
            }
            BinaryStdOut.flush();
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
        public static void main(String[] args) {
            In in = new In(args[0]);
            EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(in);
            int s = Integer.parseInt(args[1]);

            DijkstraSP sp = new DijkstraSP(digraph, s);


            for (int t = 0; t < digraph.V(); t++) {
                if (sp.hasPathTo(t)) {
                    StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                    for (DirectedEdge e : sp.pathTo(t)) {
                        StdOut.print(e + "   ");
                    }
                    StdOut.println();
                }
                else {
                    StdOut.printf("%d to %d         no path\n", s, t);
                }
            }
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
        public static void main(String[] args) {
            DirectedEdge e = new DirectedEdge(12, 34, 5.67);
            StdOut.println(e);
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

        public EdgeWeightedDigraph(int V, int E) {
            this(V);
            if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be non-negative");
            for (int i = 0; i < E; i++) {
                int v = StdRandom.uniformInt(V);
                int w = StdRandom.uniformInt(V);
                double weight = 0.01 * StdRandom.uniformInt(100);
                DirectedEdge e = new DirectedEdge(v, w, weight);
                addEdge(e);
            }
        }
        public EdgeWeightedDigraph(In in) {
            if (in == null) throw new IllegalArgumentException("argument is null");
            try {
                this.V = in.readInt();
                if (V < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be non-negative");
                indegree = new int[V];
                adj = (Bag<DirectedEdge>[]) new Bag[V];
                for (int v = 0; v < V; v++) {
                    adj[v] = new Bag<DirectedEdge>();
                }

                int E = in.readInt();
                if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
                for (int i = 0; i < E; i++) {
                    int v = in.readInt();
                    int w = in.readInt();
                    validateVertex(v);
                    validateVertex(w);
                    double weight = in.readDouble();
                    addEdge(new DirectedEdge(v, w, weight));
                }
            }
            catch (NoSuchElementException e) {
                throw new IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e);
            }
        }
        public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
            this(G.V());
            this.E = G.E();
            for (int v = 0; v < G.V(); v++)
                this.indegree[v] = G.indegree(v);
            for (int v = 0; v < G.V(); v++) {
                Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
                for (DirectedEdge e : G.adj[v]) {
                    reverse.push(e);
                }
                for (DirectedEdge e : reverse) {
                    adj[v].add(e);
                }
            }
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

        public static void main(String[] args) {
            In in = new In(args[0]);
            EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
            StdOut.println(G);
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

        public In(Socket socket) {
            if (socket == null) throw new IllegalArgumentException("socket argument is null");
            try {
                InputStream is = socket.getInputStream();
                scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
                scanner.useLocale(LOCALE);
            }
            catch (IOException ioe) {
                throw new IllegalArgumentException("could not open socket: " + socket, ioe);
            }
        }

        public In(URL url) {
            if (url == null) throw new IllegalArgumentException("url argument is null");
            try {
                URLConnection site = url.openConnection();
                InputStream is     = site.getInputStream();
                scanner            = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
                scanner.useLocale(LOCALE);
            }
            catch (IOException ioe) {
                throw new IllegalArgumentException("could not read URL: '" + url + "'", ioe);
            }
        }

        public In(File file) {
            if (file == null) throw new IllegalArgumentException("file argument is null");
            try {
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
                scanner.useLocale(LOCALE);
            }
            catch (IOException ioe) {;
                throw new IllegalArgumentException("could not read file: " + file, ioe);
            }
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

        public In(Scanner scanner) {
            if (scanner == null) throw new IllegalArgumentException("scanner argument is null");
            this.scanner = scanner;
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

        @Deprecated
        public static int[] readInts(String filename) {
            return new In(filename).readAllInts();
        }

        @Deprecated
        public static double[] readDoubles(String filename) {
            return new In(filename).readAllDoubles();
        }

        @Deprecated
        public static String[] readStrings(String filename) {
            return new In(filename).readAllStrings();
        }

        @Deprecated
        public static int[] readInts() {
            return new In().readAllInts();
        }

        @Deprecated
        public static double[] readDoubles() {
            return new In().readAllDoubles();
        }

        @Deprecated
        public static String[] readStrings() {
            return new In().readAllStrings();
        }

        public static void main(String[] args) {
            In in;
            String urlName = "https://introcs.cs.princeton.edu/java/stdlib/InTest.txt";

            System.out.println("readAll() from URL " + urlName);
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In(urlName);
                System.out.println(in.readAll());
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();

            System.out.println("readLine() from URL " + urlName);
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In(urlName);
                while (!in.isEmpty()) {
                    String s = in.readLine();
                    System.out.println(s);
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();

            System.out.println("readString() from URL " + urlName);
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In(urlName);
                while (!in.isEmpty()) {
                    String s = in.readString();
                    System.out.println(s);
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();


            System.out.println("readLine() from current directory");
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In("./InTest.txt");
                while (!in.isEmpty()) {
                    String s = in.readLine();
                    System.out.println(s);
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();


            System.out.println("readLine() from relative path");
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In("../stdlib/InTest.txt");
                while (!in.isEmpty()) {
                    String s = in.readLine();
                    System.out.println(s);
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();

            System.out.println("readChar() from file");
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In("InTest.txt");
                while (!in.isEmpty()) {
                    char c = in.readChar();
                    System.out.print(c);
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();
            System.out.println();

            System.out.println("readLine() from absolute OS X / Linux path");
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In("/n/fs/introcs/www/java/stdlib/InTest.txt");
                while (!in.isEmpty()) {
                    String s = in.readLine();
                    System.out.println(s);
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();


            System.out.println("readLine() from absolute Windows path");
            System.out.println("---------------------------------------------------------------------------");
            try {
                in = new In("G:\\www\\introcs\\stdlib\\InTest.txt");
                while (!in.isEmpty()) {
                    String s = in.readLine();
                    System.out.println(s);
                }
                System.out.println();
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
            }
            System.out.println();

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

        @Deprecated
        public void change(int i, Key key) {
            changeKey(i, key);
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


        public static void main(String[] args) {
            String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

            IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
            for (int i = 0; i < strings.length; i++) {
                pq.insert(i, strings[i]);
            }

            while (!pq.isEmpty()) {
                int i = pq.delMin();
                StdOut.println(i + " " + strings[i]);
            }
            StdOut.println();

            for (int i = 0; i < strings.length; i++) {
                pq.insert(i, strings[i]);
            }

            for (int i : pq) {
                StdOut.println(i + " " + strings[i]);
            }
            while (!pq.isEmpty()) {
                pq.delMin();
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


        public static void main(String[] args) {
            Stack<String> stack = new Stack<String>();
            while (!StdIn.isEmpty()) {
                String item = StdIn.readString();
                if (!item.equals("-"))
                    stack.push(item);
                else if (!stack.isEmpty())
                    StdOut.print(stack.pop() + " ");
            }
            StdOut.println("(" + stack.size() + " left on stack)");
        }
    }


    public final class StdIn {

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

        @Deprecated
        public static int[] readInts() {
            return readAllInts();
        }

        @Deprecated
        public static double[] readDoubles() {
            return readAllDoubles();
        }

        @Deprecated
        public static String[] readStrings() {
            return readAllStrings();
        }


        public static void main(String[] args) {

            StdOut.print("Type a string: ");
            String s = StdIn.readString();
            StdOut.println("Your string was: " + s);
            StdOut.println();

            StdOut.print("Type an int: ");
            int a = StdIn.readInt();
            StdOut.println("Your int was: " + a);
            StdOut.println();

            StdOut.print("Type a boolean: ");
            boolean b = StdIn.readBoolean();
            StdOut.println("Your boolean was: " + b);
            StdOut.println();

            StdOut.print("Type a double: ");
            double c = StdIn.readDouble();
            StdOut.println("Your double was: " + c);
            StdOut.println();
        }

    }

    public final class StdOut {

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

        public static void main(String[] args) {

            StdOut.println("Test");
            StdOut.println(17);
            StdOut.println(true);
            StdOut.printf("%.6f\n", 1.0/7.0);
        }

    }

    public final class StdRandom {

        private static Random random;
        private static long seed;

        static {
            seed = System.currentTimeMillis();
            random = new Random(seed);
        }

        private StdRandom() { }

        public static void setSeed(long s) {
            seed   = s;
            random = new Random(seed);
        }

        public static long getSeed() {
            return seed;
        }

        @Deprecated
        public static double uniform() {
            return uniformDouble();
        }

        public static double uniformDouble() {
            return random.nextDouble();
        }

        @Deprecated
        public static int uniform(int n) {
            return uniformInt(n);
        }

        public static int uniformInt(int n) {
            if (n <= 0) throw new IllegalArgumentException("argument must be positive: " + n);
            return random.nextInt(n);
        }

        @Deprecated
        public static long uniform(long n) {
            return uniformLong(n);
        }

        public static long uniformLong(long n) {
            if (n <= 0L) throw new IllegalArgumentException("argument must be positive: " + n);

            long r = random.nextLong();
            long m = n - 1;

            if ((n & m) == 0L) {
                return r & m;
            }

            long u = r >>> 1;
            while (u + m - (r = u % n) < 0L) {
                u = random.nextLong() >>> 1;
            }
            return r;
        }

        @Deprecated
        public static double random() {
            return uniformDouble();
        }

        @Deprecated
        public static int uniform(int a, int b) {
            return uniformInt(a, b);
        }

        public static int uniformInt(int a, int b) {
            if ((b <= a) || ((long) b - a >= Integer.MAX_VALUE)) {
                throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
            }
            return a + uniform(b - a);
        }

        @Deprecated
        public static double uniform(double a, double b) {
            return uniformDouble(a, b);
        }

        public static double uniformDouble(double a, double b) {
            if (!(a < b)) {
                throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
            }
            return a + uniform() * (b-a);
        }

        public static boolean bernoulli(double p) {
            if (!(p >= 0.0 && p <= 1.0))
                throw new IllegalArgumentException("probability p must be between 0.0 and 1.0: " + p);
            return uniformDouble() < p;
        }

        public static boolean bernoulli() {
            return bernoulli(0.5);
        }

        public static double gaussian() {
            double r, x, y;
            do {
                x = uniformDouble(-1.0, 1.0);
                y = uniformDouble(-1.0, 1.0);
                r = x*x + y*y;
            } while (r >= 1 || r == 0);
            return x * Math.sqrt(-2 * Math.log(r) / r);

        }

        public static double gaussian(double mu, double sigma) {
            return mu + sigma * gaussian();
        }

        public static int geometric(double p) {
            if (!(p >= 0)) {
                throw new IllegalArgumentException("probability p must be greater than 0: " + p);
            }
            if (!(p <= 1.0)) {
                throw new IllegalArgumentException("probability p must not be larger than 1: " + p);
            }
            return (int) Math.ceil(Math.log(uniformDouble()) / Math.log(1.0 - p));
        }

        public static int poisson(double lambda) {
            if (!(lambda > 0.0))
                throw new IllegalArgumentException("lambda must be positive: " + lambda);
            if (Double.isInfinite(lambda))
                throw new IllegalArgumentException("lambda must not be infinite: " + lambda);
            int k = 0;
            double p = 1.0;
            double expLambda = Math.exp(-lambda);
            do {
                k++;
                p *= uniformDouble();
            } while (p >= expLambda);
            return k-1;
        }

        public static double pareto() {
            return pareto(1.0);
        }

        public static double pareto(double alpha) {
            if (!(alpha > 0.0))
                throw new IllegalArgumentException("alpha must be positive: " + alpha);
            return Math.pow(1 - uniformDouble(), -1.0 / alpha) - 1.0;
        }

        public static double cauchy() {
            return Math.tan(Math.PI * (uniformDouble() - 0.5));
        }

        public static int discrete(double[] probabilities) {
            if (probabilities == null) throw new IllegalArgumentException("argument array must not be null");
            double EPSILON = 1.0E-14;
            double sum = 0.0;
            for (int i = 0; i < probabilities.length; i++) {
                if (!(probabilities[i] >= 0.0))
                    throw new IllegalArgumentException("array entry " + i + " must be non-negative: " + probabilities[i]);
                sum += probabilities[i];
            }
            if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)
                throw new IllegalArgumentException("sum of array entries does not approximately equal 1.0: " + sum);

            while (true) {
                double r = uniformDouble();
                sum = 0.0;
                for (int i = 0; i < probabilities.length; i++) {
                    sum = sum + probabilities[i];
                    if (sum > r) return i;
                }
            }
        }

        public static int discrete(int[] frequencies) {
            if (frequencies == null) throw new IllegalArgumentException("argument array must not be null");
            long sum = 0;
            for (int i = 0; i < frequencies.length; i++) {
                if (frequencies[i] < 0)
                    throw new IllegalArgumentException("array entry " + i + " must be non-negative: " + frequencies[i]);
                sum += frequencies[i];
            }
            if (sum == 0)
                throw new IllegalArgumentException("at least one array entry must be positive");
            if (sum >= Integer.MAX_VALUE)
                throw new IllegalArgumentException("sum of frequencies overflows an int");

            double r = uniformInt((int) sum);
            sum = 0;
            for (int i = 0; i < frequencies.length; i++) {
                sum += frequencies[i];
                if (sum > r) return i;
            }

            assert false;
            return -1;
        }

        public static double exponential(double lambda) {
            if (!(lambda > 0.0))
                throw new IllegalArgumentException("lambda must be positive: " + lambda);
            return -Math.log(1 - uniformDouble()) / lambda;
        }

        @Deprecated
        public static double exp(double lambda) {
            return exponential(lambda);
        }

        public static void shuffle(Object[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);
                Object temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static void shuffle(double[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);
                double temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static void shuffle(int[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);
                int temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static void shuffle(char[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);
                char temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static void shuffle(Object[] a, int lo, int hi) {
            validateNotNull(a);
            validateSubarrayIndices(lo, hi, a.length);

            for (int i = lo; i < hi; i++) {
                int r = i + uniformInt(hi-i);
                Object temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static void shuffle(double[] a, int lo, int hi) {
            validateNotNull(a);
            validateSubarrayIndices(lo, hi, a.length);

            for (int i = lo; i < hi; i++) {
                int r = i + uniformInt(hi-i);
                double temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static void shuffle(int[] a, int lo, int hi) {
            validateNotNull(a);
            validateSubarrayIndices(lo, hi, a.length);

            for (int i = lo; i < hi; i++) {
                int r = i + uniformInt(hi-i);
                int temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        public static int[] permutation(int n) {
            if (n < 0) throw new IllegalArgumentException("n must be non-negative: " + n);
            int[] perm = new int[n];
            for (int i = 0; i < n; i++)
                perm[i] = i;
            shuffle(perm);
            return perm;
        }

        public static int[] permutation(int n, int k) {
            if (n < 0) throw new IllegalArgumentException("n must be non-negative: " + n);
            if (k < 0 || k > n) throw new IllegalArgumentException("k must be between 0 and n: " + k);
            int[] perm = new int[k];
            for (int i = 0; i < k; i++) {
                int r = uniformInt(i+1);
                perm[i] = perm[r];
                perm[r] = i;
            }
            for (int i = k; i < n; i++) {
                int r = uniformInt(i+1);
                if (r < k) perm[r] = i;
            }
            return perm;
        }

        private static void validateNotNull(Object x) {
            if (x == null) {
                throw new IllegalArgumentException("argument must not be null");
            }
        }

        private static void validateSubarrayIndices(int lo, int hi, int length) {
            if (lo < 0 || hi > length || lo > hi) {
                throw new IllegalArgumentException("subarray indices out of bounds: [" + lo + ", " + hi + ")");
            }
        }

        public static void main(String[] args) {
            int n = Integer.parseInt(args[0]);
            if (args.length == 2) StdRandom.setSeed(Long.parseLong(args[1]));
            double[] probabilities = { 0.5, 0.3, 0.1, 0.1 };
            int[] frequencies = { 5, 3, 1, 1 };
            String[] a = "A B C D E F G".split(" ");

            StdOut.println("seed = " + StdRandom.getSeed());
            for (int i = 0; i < n; i++) {
                StdOut.printf("%2d ",   uniformInt(100));
                StdOut.printf("%8.5f ", uniformDouble(10.0, 99.0));
                StdOut.printf("%5b ",   bernoulli(0.5));
                StdOut.printf("%7.5f ", gaussian(9.0, 0.2));
                StdOut.printf("%1d ",   discrete(probabilities));
                StdOut.printf("%1d ",   discrete(frequencies));
                StdOut.printf("%11d ",  uniformLong(100000000000L));
                StdRandom.shuffle(a);
                for (String s : a)
                    StdOut.print(s);
                StdOut.println();
            }
        }

    }

}