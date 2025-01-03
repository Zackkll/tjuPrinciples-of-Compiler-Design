package code;
import java.util.*;
import java.util.regex.Pattern;

import static code.Token.*;

public class DFA {

    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public NFA nfa;
    int nowId;
    int startId;
    public String[] tags = {
            "[+]", "-", "[*]", "/", "%", "=", "[(]", "[)]", "[{]", "[}]", ";", ",",
            ">", "[^=]", "<", "[|]", "&", "[_a-zA-Z]", "[_0-9a-zA-Z]", "[^_0-9a-zA-Z]",
            "[1-9]", "[0-9]", "[^0-9]", "!",
            "[0-9]+\\.[0-9]*",":","switch"
    };


    public DFA(NFA nfa) {
        this.nfa = nfa;
    }

    public TreeSet<Node> epsilonClosure(TreeSet<Node> nodeSet) {
        //使用bfs方法计算epsilon闭包,即经过若干条epsilon边可到达的边
        Queue<Node> q = new ArrayDeque<>();
        for (Node n : nodeSet) q.add(n);
        while (!q.isEmpty()) {
            Node top = q.poll();
            for (Edge e : nfa.edgeList) {
                if (top.id == e.fromNodeId && e.tag.equals("epsilon")) {
                    if (nodeSet.contains(nfa.nodeList.get(e.toNodeId)))
                        continue;
                    nodeSet.add(nfa.nodeList.get(e.toNodeId));
                    q.add(nfa.nodeList.get(e.toNodeId));
                }
            }
        }
        return nodeSet;
    }

    public TreeSet<Node> move(TreeSet<Node> nodeSet, String a) {
        //move计算经过一次a边可以到达的点
        TreeSet<Node> res = new TreeSet<>();
        for (Node n : nodeSet) {
            for (Edge e : nfa.edgeList) {
                if (n.id == e.fromNodeId && e.tag.equals(a)) {
                    res.add(nfa.nodeList.get(e.toNodeId));
                }
            }
        }
        return res;
    }

    public void determine() {
        nodeList.add(new Node(0, false, false, ""));
        TreeSet<Node> startNodeSet = new TreeSet<>();
        startNodeSet.add(nfa.nodeList.get(0));
        startNodeSet = epsilonClosure(startNodeSet);//
        ArrayList<TreeSet<Node>> nodeArrayList = new ArrayList<>();
        nodeArrayList.add(startNodeSet);
        int pointer = 0;
        int nowId = 0;
        while (pointer < nodeArrayList.size()) {
            TreeSet<Node> nodeSet = nodeArrayList.get(pointer);
            for (String tag : tags) {
                TreeSet<Node> moveNodeSet = move(epsilonClosure(nodeSet), tag);
                if (moveNodeSet.isEmpty())
                    continue;
                if (!nodeArrayList.contains(moveNodeSet)) {
                    nodeArrayList.add(moveNodeSet);
                    Node firstInSet = moveNodeSet.first();
                    nowId++;
                    nodeList.add(new Node(nowId, firstInSet.isFinal, firstInSet.needRollback, firstInSet.type));
                    edgeList.add(new Edge(pointer, nowId, tag));
                } else {
                    int toNewNodeId = nodeArrayList.indexOf(moveNodeSet);
                    edgeList.add(new Edge(pointer, toNewNodeId, tag));
                }
            }
            pointer++;
        }
        //nodeList = nfa.nodeList;
        //edgeList = nfa.edgeList;
    }

