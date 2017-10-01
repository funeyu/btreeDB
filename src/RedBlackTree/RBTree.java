package RedBlackTree;

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
    }

    /**
     * 每次添加的时候都要重新保证红黑树的balance
     * @param from 为新加的红节点
     */
    private void balance(Node from) {

        // 新加红节点，添加在父节点的left 子节点并且其是black，就不做处理
        if(from.isLeft() && from.Parent().Color() == Colors.BLACK) {
            return ;
        }
        // 添加右侧的red节点，先旋转
        if(!from.isLeft()) {

        }

    }

    /**
     * 左旋转n节点和其父节点
     * @param n
     */
    private void rotateLeft(Node n) {

        Node parent = n.Parent();
        n.changeColor(parent.Color());
        parent.changeColor(Colors.RED);
        parent.setLeft(n.Left());
        n.setRight(parent);
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
