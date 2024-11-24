package code;
import java.util.ArrayList;

/*
 * @Description: 构造NFA
 */
public class NFA {
    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();




    public NFA() {
        String[] directOP = {"[+]", "-", "[*]", "/", "%", "="};
        String[] SE = {"[(]", "[)]", "[{]", "[}]", ";", ","};
        nodeList.add(new Node(0, false, false, ""));
        edgeList.add(new Edge(0, 0, "epsilon"));

        for (int i = 1; i < 6; i++) {
            nodeList.add(new Node(i, true, false, "OP"));
            edgeList.add(new Edge(0, i, directOP[i - 1]));
        }

        nodeList.add(new Node(6, false, false, ""));
        edgeList.add(new Edge(0, 6, "="));

        for (int i = 1; i <= 6; i++) {
            nodeList.add(new Node(i + 6, true, false, "SE"));
            edgeList.add(new Edge(0, i + 6, SE[i - 1]));
        }

        nodeList.add(new Node(13, false, false, ""));
        edgeList.add(new Edge(0, 13, ">"));
        nodeList.add(new Node(14, true, false, "OP"));
        edgeList.add(new Edge(13, 14, "="));
        nodeList.add(new Node(15, true, true, "OP"));
        edgeList.add(new Edge(13, 15, "[^=]"));

        nodeList.add(new Node(16, false, false, ""));
        edgeList.add(new Edge(0, 16, "<"));
        nodeList.add(new Node(17, true, false, "OP"));
        edgeList.add(new Edge(16, 17, "="));
        nodeList.add(new Node(18, true, true, "OP"));
        edgeList.add(new Edge(16, 18, "[^=]"));

        nodeList.add(new Node(19, false, false, ""));
        nodeList.add(new Node(20, true, false, "OP"));
        edgeList.add(new Edge(0, 19, "[|]"));
        edgeList.add(new Edge(19, 20, "[|]"));

        nodeList.add(new Node(21, false, false, ""));
        nodeList.add(new Node(22, true, false, "OP"));
        edgeList.add(new Edge(0, 21, "&"));
        edgeList.add(new Edge(21, 22, "&"));

        nodeList.add(new Node(23, false, false, ""));
        nodeList.add(new Node(24, true, true, "IDNorKWorOP"));
        edgeList.add(new Edge(0, 23, "[_a-zA-Z]"));
        edgeList.add(new Edge(23, 23, "[_0-9a-zA-Z]"));
        edgeList.add(new Edge(23, 24, "[^_0-9a-zA-Z]"));

        nodeList.add(new Node(25, false, false, ""));
        nodeList.add(new Node(26, true, true, "INT"));
        edgeList.add(new Edge(0, 25, "[0-9]"));
        edgeList.add(new Edge(25, 25, "[0-9]"));
        edgeList.add(new Edge(25, 26, "[^0-9]"));

        nodeList.add(new Node(27, false, false, ""));
        edgeList.add(new Edge(0, 27, "!"));
        nodeList.add(new Node(28, true, false, "OP"));
        edgeList.add(new Edge(27, 28, "="));

        nodeList.add(new Node(29, true, false, "OP"));
        edgeList.add(new Edge(6, 29, "="));
        nodeList.add(new Node(30, true, true, "OP"));
        edgeList.add(new Edge(6, 30, "[^=]"));

        // 添加对 ":" 的识别
        nodeList.add(new Node(31, false, false, "")); // 中间节点
        nodeList.add(new Node(32, true, true, "SE")); // 最终节点
        edgeList.add(new Edge(0, 31, ":")); // 初始到中间
        edgeList.add(new Edge(31, 32, "epsilon")); // 中间到最终

        // 添加对小数的识别
        nodeList.add(new Node(33, false, false, "")); // 起始小数节点
        nodeList.add(new Node(34, false, false, "")); // 识别到小数点
        nodeList.add(new Node(35, true, true, "FLOAT")); // 最终合法小数节点
        edgeList.add(new Edge(0, 33, "[0-9]")); // 初始到数字
        edgeList.add(new Edge(33, 33, "[0-9]")); // 继续匹配数字
        edgeList.add(new Edge(33, 34, "[.]")); // 数字后的小数点
        edgeList.add(new Edge(34, 35, "[0-9]")); // 小数点后至少一个数字
        edgeList.add(new Edge(35, 35, "[0-9]")); // 小数后继续匹配数字
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Edge e : edgeList) {
            sb.append(e.fromNodeId);
            sb.append("(");
            sb.append(nodeList.get(e.fromNodeId).isFinal + ",");
            sb.append(nodeList.get(e.fromNodeId).needRollback + ",");
            sb.append(nodeList.get(e.fromNodeId).type);
            sb.append(")----- ");
            sb.append(e.tag);
            sb.append(" ----->");
            sb.append(e.toNodeId);
            sb.append("(");
            sb.append(nodeList.get(e.toNodeId).isFinal + ",");
            sb.append(nodeList.get(e.toNodeId).needRollback + ",");
            sb.append(nodeList.get(e.toNodeId).type + ")");
            sb.append("\n");
        }
        return sb.toString();
    }
}
