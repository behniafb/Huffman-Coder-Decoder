public class Node {
    // Fields
    Node parent = null;
    Node left = null;
    Node right = null;
    String data = "";
    int frequency = 0;
    String huffmanCode = "";

    // default constructor
    public Node() {
    }

    // This constructor copies the argument Node's data, used by method "setHuffmanCode".
    public Node(Node n) {
        parent = n.parent;
        left = n.left;
        right = n.right;
        data = n.data;
        frequency = n.frequency;
        huffmanCode = n.huffmanCode;
    }
}
