import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class Printer {

    public static void printNode(Node root) {
        int maxLevel = Printer.maxLevel(root);
        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static void printNodeInternal(List<Node> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || Printer.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        Printer.printWhitespaces(firstSpaces);

        List<Node> newNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node != null) {
                if (node.data.equals("\n"))
                    System.out.print("nL" + ((node.frequency == '-') ? "-" + node.frequency : ""));
                else if (node.data.equals(" "))
                    System.out.print("Sp" + ((node.frequency == '-') ? "-" + node.frequency : ""));
                else
                    System.out.print(node.data + ((node.frequency == '-') ? "-" + node.frequency : ""));
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            Printer.printWhitespaces(betweenSpaces);
        }
        System.out.println();

        for (int i = 1; i <= endLines; i++) {
            for (Node node : nodes) {
                Printer.printWhitespaces(firstSpaces - i);
                if (node == null) {
                    Printer.printWhitespaces(endLines + endLines + i + 1);
                    continue;
                }

                if (node.left != null)
                    System.out.print("/");
                else
                    Printer.printWhitespaces(1);

                Printer.printWhitespaces(i + i - 1);

                if (node.right != null)
                    System.out.print("\\");
                else
                    Printer.printWhitespaces(1);

                Printer.printWhitespaces(endLines + endLines - i);
            }

            System.out.println();
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static int maxLevel(Node node) {
        if (node == null)
            return 0;

        return Math.max(Printer.maxLevel(node.left), Printer.maxLevel(node.right)) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }

}
