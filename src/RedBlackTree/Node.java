package RedBlackTree;

/**
 * Created by fuheyu on 2017/9/24.
 */

/**
 * 红黑树的节点
 */
public class Node {

    private Node left = null;

    private Node right = null;

    private Node parent;

    private boolean isLeft;

    private boolean color;

    private int value;

    private Node(boolean color) {

        this.color = color;
    }

    public static Node init(boolean color, Node parent) {

        Node node = new Node(color);
        node.parent = parent;

        return node;
    }

    public Node setValue(int value) {

        this.value = value;
        return this;
    }

    public int getValue() {

        return this.value;
    }

    public boolean isEmpty() {

        return left == null && right == null;
    }

    public boolean isRoot() {

        return parent == null;
    }

    public boolean isLeft() {

        return isLeft;
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
        node.isLeft = true;
        return this;
    }

    public Node setRight(Node node) {

        right = node;
        node.isLeft = false;
        return this;
    }

    public boolean Color() {

        return color;
    }

    public String toString() {

        return "is" + (isLeft ? " left node；" : "right node;") + "value:" + value;
    }
}
