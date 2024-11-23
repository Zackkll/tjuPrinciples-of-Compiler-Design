import java.util.*;

public class NFA {
    public int startState;  // 起始状态 (public 访问权限，或使用 getter 方法)
    public Set<Integer> acceptStates;  // 接受状态集合
    public Map<Integer, Map<Character, Set<Integer>>> transitions;  // 状态转移表

    public NFA() {
        this.startState = 0;  // 默认起始状态为 0
        this.acceptStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    // 添加状态转移
    public void addTransition(int fromState, char input, int toState) {
        transitions.computeIfAbsent(fromState, k -> new HashMap<>())
                .computeIfAbsent(input, k -> new HashSet<>())
                .add(toState);
    }

    // 添加空转移（ε 转移）
    public void addEpsilonTransition(int fromState, int toState) {
        addTransition(fromState, '\0', toState);
    }
}
