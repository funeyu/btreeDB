package RedBlackTree;


/**
 * Created by fuheyu on 2017/9/24.
 */
public class RBTree {

    private Node root = Node.init(Colors.BLACK, null);

    public RBTree() {}

    public void put(int key, String value) {

        // 第一次添加数据
        if(root.isEmpty() && root.getValue() == null) {
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
        } while(!curr.isNoKey());

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
        // 自己为Red节点，或者左右节点有个为Red节点
        else if(from.Color() == Colors.RED && ((from.Left() !=null && from.Left().Color() == Colors.RED)
                                                || (from.Right() != null && from.Right().Color() == Colors.RED))) {
            if((from.Left() !=null && from.Left().Color() == Colors.RED)) {
                from.setParent(from.Parent().Parent());
                from.setRight(from.Parent());

                if(from.Parent().isRoot()) {
                    root = from;
                    root.Left().changeColor(Colors.BLACK);
                    root.Right().changeColor(Colors.BLACK);
                } else {
                    from.changeColor(Colors.RED);
                    reBalance(from.Parent());
                }
            } else {

                from.setRight(from.Right().Left());
                from.Parent().setLeft(from.Right().Right());
                from.changeColor(Colors.BLACK);
                from.Parent().changeColor(Colors.BLACK);

                from.Right().setLeft(from);
                from.Right().setRight(from.Parent());

                if(from.Parent().isRoot()) {
                    from.changeColor(Colors.BLACK);
                    root = from ;
                } else {
                    from.changeColor(Colors.RED);
                }
            }
        }
        // 左右两节点都是Red
        else if((from.Left() != null && from.Left().Color() == Colors.RED) &&
                (from.Right() != null && from.Right().Color() == Colors.RED)) {
            from.Left().changeColor(Colors.BLACK);
            from.Right().changeColor(Colors.BLACK);

            if(from.isRoot()) {
                from.changeColor(Colors.BLACK);
                root = from;
            } else {
                from.changeColor(Colors.RED);
                reBalance(from.Parent());
            }
        }
        else if(from.Right() !=null && from.Right().Color() == Colors.RED) {

            rotateLeft(from.Right());
            if(from.Parent().isRoot()) {
                root = from.Parent();
            } else {
                reBalance(from.Right());
            }
        }

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

        parent.setRight(n.Left());

        n.setLeft(parent);
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
        tree.put(70, "djafa");


        Node n = tree.get(32);
        System.out.println("n" + n.getValue());
    }

}
