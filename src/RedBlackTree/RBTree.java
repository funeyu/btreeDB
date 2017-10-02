package RedBlackTree;

import java.awt.*;

/**
 * Created by fuheyu on 2017/9/24.
 */
public class RBTree {

    private Node root = Node.init(Colors.BLACK, null);

    public RBTree() {}

    public void put(int value) {

        if(root.isEmpty()) {
            root.setValue(value);
            return ;
        }

        Node empty = findPlace(value, root);
        reBalance(empty.Parent());
    }

    /**
     * 每次添加的时候都要重新保证红黑树的balance
     * @param from 为新加的红节点
     */
    private void reBalance(Node from) {

        if(from == null) {
            return ;
        }
        // 新加红节点，添加在父节点的left 子节点并且其是black，就不做处理
        if(from.isLeft() && from.Parent().Color() == Colors.BLACK) {
            return ;
        }
        // 左右两节点都是Red
        if(from.Left().Color() == Colors.RED && from.Right().Color() == Colors.RED) {
            from.Left().changeColor(Colors.BLACK);
            from.Right().changeColor(Colors.BLACK);
            from.changeColor(Colors.RED);
            reBalance(from.Parent());
        }
        // 自己为Red节点，或者左右节点有个为Red节点
        if(from.Color() == Colors.RED && (from.Left().Color() == Colors.RED || from.Right().Color() == Colors.RED)) {
            if(from.Left().Color() == Colors.RED) {
                from.setParent(from.Parent().Parent());

                from.setRight(from.Parent());

                reBalance(from.Parent());
            } else {
                from.Right().setRight(from.Parent());
                from.Right().setLeft(from);
                from.Right().changeColor(Colors.RED);

                from.changeColor(Colors.BLACK);
                from.setRight(from.Right().Left());
                from.setParent(from.Right());

                from.Parent().changeColor(Colors.BLACK);
                from.Parent().setLeft(from.Right().Right());
                from.Parent().setParent(from.Right());
            }
        }
        if(from.Right().Color() == Colors.RED) {

            rotateLeft(from.Right());
            return ;
        }

    }

    /**
     * 1）start节点的left right节点都是red
     * 2）或者该节点为red，left 或者right 为red节点
     * 以上两种情况需要提升
     * @param start
     */
    private void lift(Node start) {


    }

    /**
     * 左旋转n节点和其父节点
     * @param n
     */
    private void rotateLeft(Node n) {

        Node parent = n.Parent();
        n.changeColor(parent.Color());
        n.setParent(parent.Parent());
        parent.changeColor(Colors.RED);
        parent.setLeft(n.Left());
        n.setRight(parent);
    }

    /**
     * 右旋转n节点及其父节点
     * @param n
     */
    private void rotateRight(Node n) {


    }

    /**
     * 找到该插入的节点位置
     * @param inserValue
     * @param curr
     * @return
     */
    private Node findPlace(int inserValue, Node curr) {

        if(curr.getValue() >= inserValue) {

            if(curr.Left() == null) {
                Node left = Node.init(Colors.RED, curr);
                left.setValue(inserValue);
                curr.setLeft(left);
                return left;
            }
            return findPlace(inserValue, curr.Left());
        }
        else {
            if(curr.Right() == null) {
                Node right = Node.init(Colors.RED, curr);
                right.setValue(inserValue);
                curr.setRight(right);
                return right;
            }
            return findPlace(inserValue, curr.Right());
        }
    }


    public static void main(String[] args) {

        RBTree tree = new RBTree();
        tree.put(9);
        tree.put(10);

        Node room = tree.findPlace(8, tree.root);
        System.out.println(room);
    }

}
