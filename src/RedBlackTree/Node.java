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

    private String value;

    private int key;

    private Node(boolean color) {

        this.color = color;
    }

    public static Node init(boolean color, Node parent) {

        Node node = new Node(color);
        node.parent = parent;

        return node;
    }

    public Node setValue(String value) {

        this.value = value;
        return this;
    }

    public String getValue() {

        return this.value;
    }

    public Node setKey(int key) {

        this.key = key;
        return this;
    }

    public int getKey() {

        return this.key;
    }

    public boolean isEmpty() {

        return left == null && right == null;
    }

    public boolean isNoKey() {

        return this.key == 0;
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
        if(node != null ) {
            node.isLeft = true;
            node.parent = this;
        }
        return this;
    }

    public Node setRight(Node node) {

        right = node;
        if(node != null) {
            node.isLeft = false;
            node.parent = this;
        }

        return this;
    }

    public void setParent(Node node) {

        parent = node;
    }


    public boolean Color() {

        return color;
    }

    public String toString() {

        return "is" + (isLeft ? " left node；" : " right node;") + "value:" + value;
    }
}
