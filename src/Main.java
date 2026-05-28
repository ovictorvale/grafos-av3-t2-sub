import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

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
        private Node<Item> first;    // beginning of bag
        private int n;               // number of elements in bag

        // helper linked list class
        private static class Node<Item> {
            private Item item;
            private Node<Item> next;
        }

        /**
         * Initializes an empty bag.
         */
        public Bag() {
            first = null;
            n = 0;
        }

        /**
         * Returns true if this bag is empty.
         *
         * @return {@code true} if this bag is empty;
         *         {@code false} otherwise
         */
        public boolean isEmpty() {
            return first == null;
        }

        /**
         * Returns the number of items in this bag.
         *
         * @return the number of items in this bag
         */
        public int size() {
            return n;
        }

        /**
         * Adds the item to this bag.
         *
         * @param  item the item to add to this bag
         */
        public void add(Item item) {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
            n++;
        }


        /**
         * Returns an iterator that iterates over the items in this bag in arbitrary order.
         *
         * @return an iterator that iterates over the items in this bag in arbitrary order
         */
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

        /**
         * Unit tests the {@code Bag} data type.
         *
         * @param args the command-line arguments
         */
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
        private static final int EOF = -1;      // end of file

        private static BufferedInputStream in;  // input stream
        private static int buffer;              // one character buffer
        private static int n;                   // number of bits left in buffer
        private static boolean isInitialized;   // has BinaryStdIn been called for first time?

        // don't instantiate
        private BinaryStdIn() { }

        // fill buffer
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

        /**
         * Close this input stream and release any associated system resources.
         */
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

        /**
         * Returns true if standard input is empty.
         * @return true if and only if standard input is empty
         */
        public static boolean isEmpty() {
            if (!isInitialized) initialize();
            return buffer == EOF;
        }

        /**
         * Reads the next bit of data from standard input and return as a boolean.
         *
         * @return the next bit of data from standard input as a {@code boolean}
         * @throws NoSuchElementException if standard input is empty
         */
        public static boolean readBoolean() {
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
            n--;
            boolean bit = ((buffer >> n) & 1) == 1;
            if (n == 0) fillBuffer();
            return bit;
        }

        /**
         * Reads the next 8 bits from standard input and return as an 8-bit char.
         * Note that {@code char} is a 16-bit type;
         * to read the next 16 bits as a char, use {@code readChar(16)}.
         *
         * @return the next 8 bits of data from standard input as a {@code char}
         * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
         */
        public static char readChar() {
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

            // special case when aligned byte
            if (n == 8) {
                int x = buffer;
                fillBuffer();
                return (char) (x & 0xff);
            }

            // combine last n bits of current buffer with first 8-n bits of new buffer
            int x = buffer;
            x <<= (8 - n);
            int oldN = n;
            fillBuffer();
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
            n = oldN;
            x |= (buffer >>> n);
            return (char) (x & 0xff);
            // the above code doesn't quite work for the last character if n = 8
            // because buffer will be -1, so there is a special case for aligned byte
        }

        /**
         * Reads the next <em>r</em> bits from standard input and return as an <em>r</em>-bit character.
         *
         * @param  r number of bits to read.
         * @return the next r bits of data from standard input as a {@code char}
         * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
         * @throws IllegalArgumentException unless {@code 1 <= r <= 16}
         */
        public static char readChar(int r) {
            if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value of r = " + r);

            // optimize r = 8 case
            if (r == 8) return readChar();

            char x = 0;
            for (int i = 0; i < r; i++) {
                x <<= 1;
                boolean bit = readBoolean();
                if (bit) x |= 1;
            }
            return x;
        }

        /**
         * Reads the remaining bytes of data from standard input and return as a string.
         *
         * @return the remaining bytes of data from standard input as a {@code String}
         * @throws NoSuchElementException if standard input is empty or if the number of bits
         *         available on standard input is not a multiple of 8 (byte-aligned)
         */
        public static String readString() {
            if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

            StringBuilder sb = new StringBuilder();
            while (!isEmpty()) {
                char c = readChar();
                sb.append(c);
            }
            return sb.toString();
        }


        /**
         * Reads the next 16 bits from standard input and return as a 16-bit short.
         *
         * @return the next 16 bits of data from standard input as a {@code short}
         * @throws NoSuchElementException if there are fewer than 16 bits available on standard input
         */
        public static short readShort() {
            short x = 0;
            for (int i = 0; i < 2; i++) {
                char c = readChar();
                x <<= 8;
                x |= c;
            }
            return x;
        }

        /**
         * Reads the next 32 bits from standard input and return as a 32-bit int.
         *
         * @return the next 32 bits of data from standard input as a {@code int}
         * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
         */
        public static int readInt() {
            int x = 0;
            for (int i = 0; i < 4; i++) {
                char c = readChar();
                x <<= 8;
                x |= c;
            }
            return x;
        }

        /**
         * Reads the next <em>r</em> bits from standard input and return as an <em>r</em>-bit int.
         *
         * @param  r number of bits to read.
         * @return the next r bits of data from standard input as a {@code int}
         * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
         * @throws IllegalArgumentException unless {@code 1 <= r <= 32}
         */
        public static int readInt(int r) {
            if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value of r = " + r);

            // optimize r = 32 case
            if (r == 32) return readInt();

            int x = 0;
            for (int i = 0; i < r; i++) {
                x <<= 1;
                boolean bit = readBoolean();
                if (bit) x |= 1;
            }
            return x;
        }

        /**
         * Reads the next 64 bits from standard input and return as a 64-bit long.
         *
         * @return the next 64 bits of data from standard input as a {@code long}
         * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
         */
        public static long readLong() {
            long x = 0;
            for (int i = 0; i < 8; i++) {
                char c = readChar();
                x <<= 8;
                x |= c;
            }
            return x;
        }


        /**
         * Reads the next 64 bits from standard input and return as a 64-bit double.
         *
         * @return the next 64 bits of data from standard input as a {@code double}
         * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
         */
        public static double readDouble() {
            return Double.longBitsToDouble(readLong());
        }

        /**
         * Reads the next 32 bits from standard input and return as a 32-bit float.
         *
         * @return the next 32 bits of data from standard input as a {@code float}
         * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
         */
        public static float readFloat() {
            return Float.intBitsToFloat(readInt());
        }


        /**
         * Reads the next 8 bits from standard input and return as an 8-bit byte.
         *
         * @return the next 8 bits of data from standard input as a {@code byte}
         * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
         */
        public static byte readByte() {
            char c = readChar();
            return (byte) (c & 0xff);
        }

        /**
         * Test client. Reads in a binary input file from standard input and writes
         * it to standard output.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {

            // read one 8-bit char at a time
            while (!BinaryStdIn.isEmpty()) {
                char c = BinaryStdIn.readChar();
                BinaryStdOut.write(c);
            }
            BinaryStdOut.flush();
        }
    }

    public final class BinaryStdOut {
        private static BufferedOutputStream out;  // output stream (standard output)
        private static int buffer;                // 8-bit buffer of bits to write
        private static int n;                     // number of bits remaining in buffer
        private static boolean isInitialized;     // has BinaryStdOut been called for first time?

        // don't instantiate
        private BinaryStdOut() { }

        // initialize BinaryStdOut
        private static void initialize() {
            out = new BufferedOutputStream(System.out);
            buffer = 0;
            n = 0;
            isInitialized = true;
        }

        /**
         * Writes the specified bit to standard output.
         */
        private static void writeBit(boolean bit) {
            if (!isInitialized) initialize();

            // add bit to buffer
            buffer <<= 1;
            if (bit) buffer |= 1;

            // if buffer is full (8 bits), write out as a single byte
            n++;
            if (n == 8) clearBuffer();
        }

        /**
         * Writes the 8-bit byte to standard output.
         */
        private static void writeByte(int x) {
            if (!isInitialized) initialize();

            assert x >= 0 && x < 256;

            // optimized if byte-aligned
            if (n == 0) {
                try {
                    out.write(x);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            // otherwise write one bit at a time
            for (int i = 0; i < 8; i++) {
                boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
                writeBit(bit);
            }
        }

        // write out any remaining bits in buffer to standard output, padding with 0s
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

        /**
         * Flushes standard output, padding 0s if number of bits written so far
         * is not a multiple of 8.
         */
        public static void flush() {
            clearBuffer();
            try {
                out.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Flushes and closes standard output. Once standard output is closed, you can no
         * longer write bits to it.
         */
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


        /**
         * Writes the specified bit to standard output.
         * @param x the {@code boolean} to write.
         */
        public static void write(boolean x) {
            writeBit(x);
        }

        /**
         * Writes the 8-bit byte to standard output.
         * @param x the {@code byte} to write.
         */
        public static void write(byte x) {
            writeByte(x & 0xff);
        }

        /**
         * Writes the 32-bit int to standard output.
         * @param x the {@code int} to write.
         */
        public static void write(int x) {
            writeByte((x >>> 24) & 0xff);
            writeByte((x >>> 16) & 0xff);
            writeByte((x >>>  8) & 0xff);
            writeByte((x >>>  0) & 0xff);
        }

        /**
         * Writes the <em>r</em>-bit int to standard output.
         * @param x the {@code int} to write.
         * @param r the number of relevant bits in the char.
         * @throws IllegalArgumentException if {@code r} is not between 1 and 32.
         * @throws IllegalArgumentException if {@code x} is not between 0 and 2<sup>r</sup> - 1.
         */
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





        /**
         * Writes the 64-bit double to standard output.
         * @param x the {@code double} to write.
         */
        public static void write(double x) {
            write(Double.doubleToRawLongBits(x));
        }

        /**
         * Writes the 64-bit long to standard output.
         * @param x the {@code long} to write.
         */
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

        /**
         * Writes the 32-bit float to standard output.
         * @param x the {@code float} to write.
         */
        public static void write(float x) {
            write(Float.floatToRawIntBits(x));
        }

        /**
         * Writes the 16-bit int to standard output.
         * @param x the {@code short} to write.
         */
        public static void write(short x) {
            writeByte((x >>>  8) & 0xff);
            writeByte((x >>>  0) & 0xff);
        }

        /**
         * Writes the 8-bit char to standard output.
         * @param x the {@code char} to write.
         * @throws IllegalArgumentException if {@code x} is not between 0 and 255.
         */
        public static void write(char x) {
            if (x < 0 || x >= 256) throw new IllegalArgumentException("Illegal 8-bit char = " + x);
            writeByte(x);
        }

        /**
         * Writes the <em>r</em>-bit char to standard output.
         * @param x the {@code char} to write.
         * @param r the number of relevant bits in the char.
         * @throws IllegalArgumentException if {@code r} is not between 1 and 16.
         * @throws IllegalArgumentException if {@code x} is not between 0 and 2<sup>r</sup> - 1.
         */
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

        /**
         * Writes the string of 8-bit characters to standard output.
         * @param s the {@code String} to write.
         * @throws IllegalArgumentException if any character in the string is not
         * between 0 and 255.
         */
        public static void write(String s) {
            for (int i = 0; i < s.length(); i++)
                write(s.charAt(i));
        }

        /**
         * Writes the string of <em>r</em>-bit characters to standard output.
         * @param s the {@code String} to write.
         * @param r the number of relevant bits in each character.
         * @throws IllegalArgumentException if r is not between 1 and 16.
         * @throws IllegalArgumentException if any character in the string is not
         * between 0 and 2<sup>r</sup> - 1.
         */
        public static void write(String s, int r) {
            for (int i = 0; i < s.length(); i++)
                write(s.charAt(i), r);
        }

        /**
         * Tests the methods in this class.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            int m = Integer.parseInt(args[0]);

            // write n integers to binary standard output
            for (int i = 0; i < m; i++) {
                BinaryStdOut.write(i);
            }
            BinaryStdOut.flush();
        }

    }
    public static class DijkstraSP {
        private double[] distTo;          // distTo[v] = distance  of shortest s->v path
        private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
        private IndexMinPQ<Double> pq;    // priority queue of vertices

        /**
         * Computes a shortest-paths tree from the source vertex {@code s} to every other
         * vertex in the edge-weighted {@code digraph}.
         *
         * @param  digraph the edge-weighted digraph
         * @param  s the source vertex
         * @throws IllegalArgumentException if an edge weight is negative
         * @throws IllegalArgumentException unless {@code 0 <= s < V}
         */
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

            // relax vertices in order of distance from s
            pq = new IndexMinPQ<Double>(digraph.V());
            pq.insert(s, distTo[s]);
            while (!pq.isEmpty()) {
                int v = pq.delMin();
                for (DirectedEdge e : digraph.adj(v))
                    relax(e);
            }

            // check optimality conditions
            assert check(digraph, s);
        }

        // relax edge e and update pq if changed
        private void relax(DirectedEdge e) {
            int v = e.from(), w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }

        /**
         * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
         * @param  v the destination vertex
         * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
         *         {@code Double.POSITIVE_INFINITY} if no such path
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public double distTo(int v) {
            validateVertex(v);
            return distTo[v];
        }

        /**
         * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
         *
         * @param  v the destination vertex
         * @return {@code true} if there is a path from the source vertex
         *         {@code s} to vertex {@code v}; {@code false} otherwise
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public boolean hasPathTo(int v) {
            validateVertex(v);
            return distTo[v] < Double.POSITIVE_INFINITY;
        }

        /**
         * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
         *
         * @param  v the destination vertex
         * @return a shortest path from the source vertex {@code s} to vertex {@code v}
         *         as an iterable of edges, and {@code null} if no such path
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public Iterable<DirectedEdge> pathTo(int v) {
            validateVertex(v);
            if (!hasPathTo(v)) return null;
            Stack<DirectedEdge> path = new Stack<DirectedEdge>();
            for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
                path.push(e);
            }
            return path;
        }


        // check optimality conditions:
        // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
        // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
        private boolean check(EdgeWeightedDigraph digraph, int s) {

            // check that edge weights are non-negative
            for (DirectedEdge e : digraph.edges()) {
                if (e.weight() < 0) {
                    System.err.println("negative edge weight detected");
                    return false;
                }
            }

            // check that distTo[v] and edgeTo[v] are consistent
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

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < digraph.V(); v++) {
                for (DirectedEdge e : digraph.adj(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
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

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            int V = distTo.length;
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }

        /**
         * Unit tests the {@code DijkstraSP} data type.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            In in = new In(args[0]);
            EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(in);
            int s = Integer.parseInt(args[1]);

            // compute shortest paths
            DijkstraSP sp = new DijkstraSP(digraph, s);


            // print shortest path
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

        /**
         * Initializes a directed edge from vertex {@code v} to vertex {@code w} with
         * the given {@code weight}.
         * @param v the tail vertex
         * @param w the head vertex
         * @param weight the weight of the directed edge
         * @throws IllegalArgumentException if either {@code v} or {@code w}
         *    is a negative integer
         * @throws IllegalArgumentException if {@code weight} is {@code NaN}
         */
        public DirectedEdge(int v, int w, double weight) {
            if (v < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
            if (w < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
            if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        /**
         * Returns the tail vertex of the directed edge.
         * @return the tail vertex of the directed edge
         */
        public int from() {
            return v;
        }

        /**
         * Returns the head vertex of the directed edge.
         * @return the head vertex of the directed edge
         */
        public int to() {
            return w;
        }

        /**
         * Returns the weight of the directed edge.
         * @return the weight of the directed edge
         */
        public double weight() {
            return weight;
        }

        /**
         * Returns a string representation of the directed edge.
         * @return a string representation of the directed edge
         */
        public String toString() {
            return v + "->" + w + " " + String.format("%5.2f", weight);
        }

        /**
         * Unit tests the {@code DirectedEdge} data type.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            DirectedEdge e = new DirectedEdge(12, 34, 5.67);
            StdOut.println(e);
        }
    }

    public static class EdgeWeightedDigraph {
        private static final String NEWLINE = System.getProperty("line.separator");

        private final int V;                // number of vertices in this digraph
        private int E;                      // number of edges in this digraph
        private Bag<DirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
        private int[] indegree;             // indegree[v] = indegree of vertex v

        /**
         * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
         *
         * @param  V the number of vertices
         * @throws IllegalArgumentException if {@code V < 0}
         */
        public EdgeWeightedDigraph(int V) {
            if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be non-negative");
            this.V = V;
            this.E = 0;
            this.indegree = new int[V];
            adj = (Bag<DirectedEdge>[]) new Bag[V];
            for (int v = 0; v < V; v++)
                adj[v] = new Bag<DirectedEdge>();
        }

        /**
         * Initializes a random edge-weighted digraph with {@code V} vertices and <em>E</em> edges.
         *
         * @param  V the number of vertices
         * @param  E the number of edges
         * @throws IllegalArgumentException if {@code V < 0}
         * @throws IllegalArgumentException if {@code E < 0}
         */
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

        /**
         * Initializes an edge-weighted digraph from the specified input stream.
         * The format is the number of vertices <em>V</em>,
         * followed by the number of edges <em>E</em>,
         * followed by <em>E</em> pairs of vertices and edge weights,
         * with each entry separated by whitespace.
         *
         * @param  in the input stream
         * @throws IllegalArgumentException if {@code in} is {@code null}
         * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
         * @throws IllegalArgumentException if the number of vertices or edges is negative
         */
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

        /**
         * Initializes a new edge-weighted digraph that is a deep copy of {@code G}.
         *
         * @param  G the edge-weighted digraph to copy
         */
        public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
            this(G.V());
            this.E = G.E();
            for (int v = 0; v < G.V(); v++)
                this.indegree[v] = G.indegree(v);
            for (int v = 0; v < G.V(); v++) {
                // reverse so that adjacency list is in same order as original
                Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
                for (DirectedEdge e : G.adj[v]) {
                    reverse.push(e);
                }
                for (DirectedEdge e : reverse) {
                    adj[v].add(e);
                }
            }
        }

        /**
         * Returns the number of vertices in this edge-weighted digraph.
         *
         * @return the number of vertices in this edge-weighted digraph
         */
        public int V() {
            return V;
        }

        /**
         * Returns the number of edges in this edge-weighted digraph.
         *
         * @return the number of edges in this edge-weighted digraph
         */
        public int E() {
            return E;
        }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }

        /**
         * Adds the directed edge {@code e} to this edge-weighted digraph.
         *
         * @param  e the edge
         * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
         *         and {@code V-1}
         */
        public void addEdge(DirectedEdge e) {
            int v = e.from();
            int w = e.to();
            validateVertex(v);
            validateVertex(w);
            adj[v].add(e);
            indegree[w]++;
            E++;
        }


        /**
         * Returns the directed edges incident from vertex {@code v}.
         *
         * @param  v the vertex
         * @return the directed edges incident from vertex {@code v} as an Iterable
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public Iterable<DirectedEdge> adj(int v) {
            validateVertex(v);
            return adj[v];
        }

        /**
         * Returns the number of directed edges incident from vertex {@code v}.
         * This is known as the <em>outdegree</em> of vertex {@code v}.
         *
         * @param  v the vertex
         * @return the outdegree of vertex {@code v}
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public int outdegree(int v) {
            validateVertex(v);
            return adj[v].size();
        }

        /**
         * Returns the number of directed edges incident to vertex {@code v}.
         * This is known as the <em>indegree</em> of vertex {@code v}.
         *
         * @param  v the vertex
         * @return the indegree of vertex {@code v}
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public int indegree(int v) {
            validateVertex(v);
            return indegree[v];
        }

        /**
         * Returns all directed edges in this edge-weighted digraph.
         * To iterate over the edges in this edge-weighted digraph, use foreach notation:
         * {@code for (DirectedEdge e : G.edges())}.
         *
         * @return all edges in this edge-weighted digraph, as an iterable
         */
        public Iterable<DirectedEdge> edges() {
            Bag<DirectedEdge> list = new Bag<DirectedEdge>();
            for (int v = 0; v < V; v++) {
                for (DirectedEdge e : adj(v)) {
                    list.add(e);
                }
            }
            return list;
        }

        /**
         * Returns a string representation of this edge-weighted digraph.
         *
         * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
         *         followed by the <em>V</em> adjacency lists of edges
         */
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

        /**
         * Returns a string representation of this edge-weighted digraph in DOT format,
         * suitable for visualization with Graphviz.
         *
         * To visualize the graph, install Graphviz (e.g., "brew install graphviz").
         * Then use one of the graph visualization tools
         *    - dot    (hierarchical or layer drawing)
         *    - neato  (spring model)
         *    - fdp    (force-directed placement)
         *    - sfdp   (scalable force-directed placement)
         *    - twopi  (radial layout)
         *
         * For example, the following commands will create graph drawings in SVG
         * and PDF formats
         *    - dot input.dot -Tsvg -o output.svg
         *    - dot input.dot -Tpdf -o output.pdf
         *
         * To change the digraph attributes (e.g., vertex and edge shapes, arrows, colors)
         *  in the DOT format, see https://graphviz.org/doc/info/lang.html
         *
         * @return a string representation of this edge-weighted digraph in DOT format
         */
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

        /**
         * Unit tests the {@code EdgeWeightedDigraph} data type.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            In in = new In(args[0]);
            EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
            StdOut.println(G);
        }

    }


    public static final class In {

        ///// begin: section (1 of 2) of code duplicated from In to StdIn.

        // assume Unicode UTF-8 encoding
        private static final String CHARSET_NAME = "UTF-8";

        // assume language = English, country = US for consistency with System.out.
        private static final Locale LOCALE = Locale.US;

        // the default token separator; we maintain the invariant that this value
        // is held by the scanner's delimiter between calls
        private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");

        // makes whitespace characters significant
        private static final Pattern EMPTY_PATTERN = Pattern.compile("");

        // used to read the entire input. source:
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

        //// end: section (1 of 2) of code duplicated from In to StdIn.

        private Scanner scanner;

        /**
         * Initializes an input stream from standard input.
         */
        public In() {
            scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        }

        /**
         * Initializes an input stream from a socket.
         *
         * @param  socket the socket
         * @throws IllegalArgumentException if cannot open {@code socket}
         * @throws IllegalArgumentException if {@code socket} is {@code null}
         */
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

        /**
         * Initializes an input stream from a URL.
         *
         * @param  url the URL
         * @throws IllegalArgumentException if cannot open {@code url}
         * @throws IllegalArgumentException if {@code url} is {@code null}
         */
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

        /**
         * Initializes an input stream from a file.
         *
         * @param  file the file
         * @throws IllegalArgumentException if cannot open {@code file}
         * @throws IllegalArgumentException if {@code file} is {@code null}
         */
        public In(File file) {
            if (file == null) throw new IllegalArgumentException("file argument is null");
            try {
                // for consistency with StdIn, wrap with BufferedInputStream instead of use
                // file as argument to Scanner
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
                scanner.useLocale(LOCALE);
            }
            catch (IOException ioe) {;
                throw new IllegalArgumentException("could not read file: " + file, ioe);
            }
        }


        /**
         * Initializes an input stream from a filename or web page name.
         *
         * @param  name the filename or web page name
         * @throws IllegalArgumentException if cannot open {@code name} as
         *         a file or URL
         * @throws IllegalArgumentException if {@code name} is {@code null}
         */
        public In(String name) {
            if (name == null) throw new IllegalArgumentException("argument is null");
            if (name.length() == 0) throw new IllegalArgumentException("argument is the empty string");
            try {
                // first try to read file from local file system
                File file = new File(name);
                if (file.exists()) {
                    // for consistency with StdIn, wrap with BufferedInputStream instead of use
                    // file as argument to Scanner
                    FileInputStream fis = new FileInputStream(file);
                    scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
                    scanner.useLocale(LOCALE);
                    return;
                }

                // resource relative to .class file
                URL url = getClass().getResource(name);

                // resource relative to classloader root
                if (url == null) {
                    url = getClass().getClassLoader().getResource(name);
                }

                // or URL from web
                if (url == null) {
                    URI uri = new URI(name);
                    if (uri.isAbsolute()) url = uri.toURL();
                    else throw new IllegalArgumentException("could not read: '" + name + "'");
                    url = new URL(name);
                }

                URLConnection site = url.openConnection();

                // in order to set User-Agent, replace above line with these two
                // HttpURLConnection site = (HttpURLConnection) url.openConnection();
                // site.addRequestProperty("User-Agent", "Mozilla/4.76");

                InputStream is     = site.getInputStream();
                scanner            = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
                scanner.useLocale(LOCALE);
            }
            catch (IOException | URISyntaxException e) {
                throw new IllegalArgumentException("could not read: '" + name + "'");
            }
        }

        /**
         * Initializes an input stream from a given {@link Scanner} source; use with
         * {@code new Scanner(String)} to read from a string.
         * <p>
         * Note that this does not create a defensive copy, so the
         * scanner will be mutated as you read on.
         *
         * @param  scanner the scanner
         * @throws IllegalArgumentException if {@code scanner} is {@code null}
         */
        public In(Scanner scanner) {
            if (scanner == null) throw new IllegalArgumentException("scanner argument is null");
            this.scanner = scanner;
        }

        /**
         * Returns true if this input stream exists.
         *
         * @return {@code true} if this input stream exists; {@code false} otherwise
         */
        public boolean exists()  {
            return scanner != null;
        }

        ////  begin: section (2 of 2) of code duplicated from In to StdIn,
        ////  with all methods changed from "public" to "public static".

        /**
         * Returns true if input stream is empty (except possibly whitespace).
         * Use this to know whether the next call to {@link #readString()},
         * {@link #readDouble()}, etc. will succeed.
         *
         * @return {@code true} if this input stream is empty (except possibly whitespace);
         *         {@code false} otherwise
         */
        public boolean isEmpty() {
            return !scanner.hasNext();
        }

        /**
         * Returns true if this input stream has a next line.
         * Use this method to know whether the
         * next call to {@link #readLine()} will succeed.
         * This method is functionally equivalent to {@link #hasNextChar()}.
         *
         * @return {@code true} if this input stream has more input (including whitespace);
         *         {@code false} otherwise
         */
        public boolean hasNextLine() {
            return scanner.hasNextLine();
        }

        /**
         * Returns true if this input stream has more input (including whitespace).
         * Use this method to know whether the next call to {@link #readChar()} will succeed.
         * This method is functionally equivalent to {@link #hasNextLine()}.
         *
         * @return {@code true} if this input stream has more input (including whitespace);
         *         {@code false} otherwise
         */
        public boolean hasNextChar() {
            scanner.useDelimiter(EMPTY_PATTERN);
            boolean result = scanner.hasNext();
            scanner.useDelimiter(WHITESPACE_PATTERN);
            return result;
        }


        /**
         * Reads and returns the next line in this input stream.
         *
         * @return the next line in this input stream; {@code null} if no such line
         */
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

        /**
         * Reads and returns the next character in this input stream.
         *
         * @return the next {@code char} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         */
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


        /**
         * Reads and returns the remainder of this input stream, as a string.
         *
         * @return the remainder of this input stream, as a string
         */
        public String readAll() {
            if (!scanner.hasNextLine())
                return "";

            String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
            // not that important to reset delimeter, since now scanner is empty
            scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
            return result;
        }


        /**
         * Reads the next token from this input stream and returns it as a {@code String}.
         *
         * @return the next {@code String} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         */
        public String readString() {
            try {
                return scanner.next();
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'String' value from the input stream, "
                        + "but no more tokens are available");
            }
        }

        /**
         * Reads the next token from this input stream, parses it as a {@code int},
         * and returns the {@code int}.
         *
         * @return the next {@code int} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as an {@code int}
         */
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

        /**
         * Reads the next token from this input stream, parses it as a {@code double},
         * and returns the {@code double}.
         *
         * @return the next {@code double} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code double}
         */
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

        /**
         * Reads the next token from this input stream, parses it as a {@code float},
         * and returns the {@code float}.
         *
         * @return the next {@code float} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code float}
         */
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

        /**
         * Reads the next token from this input stream, parses it as a {@code long},
         * and returns the {@code long}.
         *
         * @return the next {@code long} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code long}
         */
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

        /**
         * Reads the next token from this input stream, parses it as a {@code short},
         * and returns the {@code short}.
         *
         * @return the next {@code short} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code short}
         */
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

        /**
         * Reads the next token from this input stream, parses it as a {@code byte},
         * and returns the {@code byte}.
         * <p>
         *
         * @return the next {@code byte} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code byte}
         */
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

        /**
         * Reads the next token from this input stream, parses it as a {@code boolean}
         * (interpreting either {@code "true"} or {@code "1"} as {@code true},
         * and either {@code "false"} or {@code "0"} as {@code false}).
         *
         * @return the next {@code boolean} in this input stream
         * @throws NoSuchElementException if the input stream is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code boolean}
         */
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

        /**
         * Reads all remaining tokens from this input stream and returns them as
         * an array of strings.
         *
         * @return all remaining tokens in this input stream, as an array of strings
         */
        public String[] readAllStrings() {
            // we could use readAll.trim().split(), but that's not consistent
            // since trim() uses characters 0x00..0x20 as whitespace
            String[] tokens = WHITESPACE_PATTERN.split(readAll());
            if (tokens.length == 0 || tokens[0].length() > 0)
                return tokens;
            String[] decapitokens = new String[tokens.length-1];
            for (int i = 0; i < tokens.length-1; i++)
                decapitokens[i] = tokens[i+1];
            return decapitokens;
        }

        /**
         * Reads all remaining lines from this input stream and returns them as
         * an array of strings.
         *
         * @return all remaining lines in this input stream, as an array of strings
         */
        public String[] readAllLines() {
            ArrayList<String> lines = new ArrayList<String>();
            while (hasNextLine()) {
                lines.add(readLine());
            }
            return lines.toArray(new String[0]);
        }


        /**
         * Reads all remaining tokens from this input stream, parses them as integers,
         * and returns them as an array of integers.
         *
         * @return all remaining lines in this input stream, as an array of integers
         */
        public int[] readAllInts() {
            String[] fields = readAllStrings();
            int[] vals = new int[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Integer.parseInt(fields[i]);
            return vals;
        }

        /**
         * Reads all remaining tokens from this input stream, parses them as longs,
         * and returns them as an array of longs.
         *
         * @return all remaining lines in this input stream, as an array of longs
         */
        public long[] readAllLongs() {
            String[] fields = readAllStrings();
            long[] vals = new long[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Long.parseLong(fields[i]);
            return vals;
        }

        /**
         * Reads all remaining tokens from this input stream, parses them as doubles,
         * and returns them as an array of doubles.
         *
         * @return all remaining lines in this input stream, as an array of doubles
         */
        public double[] readAllDoubles() {
            String[] fields = readAllStrings();
            double[] vals = new double[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Double.parseDouble(fields[i]);
            return vals;
        }

        ///// end: section (2 of 2) of code duplicated from In to StdIn */

        /**
         * Closes this input stream.
         */
        public void close() {
            scanner.close();
        }

        /**
         * Reads all integers from a file and returns them as
         * an array of integers.
         *
         * @param      filename the name of the file
         * @return     the integers in the file
         * @deprecated Replaced by {@code new In(filename)}.{@link #readAllInts()}.
         */
        @Deprecated
        public static int[] readInts(String filename) {
            return new In(filename).readAllInts();
        }

        /**
         * Reads all doubles from a file and returns them as
         * an array of doubles.
         *
         * @param      filename the name of the file
         * @return     the doubles in the file
         * @deprecated Replaced by {@code new In(filename)}.{@link #readAllDoubles()}.
         */
        @Deprecated
        public static double[] readDoubles(String filename) {
            return new In(filename).readAllDoubles();
        }

        /**
         * Reads all strings from a file and returns them as
         * an array of strings.
         *
         * @param      filename the name of the file
         * @return     the strings in the file
         * @deprecated Replaced by {@code new In(filename)}.{@link #readAllStrings()}.
         */
        @Deprecated
        public static String[] readStrings(String filename) {
            return new In(filename).readAllStrings();
        }

        /**
         * Reads all integers from standard input and returns them
         * an array of integers.
         *
         * @return     the integers on standard input
         * @deprecated Replaced by {@code new In()}.{@link #readAllInts()}.
         */
        @Deprecated
        public static int[] readInts() {
            return new In().readAllInts();
        }

        /**
         * Reads all doubles from standard input and returns them as
         * an array of doubles.
         *
         * @return     the doubles on standard input
         * @deprecated Replaced by {@code new In()}.{@link #readAllDoubles()}.
         */
        @Deprecated
        public static double[] readDoubles() {
            return new In().readAllDoubles();
        }

        /**
         * Reads all strings from standard input and returns them as
         *  an array of strings.
         *
         * @return     the strings on standard input
         * @deprecated Replaced by {@code new In()}.{@link #readAllStrings()}.
         */
        @Deprecated
        public static String[] readStrings() {
            return new In().readAllStrings();
        }

        /**
         * Unit tests the {@code In} data type.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            In in;
            String urlName = "https://introcs.cs.princeton.edu/java/stdlib/InTest.txt";

            // read from a URL
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

            // read one line at a time from URL
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

            // read one string at a time from URL
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


            // read one line at a time from file in current directory
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


            // read one line at a time from file using relative path
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

            // read one char at a time
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

            // read one line at a time from absolute OS X / Linux path
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


            // read one line at a time from absolute Windows path
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
        private int maxN;        // maximum number of elements on PQ
        private int n;           // number of elements on PQ
        private int[] pq;        // binary heap using 1-based indexing
        private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
        private Key[] keys;      // keys[i] = priority of i

        /**
         * Initializes an empty indexed priority queue with indices between {@code 0}
         * and {@code maxN - 1}.
         * @param  maxN the keys on this priority queue are index from {@code 0}
         *         {@code maxN - 1}
         * @throws IllegalArgumentException if {@code maxN < 0}
         */
        public IndexMinPQ(int maxN) {
            if (maxN < 0) throw new IllegalArgumentException();
            this.maxN = maxN;
            n = 0;
            keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
            pq   = new int[maxN + 1];
            qp   = new int[maxN + 1];                   // make this of length maxN??
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }

        /**
         * Returns true if this priority queue is empty.
         *
         * @return {@code true} if this priority queue is empty;
         *         {@code false} otherwise
         */
        public boolean isEmpty() {
            return n == 0;
        }

        /**
         * Is {@code i} an index on this priority queue?
         *
         * @param  i an index
         * @return {@code true} if {@code i} is an index on this priority queue;
         *         {@code false} otherwise
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         */
        public boolean contains(int i) {
            validateIndex(i);
            return qp[i] != -1;
        }

        /**
         * Returns the number of keys on this priority queue.
         *
         * @return the number of keys on this priority queue
         */
        public int size() {
            return n;
        }

        /**
         * Associates key with index {@code i}.
         *
         * @param  i an index
         * @param  key the key to associate with index {@code i}
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @throws IllegalArgumentException if there already is an item associated
         *         with index {@code i}
         */
        public void insert(int i, Key key) {
            validateIndex(i);
            if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
            n++;
            qp[i] = n;
            pq[n] = i;
            keys[i] = key;
            swim(n);
        }

        /**
         * Returns an index associated with a minimum key.
         *
         * @return an index associated with a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public int minIndex() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        /**
         * Returns a minimum key.
         *
         * @return a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public Key minKey() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return keys[pq[1]];
        }

        /**
         * Removes a minimum key and returns its associated index.
         * @return an index associated with a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public int delMin() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            int min = pq[1];
            exch(1, n--);
            sink(1);
            assert min == pq[n+1];
            qp[min] = -1;        // delete
            keys[min] = null;    // to help with garbage collection
            pq[n+1] = -1;        // not needed
            return min;
        }

        /**
         * Returns the key associated with index {@code i}.
         *
         * @param  i the index of the key to return
         * @return the key associated with index {@code i}
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public Key keyOf(int i) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            else return keys[i];
        }

        /**
         * Change the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to change
         * @param  key change the key associated with index {@code i} to this key
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
        public void changeKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            keys[i] = key;
            swim(qp[i]);
            sink(qp[i]);
        }

        /**
         * Change the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to change
         * @param  key change the key associated with index {@code i} to this key
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @deprecated Replaced by {@code changeKey(int, Key)}.
         */
        @Deprecated
        public void change(int i, Key key) {
            changeKey(i, key);
        }

        /**
         * Decrease the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to decrease
         * @param  key decrease the key associated with index {@code i} to this key
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @throws IllegalArgumentException if {@code key >= keyOf(i)}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
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

        /**
         * Increase the key associated with index {@code i} to the specified value.
         *
         * @param  i the index of the key to increase
         * @param  key increase the key associated with index {@code i} to this key
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @throws IllegalArgumentException if {@code key <= keyOf(i)}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
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

        /**
         * Remove the key associated with index {@code i}.
         *
         * @param  i the index of the key to remove
         * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
         * @throws NoSuchElementException no key is associated with index {@code i}
         */
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

        // throw an IllegalArgumentException if i is an invalid index
        private void validateIndex(int i) {
            if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
            if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
        }

        /***************************************************************************
         * General helper functions.
         ***************************************************************************/
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


        /***************************************************************************
         * Heap helper functions.
         ***************************************************************************/
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


        /***************************************************************************
         * Iterators.
         ***************************************************************************/

        /**
         * Returns an iterator that iterates over the keys on the
         * priority queue in ascending order.
         * The iterator doesn't implement {@code remove()} since it's optional.
         *
         * @return an iterator that iterates over the keys in ascending order
         */
        public Iterator<Integer> iterator() { return new HeapIterator(); }

        private class HeapIterator implements Iterator<Integer> {
            // create a new pq
            private IndexMinPQ<Key> copy;

            // add all elements to copy of heap
            // takes linear time since already in heap order so no keys move
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


        /**
         * Unit tests the {@code IndexMinPQ} data type.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {
            // insert a bunch of strings
            String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

            IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
            for (int i = 0; i < strings.length; i++) {
                pq.insert(i, strings[i]);
            }

            // delete and print each key
            while (!pq.isEmpty()) {
                int i = pq.delMin();
                StdOut.println(i + " " + strings[i]);
            }
            StdOut.println();

            // reinsert the same strings
            for (int i = 0; i < strings.length; i++) {
                pq.insert(i, strings[i]);
            }

            // print each key using the iterator
            for (int i : pq) {
                StdOut.println(i + " " + strings[i]);
            }
            while (!pq.isEmpty()) {
                pq.delMin();
            }

        }
    }

    public static class Stack<Item> implements Iterable<Item> {
        private Node<Item> first;     // top of stack
        private int n;                // size of the stack

        // helper linked list class
        private static class Node<Item> {
            private Item item;
            private Node<Item> next;
        }

        /**
         * Initializes an empty stack.
         */
        public Stack() {
            first = null;
            n = 0;
        }

        /**
         * Returns true if this stack is empty.
         *
         * @return true if this stack is empty; false otherwise
         */
        public boolean isEmpty() {
            return first == null;
        }

        /**
         * Returns the number of items in this stack.
         *
         * @return the number of items in this stack
         */
        public int size() {
            return n;
        }

        /**
         * Adds the item to this stack.
         *
         * @param  item the item to add
         */
        public void push(Item item) {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
            n++;
        }

        /**
         * Removes and returns the item most recently added to this stack.
         *
         * @return the item most recently added
         * @throws NoSuchElementException if this stack is empty
         */
        public Item pop() {
            if (isEmpty()) throw new NoSuchElementException("Stack underflow");
            Item item = first.item;        // save item to return
            first = first.next;            // delete first node
            n--;
            return item;                   // return the saved item
        }


        /**
         * Returns (but does not remove) the item most recently added to this stack.
         *
         * @return the item most recently added to this stack
         * @throws NoSuchElementException if this stack is empty
         */
        public Item peek() {
            if (isEmpty()) throw new NoSuchElementException("Stack underflow");
            return first.item;
        }

        /**
         * Returns a string representation of this stack.
         *
         * @return the sequence of items in this stack in LIFO order, separated by spaces
         */
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (Item item : this) {
                s.append(item);
                s.append(' ');
            }
            return s.toString();
        }


        /**
         * Returns an iterator to this stack that iterates through the items in LIFO order.
         *
         * @return an iterator to this stack that iterates through the items in LIFO order
         */
        public Iterator<Item> iterator() {
            return new LinkedIterator(first);
        }

        // the iterator
        private class LinkedIterator implements Iterator<Item> {
            private Node<Item> current;

            public LinkedIterator(Node<Item> first) {
                current = first;
            }

            // is there a next item?
            public boolean hasNext() {
                return current != null;
            }

            // returns the next item
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }
        }


        /**
         * Unit tests the {@code Stack} data type.
         *
         * @param args the command-line arguments
         */
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

        /*** begin: section (1 of 2) of code duplicated from In to StdIn. */

        // assume Unicode UTF-8 encoding
        private static final String CHARSET_NAME = "UTF-8";

        // assume language = English, country = US for consistency with System.out.
        private static final Locale LOCALE = Locale.US;

        // the default token separator; we maintain the invariant that this value
        // is held by the scanner's delimiter between calls
        private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");

        // makes whitespace significant
        private static final Pattern EMPTY_PATTERN = Pattern.compile("");

        // used to read the entire input
        private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

        /*** end: section (1 of 2) of code duplicated from In to StdIn. */

        private static Scanner scanner;

        // it doesn't make sense to instantiate this class
        private StdIn() { }

        //// begin: section (2 of 2) of code duplicated from In to StdIn,
        //// with all methods changed from "public" to "public static"

        /**
         * Returns true if standard input is empty (except possibly for whitespace).
         * Use this method to know whether the next call to {@link #readString()},
         * {@link #readDouble()}, etc. will succeed.
         *
         * @return {@code true} if standard input is empty (except possibly
         *         for whitespace); {@code false} otherwise
         */
        public static boolean isEmpty() {
            return !scanner.hasNext();
        }

        /**
         * Returns true if standard input has a next line.
         * Use this method to know whether the
         * next call to {@link #readLine()} will succeed.
         * This method is functionally equivalent to {@link #hasNextChar()}.
         *
         * @return {@code true} if standard input has more input (including whitespace);
         *         {@code false} otherwise
         */
        public static boolean hasNextLine() {
            return scanner.hasNextLine();
        }

        /**
         * Returns true if standard input has more input (including whitespace).
         * Use this method to know whether the next call to {@link #readChar()} will succeed.
         * This method is functionally equivalent to {@link #hasNextLine()}.
         *
         * @return {@code true} if standard input has more input (including whitespace);
         *         {@code false} otherwise
         */
        public static boolean hasNextChar() {
            scanner.useDelimiter(EMPTY_PATTERN);
            boolean result = scanner.hasNext();
            scanner.useDelimiter(WHITESPACE_PATTERN);
            return result;
        }


        /**
         * Reads and returns the next line, excluding the line separator if present.
         *
         * @return the next line, excluding the line separator if present;
         *         {@code null} if no such line
         */
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

        /**
         * Reads and returns the next character.
         *
         * @return the next {@code char}
         * @throws NoSuchElementException if standard input is empty
         */
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

        /**
         * Reads and returns the remainder of the input, as a string.
         *
         * @return the remainder of the input, as a string
         * @throws NoSuchElementException if standard input is empty
         */
        public static String readAll() {
            if (!scanner.hasNextLine())
                return "";

            String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
            // not that important to reset delimiter, since now scanner is empty
            scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
            return result;
        }


        /**
         * Reads the next token from standard input and returns it as a {@code String}.
         *
         * @return the next {@code String}
         * @throws NoSuchElementException if standard input is empty
         */
        public static String readString() {
            try {
                return scanner.next();
            }
            catch (NoSuchElementException e) {
                throw new NoSuchElementException("attempts to read a 'String' value from standard input, "
                        + "but no more tokens are available");
            }
        }

        /**
         * Reads the next token from standard input, parses it as an integer, and returns the integer.
         *
         * @return the next integer on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as an {@code int}
         */
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

        /**
         * Reads the next token from standard input, parses it as a double, and returns the double.
         *
         * @return the next double on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code double}
         */
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

        /**
         * Reads the next token from standard input, parses it as a float, and returns the float.
         *
         * @return the next float on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code float}
         */
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

        /**
         * Reads the next token from standard input, parses it as a long integer, and returns the long integer.
         *
         * @return the next long integer on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code long}
         */
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

        /**
         * Reads the next token from standard input, parses it as a short integer, and returns the short integer.
         *
         * @return the next short integer on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code short}
         */
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

        /**
         * Reads the next token from standard input, parses it as a byte, and returns the byte.
         *
         * @return the next byte on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code byte}
         */
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

        /**
         * Reads the next token from standard input, parses it as a boolean,
         * and returns the boolean.
         *
         * @return the next boolean on standard input
         * @throws NoSuchElementException if standard input is empty
         * @throws InputMismatchException if the next token cannot be parsed as a {@code boolean}:
         *    {@code true} or {@code 1} for true, and {@code false} or {@code 0} for false,
         *    ignoring case
         */
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

        /**
         * Reads all remaining tokens from standard input and returns them as an array of strings.
         *
         * @return all remaining tokens on standard input, as an array of strings
         */
        public static String[] readAllStrings() {
            // we could use readAll.trim().split(), but that's not consistent
            // because trim() uses characters 0x00..0x20 as whitespace
            String[] tokens = WHITESPACE_PATTERN.split(readAll());
            if (tokens.length == 0 || tokens[0].length() > 0)
                return tokens;

            // don't include first token if it is leading whitespace
            String[] decapitokens = new String[tokens.length-1];
            for (int i = 0; i < tokens.length - 1; i++)
                decapitokens[i] = tokens[i+1];
            return decapitokens;
        }

        /**
         * Reads all remaining lines from standard input and returns them as an array of strings.
         * @return all remaining lines on standard input, as an array of strings
         */
        public static String[] readAllLines() {
            ArrayList<String> lines = new ArrayList<String>();
            while (hasNextLine()) {
                lines.add(readLine());
            }
            return lines.toArray(new String[0]);
        }

        /**
         * Reads all remaining tokens from standard input, parses them as integers, and returns
         * them as an array of integers.
         * @return all remaining integers on standard input, as an array
         * @throws InputMismatchException if any token cannot be parsed as an {@code int}
         */
        public static int[] readAllInts() {
            String[] fields = readAllStrings();
            int[] vals = new int[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Integer.parseInt(fields[i]);
            return vals;
        }

        /**
         * Reads all remaining tokens from standard input, parses them as longs, and returns
         * them as an array of longs.
         * @return all remaining longs on standard input, as an array
         * @throws InputMismatchException if any token cannot be parsed as a {@code long}
         */
        public static long[] readAllLongs() {
            String[] fields = readAllStrings();
            long[] vals = new long[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Long.parseLong(fields[i]);
            return vals;
        }

        /**
         * Reads all remaining tokens from standard input, parses them as doubles, and returns
         * them as an array of doubles.
         * @return all remaining doubles on standard input, as an array
         * @throws InputMismatchException if any token cannot be parsed as a {@code double}
         */
        public static double[] readAllDoubles() {
            String[] fields = readAllStrings();
            double[] vals = new double[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = Double.parseDouble(fields[i]);
            return vals;
        }

        //// end: section (2 of 2) of code duplicated from In to StdIn

        // do this once when StdIn is initialized
        static {
            resync();
        }

        /**
         * If StdIn changes, use this to reinitialize the scanner.
         */
        private static void resync() {
            setScanner(new Scanner(new java.io.BufferedInputStream(System.in), CHARSET_NAME));
        }

        private static void setScanner(Scanner scanner) {
            StdIn.scanner = scanner;
            StdIn.scanner.useLocale(LOCALE);
        }

        /**
         * Reads all remaining tokens, parses them as integers, and returns
         * them as an array of integers.
         * @return all remaining integers, as an array
         * @throws InputMismatchException if any token cannot be parsed as an {@code int}
         * @deprecated Replaced by {@link #readAllInts()}.
         */
        @Deprecated
        public static int[] readInts() {
            return readAllInts();
        }

        /**
         * Reads all remaining tokens, parses them as doubles, and returns
         * them as an array of doubles.
         * @return all remaining doubles, as an array
         * @throws InputMismatchException if any token cannot be parsed as a {@code double}
         * @deprecated Replaced by {@link #readAllDoubles()}.
         */
        @Deprecated
        public static double[] readDoubles() {
            return readAllDoubles();
        }

        /**
         * Reads all remaining tokens and returns them as an array of strings.
         * @return all remaining tokens, as an array of strings
         * @deprecated Replaced by {@link #readAllStrings()}.
         */
        @Deprecated
        public static String[] readStrings() {
            return readAllStrings();
        }


        /**
         * Interactive test of basic functionality.
         *
         * @param args the command-line arguments
         */
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

        // force Unicode UTF-8 encoding; otherwise it's system dependent
        private static final Charset CHARSET = StandardCharsets.UTF_8;

        // assume language = English, country = US for consistency with StdIn
        private static final Locale LOCALE = Locale.US;

        // send output here
        private static PrintWriter out;

        // this is called before invoking any methods
        static {
            out = new PrintWriter(new OutputStreamWriter(System.out, CHARSET), true);
        }

        // don't instantiate
        private StdOut() { }

        /**
         * Terminates the current line by printing the line-separator string.
         */
        public static void println() {
            out.println();
        }

        /**
         * Prints an object to this output stream and then terminates the line.
         *
         * @param x the object to print
         */
        public static void println(Object x) {
            out.println(x);
        }

        /**
         * Prints a boolean to standard output and then terminates the line.
         *
         * @param x the boolean to print
         */
        public static void println(boolean x) {
            out.println(x);
        }

        /**
         * Prints a character to standard output and then terminates the line.
         *
         * @param x the character to print
         */
        public static void println(char x) {
            out.println(x);
        }

        /**
         * Prints a double to standard output and then terminates the line.
         *
         * @param x the double to print
         */
        public static void println(double x) {
            out.println(x);
        }

        /**
         * Prints an integer to standard output and then terminates the line.
         *
         * @param x the integer to print
         */
        public static void println(float x) {
            out.println(x);
        }

        /**
         * Prints an integer to standard output and then terminates the line.
         *
         * @param x the integer to print
         */
        public static void println(int x) {
            out.println(x);
        }

        /**
         * Prints a long to standard output and then terminates the line.
         *
         * @param x the long to print
         */
        public static void println(long x) {
            out.println(x);
        }

        /**
         * Prints a short integer to standard output and then terminates the line.
         *
         * @param x the short to print
         */
        public static void println(short x) {
            out.println(x);
        }

        /**
         * Prints a byte to standard output and then terminates the line.
         * <p>
         * To write binary data, see {@link BinaryStdOut}.
         *
         * @param x the byte to print
         */
        public static void println(byte x) {
            out.println(x);
        }

        /**
         * Flushes standard output.
         */
        public static void print() {
            out.flush();
        }

        /**
         * Prints an object to standard output and flushes standard output.
         *
         * @param x the object to print
         */
        public static void print(Object x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a boolean to standard output and flushes standard output.
         *
         * @param x the boolean to print
         */
        public static void print(boolean x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a character to standard output and flushes standard output.
         *
         * @param x the character to print
         */
        public static void print(char x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a double to standard output and flushes standard output.
         *
         * @param x the double to print
         */
        public static void print(double x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a float to standard output and flushes standard output.
         *
         * @param x the float to print
         */
        public static void print(float x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints an integer to standard output and flushes standard output.
         *
         * @param x the integer to print
         */
        public static void print(int x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a long integer to standard output and flushes standard output.
         *
         * @param x the long integer to print
         */
        public static void print(long x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a short integer to standard output and flushes standard output.
         *
         * @param x the short integer to print
         */
        public static void print(short x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a byte to standard output and flushes standard output.
         *
         * @param x the byte to print
         */
        public static void print(byte x) {
            out.print(x);
            out.flush();
        }

        /**
         * Prints a formatted string to standard output, using the specified format
         * string and arguments, and then flushes standard output.
         *
         *
         * @param format the <a href = "http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">format string</a>
         * @param args   the arguments accompanying the format string
         */
        public static void printf(String format, Object... args) {
            out.printf(LOCALE, format, args);
            out.flush();
        }

        /**
         * Prints a formatted string to standard output, using the locale and
         * the specified format string and arguments; then flushes standard output.
         *
         * @param locale the locale
         * @param format the <a href = "http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">format string</a>
         * @param args   the arguments accompanying the format string
         */
        public static void printf(Locale locale, String format, Object... args) {
            out.printf(locale, format, args);
            out.flush();
        }

        /**
         * Unit tests some methods in {@code StdOut}.
         *
         * @param args the command-line arguments
         */
        public static void main(String[] args) {

            // write to stdout
            StdOut.println("Test");
            StdOut.println(17);
            StdOut.println(true);
            StdOut.printf("%.6f\n", 1.0/7.0);
        }

    }

    public final class StdRandom {

        private static Random random;    // pseudo-random number generator
        private static long seed;        // pseudo-random number generator seed

        // static initializer
        static {
            // this is how the seed was set in Java 1.4
            seed = System.currentTimeMillis();
            random = new Random(seed);
        }

        // don't instantiate
        private StdRandom() { }

        /**
         * Sets the seed of the pseudo-random number generator.
         * This method enables you to produce the same sequence of "random"
         * number for each execution of the program.
         * Ordinarily, you should call this method at most once per program.
         *
         * @param s the seed
         */
        public static void setSeed(long s) {
            seed   = s;
            random = new Random(seed);
        }

        /**
         * Returns the seed of the pseudo-random number generator.
         *
         * @return the seed
         */
        public static long getSeed() {
            return seed;
        }

        /**
         * Returns a random real number uniformly in [0, 1).
         *
         * @return a random real number uniformly in [0, 1)
         * @deprecated Replaced by {@link #uniformDouble()}.
         */
        @Deprecated
        public static double uniform() {
            return uniformDouble();
        }

        /**
         * Returns a random real number uniformly in [0, 1).
         *
         * @return a random real number uniformly in [0, 1)
         */
        public static double uniformDouble() {
            return random.nextDouble();
        }

        /**
         * Returns a random integer uniformly in [0, n).
         *
         * @param n number of possible integers
         * @return a random integer uniformly between 0 (inclusive) and {@code n} (exclusive)
         * @throws IllegalArgumentException if {@code n <= 0}
         * @deprecated Replaced by {@link #uniformInt(int n)}.
         */
        @Deprecated
        public static int uniform(int n) {
            return uniformInt(n);
        }

        /**
         * Returns a random integer uniformly in [0, n).
         *
         * @param n number of possible integers
         * @return a random integer uniformly between 0 (inclusive) and {@code n} (exclusive)
         * @throws IllegalArgumentException if {@code n <= 0}
         */
        public static int uniformInt(int n) {
            if (n <= 0) throw new IllegalArgumentException("argument must be positive: " + n);
            return random.nextInt(n);
        }

        /**
         * Returns a random long integer uniformly in [0, n).
         *
         * @param n number of possible {@code long} integers
         * @return a random long integer uniformly between 0 (inclusive) and {@code n} (exclusive)
         * @throws IllegalArgumentException if {@code n <= 0}
         * @deprecated Replaced by {@link #uniformLong(long n)}.
         */
        @Deprecated
        public static long uniform(long n) {
            return uniformLong(n);
        }

        /**
         * Returns a random long integer uniformly in [0, n).
         *
         * @param n number of possible {@code long} integers
         * @return a random long integer uniformly between 0 (inclusive) and {@code n} (exclusive)
         * @throws IllegalArgumentException if {@code n <= 0}
         */
        public static long uniformLong(long n) {
            if (n <= 0L) throw new IllegalArgumentException("argument must be positive: " + n);

            // https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#longs-long-long-long-
            long r = random.nextLong();
            long m = n - 1;

            // power of two
            if ((n & m) == 0L) {
                return r & m;
            }

            // reject over-represented candidates
            long u = r >>> 1;
            while (u + m - (r = u % n) < 0L) {
                u = random.nextLong() >>> 1;
            }
            return r;
        }

        ///////////////////////////////////////////////////////////////////////////
        //  STATIC METHODS BELOW RELY ON JAVA.UTIL.RANDOM ONLY INDIRECTLY VIA
        //  THE STATIC METHODS ABOVE.
        ///////////////////////////////////////////////////////////////////////////

        /**
         * Returns a random real number uniformly in [0, 1).
         *
         * @return     a random real number uniformly in [0, 1)
         * @deprecated Replaced by {@link #uniformDouble()}.
         */
        @Deprecated
        public static double random() {
            return uniformDouble();
        }

        /**
         * Returns a random integer uniformly in [a, b).
         *
         * @param  a the left endpoint
         * @param  b the right endpoint
         * @return a random integer uniformly in [a, b)
         * @throws IllegalArgumentException if {@code b <= a}
         * @throws IllegalArgumentException if {@code b - a >= Integer.MAX_VALUE}
         * @deprecated Replaced by {@link #uniformInt(int a, int b)}.
         */
        @Deprecated
        public static int uniform(int a, int b) {
            return uniformInt(a, b);
        }

        /**
         * Returns a random integer uniformly in [a, b).
         *
         * @param  a the left endpoint
         * @param  b the right endpoint
         * @return a random integer uniformly in [a, b)
         * @throws IllegalArgumentException if {@code b <= a}
         * @throws IllegalArgumentException if {@code b - a >= Integer.MAX_VALUE}
         */
        public static int uniformInt(int a, int b) {
            if ((b <= a) || ((long) b - a >= Integer.MAX_VALUE)) {
                throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
            }
            return a + uniform(b - a);
        }

        /**
         * Returns a random real number uniformly in [a, b).
         *
         * @param  a the left endpoint
         * @param  b the right endpoint
         * @return a random real number uniformly in [a, b)
         * @throws IllegalArgumentException unless {@code a < b}
         * @deprecated Replaced by {@link #uniformDouble(double a, double b)}.
         */
        @Deprecated
        public static double uniform(double a, double b) {
            return uniformDouble(a, b);
        }

        /**
         * Returns a random real number uniformly in [a, b).
         *
         * @param  a the left endpoint
         * @param  b the right endpoint
         * @return a random real number uniformly in [a, b)
         * @throws IllegalArgumentException unless {@code a < b}
         */
        public static double uniformDouble(double a, double b) {
            if (!(a < b)) {
                throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
            }
            return a + uniform() * (b-a);
        }

        /**
         * Returns a random boolean from a Bernoulli distribution with success
         * probability <em>p</em>.
         *
         * @param  p the probability of returning {@code true}
         * @return {@code true} with probability {@code p} and
         *         {@code false} with probability {@code 1 - p}
         * @throws IllegalArgumentException unless {@code 0} &le; {@code p} &le; {@code 1.0}
         */
        public static boolean bernoulli(double p) {
            if (!(p >= 0.0 && p <= 1.0))
                throw new IllegalArgumentException("probability p must be between 0.0 and 1.0: " + p);
            return uniformDouble() < p;
        }

        /**
         * Returns a random boolean from a Bernoulli distribution with success
         * probability 1/2.
         *
         * @return {@code true} with probability 1/2 and
         *         {@code false} with probability 1/2
         */
        public static boolean bernoulli() {
            return bernoulli(0.5);
        }

        /**
         * Returns a random real number from a standard Gaussian distribution.
         *
         * @return a random real number from a standard Gaussian distribution
         *         (mean 0 and standard deviation 1).
         */
        public static double gaussian() {
            // use the polar form of the Box-Muller transform
            double r, x, y;
            do {
                x = uniformDouble(-1.0, 1.0);
                y = uniformDouble(-1.0, 1.0);
                r = x*x + y*y;
            } while (r >= 1 || r == 0);
            return x * Math.sqrt(-2 * Math.log(r) / r);

            // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
            // is an independent random gaussian
        }

        /**
         * Returns a random real number from a Gaussian distribution with mean &mu;
         * and standard deviation &sigma;.
         *
         * @param  mu the mean
         * @param  sigma the standard deviation
         * @return a real number distributed according to the Gaussian distribution
         *         with mean {@code mu} and standard deviation {@code sigma}
         */
        public static double gaussian(double mu, double sigma) {
            return mu + sigma * gaussian();
        }

        /**
         * Returns a random integer from a geometric distribution with success
         * probability <em>p</em>.
         * The integer represents the number of independent trials
         * before the first success.
         *
         * @param  p the parameter of the geometric distribution
         * @return a random integer from a geometric distribution with success
         *         probability {@code p}; or {@code Integer.MAX_VALUE} if
         *         {@code p} is (nearly) equal to {@code 1.0}.
         * @throws IllegalArgumentException unless {@code p >= 0.0} and {@code p <= 1.0}
         */
        public static int geometric(double p) {
            if (!(p >= 0)) {
                throw new IllegalArgumentException("probability p must be greater than 0: " + p);
            }
            if (!(p <= 1.0)) {
                throw new IllegalArgumentException("probability p must not be larger than 1: " + p);
            }
            // using algorithm given by Knuth
            return (int) Math.ceil(Math.log(uniformDouble()) / Math.log(1.0 - p));
        }

        /**
         * Returns a random integer from a Poisson distribution with mean &lambda;.
         *
         * @param  lambda the mean of the Poisson distribution
         * @return a random integer from a Poisson distribution with mean {@code lambda}
         * @throws IllegalArgumentException unless {@code lambda > 0.0} and not infinite
         */
        public static int poisson(double lambda) {
            if (!(lambda > 0.0))
                throw new IllegalArgumentException("lambda must be positive: " + lambda);
            if (Double.isInfinite(lambda))
                throw new IllegalArgumentException("lambda must not be infinite: " + lambda);
            // using algorithm given by Knuth
            // see http://en.wikipedia.org/wiki/Poisson_distribution
            int k = 0;
            double p = 1.0;
            double expLambda = Math.exp(-lambda);
            do {
                k++;
                p *= uniformDouble();
            } while (p >= expLambda);
            return k-1;
        }

        /**
         * Returns a random real number from the standard Pareto distribution.
         *
         * @return a random real number from the standard Pareto distribution
         */
        public static double pareto() {
            return pareto(1.0);
        }

        /**
         * Returns a random real number from a Pareto distribution with
         * shape parameter &alpha;.
         *
         * @param  alpha shape parameter
         * @return a random real number from a Pareto distribution with shape
         *         parameter {@code alpha}
         * @throws IllegalArgumentException unless {@code alpha > 0.0}
         */
        public static double pareto(double alpha) {
            if (!(alpha > 0.0))
                throw new IllegalArgumentException("alpha must be positive: " + alpha);
            return Math.pow(1 - uniformDouble(), -1.0 / alpha) - 1.0;
        }

        /**
         * Returns a random real number from the Cauchy distribution.
         *
         * @return a random real number from the Cauchy distribution.
         */
        public static double cauchy() {
            return Math.tan(Math.PI * (uniformDouble() - 0.5));
        }

        /**
         * Returns a random integer from the specified discrete distribution.
         *
         * @param  probabilities the probability of occurrence of each integer
         * @return a random integer from a discrete distribution:
         *         {@code i} with probability {@code probabilities[i]}
         * @throws IllegalArgumentException if {@code probabilities} is {@code null}
         * @throws IllegalArgumentException if sum of array entries is not (very nearly) equal to {@code 1.0}
         * @throws IllegalArgumentException unless {@code probabilities[i] >= 0.0} for each index {@code i}
         */
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

            // the for loop may not return a value when both r is (nearly) 1.0 and when the
            // cumulative sum is less than 1.0 (as a result of floating-point roundoff error)
            while (true) {
                double r = uniformDouble();
                sum = 0.0;
                for (int i = 0; i < probabilities.length; i++) {
                    sum = sum + probabilities[i];
                    if (sum > r) return i;
                }
            }
        }

        /**
         * Returns a random integer from the specified discrete distribution.
         *
         * @param  frequencies the frequency of occurrence of each integer
         * @return a random integer from a discrete distribution:
         *         {@code i} with probability proportional to {@code frequencies[i]}
         * @throws IllegalArgumentException if {@code frequencies} is {@code null}
         * @throws IllegalArgumentException if all array entries are {@code 0}
         * @throws IllegalArgumentException if {@code frequencies[i]} is negative for any index {@code i}
         * @throws IllegalArgumentException if sum of frequencies exceeds {@code Integer.MAX_VALUE} (2<sup>31</sup> - 1)
         */
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

            // pick index i with probability proportional to frequency
            double r = uniformInt((int) sum);
            sum = 0;
            for (int i = 0; i < frequencies.length; i++) {
                sum += frequencies[i];
                if (sum > r) return i;
            }

            // can't reach here
            assert false;
            return -1;
        }

        /**
         * Returns a random real number from an exponential distribution
         * with rate &lambda;.
         *
         * @param  lambda the rate of the exponential distribution
         * @return a random real number from an exponential distribution with
         *         rate {@code lambda}
         * @throws IllegalArgumentException unless {@code lambda > 0.0}
         */
        public static double exponential(double lambda) {
            if (!(lambda > 0.0))
                throw new IllegalArgumentException("lambda must be positive: " + lambda);
            return -Math.log(1 - uniformDouble()) / lambda;
        }

        /**
         * Returns a random real number from an exponential distribution
         * with rate &lambda;.
         *
         * @param  lambda the rate of the exponential distribution
         * @return a random real number from an exponential distribution with
         *         rate {@code lambda}
         * @throws IllegalArgumentException unless {@code lambda > 0.0}
         * @deprecated Replaced by {@link #exponential(double)}.
         */
        @Deprecated
        public static double exp(double lambda) {
            return exponential(lambda);
        }

        /**
         * Rearranges the elements of the specified array in uniformly random order.
         *
         * @param  a the array to shuffle
         * @throws IllegalArgumentException if {@code a} is {@code null}
         */
        public static void shuffle(Object[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);     // between i and n-1
                Object temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Rearranges the elements of the specified array in uniformly random order.
         *
         * @param  a the array to shuffle
         * @throws IllegalArgumentException if {@code a} is {@code null}
         */
        public static void shuffle(double[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);     // between i and n-1
                double temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Rearranges the elements of the specified array in uniformly random order.
         *
         * @param  a the array to shuffle
         * @throws IllegalArgumentException if {@code a} is {@code null}
         */
        public static void shuffle(int[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);     // between i and n-1
                int temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Rearranges the elements of the specified array in uniformly random order.
         *
         * @param  a the array to shuffle
         * @throws IllegalArgumentException if {@code a} is {@code null}
         */
        public static void shuffle(char[] a) {
            validateNotNull(a);
            int n = a.length;
            for (int i = 0; i < n; i++) {
                int r = i + uniformInt(n-i);     // between i and n-1
                char temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Rearranges the elements of the specified subarray in uniformly random order.
         *
         * @param  a the array to shuffle
         * @param  lo the left endpoint (inclusive)
         * @param  hi the right endpoint (exclusive)
         * @throws IllegalArgumentException if {@code a} is {@code null}
         * @throws IllegalArgumentException unless {@code (0 <= lo) && (lo < hi) && (hi <= a.length)}
         *
         */
        public static void shuffle(Object[] a, int lo, int hi) {
            validateNotNull(a);
            validateSubarrayIndices(lo, hi, a.length);

            for (int i = lo; i < hi; i++) {
                int r = i + uniformInt(hi-i);     // between i and hi-1
                Object temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Rearranges the elements of the specified subarray in uniformly random order.
         *
         * @param  a the array to shuffle
         * @param  lo the left endpoint (inclusive)
         * @param  hi the right endpoint (exclusive)
         * @throws IllegalArgumentException if {@code a} is {@code null}
         * @throws IllegalArgumentException unless {@code (0 <= lo) && (lo < hi) && (hi <= a.length)}
         */
        public static void shuffle(double[] a, int lo, int hi) {
            validateNotNull(a);
            validateSubarrayIndices(lo, hi, a.length);

            for (int i = lo; i < hi; i++) {
                int r = i + uniformInt(hi-i);     // between i and hi-1
                double temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Rearranges the elements of the specified subarray in uniformly random order.
         *
         * @param  a the array to shuffle
         * @param  lo the left endpoint (inclusive)
         * @param  hi the right endpoint (exclusive)
         * @throws IllegalArgumentException if {@code a} is {@code null}
         * @throws IllegalArgumentException unless {@code (0 <= lo) && (lo < hi) && (hi <= a.length)}
         */
        public static void shuffle(int[] a, int lo, int hi) {
            validateNotNull(a);
            validateSubarrayIndices(lo, hi, a.length);

            for (int i = lo; i < hi; i++) {
                int r = i + uniformInt(hi-i);     // between i and hi-1
                int temp = a[i];
                a[i] = a[r];
                a[r] = temp;
            }
        }

        /**
         * Returns a uniformly random permutation of <em>n</em> elements.
         *
         * @param  n number of elements
         * @throws IllegalArgumentException if {@code n} is negative
         * @return an array of length {@code n} that is a uniformly random permutation
         *         of {@code 0}, {@code 1}, ..., {@code n-1}
         */
        public static int[] permutation(int n) {
            if (n < 0) throw new IllegalArgumentException("n must be non-negative: " + n);
            int[] perm = new int[n];
            for (int i = 0; i < n; i++)
                perm[i] = i;
            shuffle(perm);
            return perm;
        }

        /**
         * Returns a uniformly random permutation of <em>k</em> of <em>n</em> elements.
         *
         * @param  n number of elements
         * @param  k number of elements to select
         * @throws IllegalArgumentException if {@code n} is negative
         * @throws IllegalArgumentException unless {@code 0 <= k <= n}
         * @return an array of length {@code k} that is a uniformly random permutation
         *         of {@code k} of the elements from {@code 0}, {@code 1}, ..., {@code n-1}
         */
        public static int[] permutation(int n, int k) {
            if (n < 0) throw new IllegalArgumentException("n must be non-negative: " + n);
            if (k < 0 || k > n) throw new IllegalArgumentException("k must be between 0 and n: " + k);
            int[] perm = new int[k];
            for (int i = 0; i < k; i++) {
                int r = uniformInt(i+1);    // between 0 and i
                perm[i] = perm[r];
                perm[r] = i;
            }
            for (int i = k; i < n; i++) {
                int r = uniformInt(i+1);    // between 0 and i
                if (r < k) perm[r] = i;
            }
            return perm;
        }

        // throw an IllegalArgumentException if x is null
        // (x can be of type Object[], double[], int[], ...)
        private static void validateNotNull(Object x) {
            if (x == null) {
                throw new IllegalArgumentException("argument must not be null");
            }
        }

        // throw an exception unless 0 <= lo <= hi <= length
        private static void validateSubarrayIndices(int lo, int hi, int length) {
            if (lo < 0 || hi > length || lo > hi) {
                throw new IllegalArgumentException("subarray indices out of bounds: [" + lo + ", " + hi + ")");
            }
        }

        /**
         * Unit tests the methods in this class.
         *
         * @param args the command-line arguments
         */
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