    public void minimize() {
        //最小化算法
        ArrayList<TreeSet<Node>> P = new ArrayList<>();
        TreeSet<Node> F = new TreeSet<>();
        TreeSet<Node> NF = new TreeSet<>();
        for (Node n : nodeList) {
            if (n.isFinal) F.add(n);
            else NF.add(n);
        }
        P.add(F);
        P.add(NF);
        ArrayList<TreeSet<Node>> W = new ArrayList<>();
        W.add(F);
        W.add(NF);
        while (!W.isEmpty()) {
            TreeSet<Node> A = W.get(0);
            W.remove(0);
            TreeSet<Node> premove = new TreeSet<>();
            for (String tag : tags) {
                ArrayList<TreeSet<Node>> X = new ArrayList<>();
                for (TreeSet<Node> p : P) {
                    TreeSet<Node> pTag = new TreeSet<>();
                    TreeSet<Node> pNotTag = new TreeSet<>();
                    for (Node n : p) {
                        for (Edge e : edgeList) {
                            if (e.fromNodeId == n.id && e.tag.equals(tag) || e.toNodeId == n.id && e.tag.equals(tag)) {
                                pTag.add(n);
                            }
                        }
                    }
                    for (Node n : p) {
                        if (!pTag.contains(n)) {
                            pNotTag.add(n);
                        }
                    }
                    if (!pTag.isEmpty() && !pNotTag.isEmpty()) {
                        X.add(pTag);
                        X.add(pNotTag);
                        premove = p;
                        break;
                    }
                }
                P.remove(premove);
                for (TreeSet<Node> x : X) {
                    P.add(x);
                    if (!W.contains(x)) {
                        W.add(x);
                    } else W.remove(x);
                }
            }
        }
        ArrayList<Node> newNodeList = new ArrayList<>();
        ArrayList<Edge> newEdgeList = new ArrayList<>();
        for (int i = 0; i < P.size(); i++) {
            TreeSet<Node> p = P.get(i);
            Node firstInSet = p.first();
            newNodeList.add(new Node(i, firstInSet.isFinal, firstInSet.needRollback, firstInSet.type));
        }
        for (Edge e : edgeList) {
            int fromNodeId = 0;
            int toNodeId = 0;
            for (int i = 0; i < P.size(); i++) {
                TreeSet<Node> p = P.get(i);
                if (p.contains(nodeList.get(e.fromNodeId))) {
                    fromNodeId = i;
                }
                if (p.contains(nodeList.get(e.toNodeId))) {
                    toNodeId = i;
                }
            }
            newEdgeList.add(new Edge(fromNodeId, toNodeId, e.tag));
        }
        edgeList = newEdgeList;
        nodeList = newNodeList;
        startId = 1;
        nowId = 1;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        System.out.println("DFA节点数:" + nodeList.size());
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

    public boolean nextId(String tag) {
        for (Edge edge : edgeList) {
            if (edge.fromNodeId == nowId && Pattern.matches(edge.tag, tag)) {
                // 并将nowId指向新的位置
                nowId = edge.toNodeId;
                // 说明成功找到下一个节点
                return true;
            }
        }
        //System.out.println(tag);
        return false;
    }

    public boolean isFinal(int id) {
        return nodeList.get(id).isFinal;
    }

    public boolean isBackOff(int id) {
        return nodeList.get(id).needRollback;
    }

    public String getTag(int id) {
        return nodeList.get(id).type;
    }

    public String getTokenType(String token, String node_tag) {
        if (node_tag.equals("OP") || node_tag.equals("SE") || node_tag.equals("INT")) {
            return node_tag;
        } else if (node_tag.equals("IDNorKWorOP")) {
            Set<String> keywords = KW.keySet();
            Set<String> ops = OP.keySet();
            if (keywords.contains(token)) {
                return "KW";
            } else if (ops.contains(token)) {
                return "OP";
            } else {
                return "IDN";
            }
        } else {
            return "ERROR";
        }
    }

    public String getTokenNum(String token, String token_type) {
        if (token_type == "IDN" || token_type == "INT") {
            return token;
        } else if (token_type == "KW") {
            return KW.get(token).toString();
        } else if (token_type == "OP") {
            return OP.get(token).toString();
        } else if (token_type == "SE") {
            //System.out.println(token);
            return SE.get(token).toString();
        }
        return "error";
    }

    public void getStart() {
        nowId = startId;
    }
}
