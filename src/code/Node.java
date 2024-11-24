package code;
/*
 * @Description: NFA图中的点
 */
public class Node implements Comparable<Node> {
    public int id;
    public boolean isFinal;
    public boolean needRollback;
    public String type;

    public Node(int id, boolean isFinal, boolean needRollback, String type) {
        this.id = id;
        this.isFinal = isFinal;
        this.needRollback = needRollback;
        this.type = type;
    }


    @Override
    public int compareTo(Node o) {
        return this.id - o.id;
    }
}
