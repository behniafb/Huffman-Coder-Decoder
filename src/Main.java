// Behnia Farahbod
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            String text = Files.readString(Paths.get("src\\Text.txt"));
            charFrequency(text);
            try {
                initPriQueueAndPrintTree();
                replaceAndSave(bw, text);
                System.out.println("Compressing done!");
            } catch (Exception e) {
                System.out.println("Nothing to compress!");
            }
        }
        // Decrypt
        else {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\Huffman-Decoded-Text.txt", false));
            String cmp = Files.readString(Paths.get("src\\Huffman-Coded-Text.cmp"));
            String firstLine = new BufferedReader(new FileReader("src\\Huffman-Coded-Text.cmp")).readLine();
            String[] firstLineArray = firstLine.split("भा");

            // Read properties of characters

            for (String s : firstLineArray) {
                Node n = new Node();
                n.data = s.charAt(0) + "";
                n.frequency = Integer.parseInt(s.substring(1));
                nodes.add(n);
            }
            try {
                initPriQueueAndPrintTree();

                cmp = cmp.substring(firstLine.length() + 2); // Everything except the first line - Correct until here
                cmp = textToBinaryString(cmp);
                StringBuilder finalText = new StringBuilder();
                int i = 0;
                while (!cmp.isEmpty()) {
                    for (Node node : nodes) {
                        if (i + node.huffmanCode.length() < cmp.length() && node.huffmanCode.equals(cmp.substring(i, i + node.huffmanCode.length()))) {
                            finalText.append(node.data);
                            i = i + node.huffmanCode.length();
                            break;
                        }
                    }
                }
                bw.write(finalText.toString());
            /*int i = 0;
            while (i < cmp.length()) {
                for (Node node : nodes) {
                    if (cmp.substring(i, node.huffmanCode.length()).equals(node.huffmanCode)) {
                        bw.write(node.data);
                        i = i + node.huffmanCode.length();
                        break;
                    }
                }
            }*/

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

    static void replaceAndSave(BufferedWriter bw, String text) throws IOException {
        String binaryText = replaceWithBinary(text, nodes);
        System.out.println("\nThe binary form of the text :\n" + binaryText);

        // Save nodes' properties
        for (Node n : nodes) {
            bw.write(n.data + n.frequency); // Used second method of saving
            bw.write("भा"); // Separator
        }
        bw.newLine();

        // Make the array of individual bits
        boolean[] binaryTextArray = new boolean[binaryText.length()];
        for (int i = 0; i < binaryTextArray.length; i++) {
            if (binaryText.charAt(i) == '1') {
                binaryTextArray[i] = true;
            }
        }

        int numberOfBytes = binaryTextArray.length / 8 + 1;
        for (int i = 0; i < numberOfBytes; ++i) {
            byte temp = 0;
            for (int j = 0; j < 8 && (i * 8) + j < binaryTextArray.length; j++) {
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
    static String textToBinaryString(String text) {

        StringBuilder binaryString = new StringBuilder();
        char[] chars = text.toCharArray();
        for (char c : chars) {
            binaryString.append(charToBinaryForm(c));
        }

        return binaryString.toString();
    }

    private static String charToBinaryForm(char c) {
        int charNumber = c;
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            if (charNumber >= Math.pow(2, i)) {
                charNumber -= Math.pow(2, i);
                sb.append('1');
            } else
                sb.append('0');
        }
        return sb.toString();
    }
}