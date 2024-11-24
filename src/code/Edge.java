package code;


/*
 * @Description: NFA中的边
 */
public class Edge {
    public int fromNodeId;
    public int toNodeId;
    public String tag;//边上的符号标记

    public Edge(int fromNodeId, int toNodeId, String tag) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.tag = tag;
    }
}
