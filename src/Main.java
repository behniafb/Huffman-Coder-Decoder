// Behnia Farahbod -- with correct Decoder.

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    static ArrayList<Node> nodes = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        System.out.println("What do you want to do?\n1) Encrypt\n2) Decrypt");
        // Encrypt
        if (1 == new Scanner(System.in).nextInt()) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\Huffman-Coded-Text.cmp", false));
            BufferedReader reader = new BufferedReader(new FileReader("src\\Text.txt"));
            StringBuilder text = new StringBuilder();
            do {
                text.append(reader.readLine()).append("\n");
            } while (reader.ready());
            text.delete(text.length() - 1, text.length());
            charFrequency(text.toString());
            try {
                initPriQueueAndPrintTree();
                replaceAndSave(bw, text.toString());
                System.out.println("Compressing done!");
            } catch (Exception e) {
                System.out.println("Nothing to compress!");
            }
        }
        // Decrypt
        else {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\Huffman-Decoded-Text.txt", false));
            BufferedReader reader = new BufferedReader(new FileReader("src\\Huffman-Coded-Text.cmp"));
            StringBuilder lines = new StringBuilder();
            do {
                lines.append(reader.readLine()).append("\n");
            } while (!lines.toString().contains("eof"));
            lines.delete(lines.length() - 4, lines.length());
            StringBuilder cmp = new StringBuilder();
            do {
                cmp.append(reader.readLine());
            } while (reader.ready());// Read until the end
            String[] firstLineArray = lines.toString().split("भा");

            // Read properties of characters
            for (String s : firstLineArray) {
                Node n = new Node();
                n.data = s.charAt(0) + "";
                n.frequency = Integer.parseInt(s.substring(1));
                nodes.add(n);
            }
            try {
                initPriQueueAndPrintTree();

                char[] charArr = cmp.toString().toCharArray();
                byte[] byteArr = new byte[charArr.length];
                for (int i = 0; i < byteArr.length; ++i) {
                    byteArr[i] = (byte) charArr[i];
                }
                cmp = new StringBuilder(bytesToBinaryString(byteArr));

                StringBuilder finalText = new StringBuilder();
                StringBuilder finalBuilder = new StringBuilder();
                int numOfBits = 0;
                for (Node node : nodes) { // تعداد بیتی که باید بخونیم
                    numOfBits += node.frequency * node.huffmanCode.length();
                }

                for (int i = 0; i < numOfBits; i++) {
                    if (i > cmp.length())
                        finalBuilder.append("0");
                    else
                        finalBuilder.append(cmp.charAt(i));
                    for (Node node : nodes) {
                        if (finalBuilder.toString().equals(node.huffmanCode)) {
                            finalText.append(node.data);
                            finalBuilder.delete(0, finalBuilder.length()); // Empty the whole finalBuilder
                            break;
                        }
                    }
                }

                bw.write(finalText.toString());

                bw.close();
                System.out.println("Decoding done !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void initPriQueueAndPrintTree() throws Exception {
        PriorityQueue<Node> pQueue = new PriorityQueue<>(new NodeComparator());
        pQueue.addAll(nodes);
        Node head = buildHuffmanTree(pQueue);
        System.out.println("The tree :");
        Printer.printNode(head);
        if (nodes.size() == 1)
            head.huffmanCode = "1";
        else if (nodes.size() == 0) throw new Exception("Nothing to compress!");
        else
            setHuffmanCode(nodes);
    }

    // Finds the frequency of characters in a string.
    static void charFrequency(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            Node temp = getNode(c);
            if (temp != null) temp.frequency++;
            else {
                Node newNode = new Node();
                newNode.data = c + "";
                newNode.frequency = 1;
                nodes.add(newNode);
            }
        }
    }

    // If the nodes array contains a node with data of c, returns it. Otherwise, returns null.
    private static Node getNode(char c) {
        for (Node node : nodes) {
            if (node.data.equals(c + "")) return node;
        }
        return null;
    }

    static Node buildHuffmanTree(PriorityQueue<Node> queue) {
        while (queue.size() > 1) {
            Node p = queue.poll();
            Node q = queue.poll();
            Node r = new Node();
            r.left = p;
            r.right = q;
            p.parent = r;
            q.parent = r;
            r.data = "-"; // To see the tree better
            r.frequency = p.frequency + q.frequency;
            queue.add(r);
        }
        return queue.poll();
    }

    static void setHuffmanCode(ArrayList<Node> nodes) {

        for (Node node : nodes) {
            // for each node
            StringBuilder code = new StringBuilder();
            Node p = node;
            while (p.parent != null) {
                if (p == p.parent.left) code.insert(0, 0);
                else code.insert(0, 1);
                p = p.parent;
            }
            node.huffmanCode = code.toString();
        }
    }

    // For encoding
    static void replaceAndSave(BufferedWriter bw, String text) throws IOException {
        String binaryText = replaceWithBinary(text, nodes);
        System.out.println("\nThe binary form of the text :\n" + binaryText);

        // Save nodes' properties
        StringBuilder sb = new StringBuilder();
        for (Node n : nodes) {
            sb.append(n.data).append(n.frequency).append("भा"); // Used second method of saving
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("eof\n");
        bw.write(sb.toString());

        // Make the array of individual bits
        boolean[] binaryTextArray = new boolean[binaryText.length()];
        for (int i = 0; i < binaryTextArray.length; i++) {
            if (binaryText.charAt(i) == '1') {
                binaryTextArray[i] = true;
            }
        }

        int numberOfBytes = binaryTextArray.length / 8 + 1;
        for (int i = 0; i < numberOfBytes; i++) {
            int temp = 0;
            for (int j = 0; j <= 7 && (i * 8) + j < binaryTextArray.length; j++) {
                if (binaryTextArray[(i * 8) + j]) {
                    temp += Math.pow(2, j);
                }
            }// each bit of a byte
            bw.write((char) temp);
        }// each byte

        bw.close();
    }

    static private String replaceWithBinary(String text, ArrayList<Node> nodes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            for (Node node : nodes) {
                if ((text.charAt(i) + "").equals(node.data)) {
                    sb.append(node.huffmanCode);
                    break;
                }
            }//each node
        }// each character of text
        return sb.toString();
    }

    // For decoding
    static String bytesToBinaryString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            boolean[] temp = booleanArrayFromByte(b);
            for (boolean bool : temp) {
                if (bool) {
                    result.append("1");
                } else {
                    result.append("0");
                }
            }
        }
        return result.toString();
    }

    static boolean[] booleanArrayFromByte(byte b) {
        boolean[] array = new boolean[8];
        array[0] = ((b & 0b1) != 0);
        array[1] = ((b & 0b10) != 0);
        array[2] = ((b & 0b100) != 0);
        array[3] = ((b & 0b1000) != 0);
        array[4] = ((b & 0b10000) != 0);
        array[5] = ((b & 0b100000) != 0);
        array[6] = ((b & 0b1000000) != 0);
        array[7] = ((b & 0b10000000) != 0);
        return array;
    }
}