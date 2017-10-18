package RedBlackTree;

import java.awt.*;

/**
 * Created by fuheyu on 2017/9/24.
 */
public class RBTree {

    private Node root = Node.init(Colors.BLACK, null);

    public RBTree() {}

    public void put(int key, String value) {

        if(root.isEmpty()) {
            root.setKey(key);
            root.setValue(value);
            return ;
        }

        Node empty = findPlace(key, value, root);
        reBalance(empty.Parent());
    }

    /**
     * 根据key去查询相应的value值
     * @param key
     * @return
     */
    public Node get(int key) {

        Node curr = root;
        do {
            if(curr.getKey() == key) {
                return curr;
            }
            else if(curr.getKey() > key) {
                curr = curr.Left();
            } else {
                curr = curr.Right();
            }
        } while(!curr.isEmpty());

        return null;
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
     * @param inserKey
     * @param curr
     * @return
     */
    private Node findPlace(int inserKey, String value, Node curr) {

        if(curr.getKey() >= inserKey) {

            if(curr.Left() == null) {
                Node left = Node.init(Colors.RED, curr);
                left.setKey(inserKey);
                left.setValue(value);
                curr.setLeft(left);
                return left;
            }
            return findPlace(inserKey, value, curr.Left());
        }
        else {
            if(curr.Right() == null) {
                Node right = Node.init(Colors.RED, curr);
                right.setValue(value);
                right.setKey(inserKey);
                curr.setRight(right);
                return right;
            }
            return findPlace(inserKey, value , curr.Right());
        }
    }


    public static void main(String[] args) {

        RBTree tree = new RBTree();
        tree.put(9, "java");
        tree.put(10, "nodejs");
        tree.put(2, "eclipse");
        tree.put(3234, "hello");
        tree.put(32, "nodejsdd");
        tree.put(3, "eele");
        tree.put(0, "djafa");


        Node n = tree.get(3234);
        System.out.println("n" + n.getValue());
    }

}
