package RedBlackTree;

/**
 * Created by fuheyu on 2017/9/24.
 */

/**
 * 红黑树的节点
 */
public class Node {

    private Node left;

    private Node right;

    private Node parent;

    private boolean color;

    private Node(boolean color) {

        this.color = color;
    }

    public static Node init(boolean color, Node parent) {

        Node node = new Node(color);
        node.parent = parent;

        return node;
    }

    public Node changeColor(boolean color) {

        this.color = color;
        return this;
    }

    public Node Parent() {

        return parent;
    }

    public Node Left() {

        return left;
    }

    public Node Right() {

        return right;
    }

    public Node setLeft(Node node) {

        left = node;
        return this;
    }

    public Node setRight(Node node) {

        right = node;
        return this;
    }

}
