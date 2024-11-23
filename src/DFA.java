import java.util.*;

public class DFA {
    int startState; // 起始状态
    Set<Integer> acceptStates; // 接受状态集合
    Map<Integer, Map<Character, Integer>> transitions; // 转移表

    public DFA() {
        this.startState = 0;
        this.acceptStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    // 添加状态转移
    public void addTransition(int fromState, char input, int toState) {
        transitions.computeIfAbsent(fromState, k -> new HashMap<>()).put(input, toState);
    }

    // 获取所有可能的输入字符
    public Set<Character> getAllPossibleInputs() {
        Set<Character> inputs = new HashSet<>();
        for (Map<Character, Integer> stateTransitions : transitions.values()) {
            inputs.addAll(stateTransitions.keySet());
        }
        return inputs;
    }
}
