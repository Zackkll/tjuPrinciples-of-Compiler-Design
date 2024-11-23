package code;
/*
 * @Description: NFA图中的点
 */
public class Node implements Comparable<Node> {
    public int id;
    public boolean isLast;
    public boolean needRollback;
    public String type;

    public Node(int id, boolean isLast, boolean needRollback, String type) {
        this.id = id;
        this.isLast = isLast;
        this.needRollback = needRollback;
        this.type = type;
    }


    @Override
    public int compareTo(Node o) {
        return this.id - o.id;
    }
}
