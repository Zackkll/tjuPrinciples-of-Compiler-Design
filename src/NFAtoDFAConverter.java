import java.util.*;

public class NFAtoDFAConverter {

    public static DFA convertNfaToDfa(NFA nfa) {
        DFA dfa = new DFA();
        Map<Set<Integer>, Integer> stateMapping = new HashMap<>();
        Queue<Set<Integer>> queue = new LinkedList<>();
        Map<Integer, Map<Character, Integer>> dfaTransitions = new HashMap<>();
        Set<Integer> acceptStates = new HashSet<>();

        int currentStateId = 0;

        // 初始化：计算起始状态的ε闭包
        Set<Integer> startSet = epsilonClosure(Collections.singleton(nfa.startState), nfa.transitions);
        stateMapping.put(startSet, currentStateId++);
        queue.add(startSet);

        while (!queue.isEmpty()) {
            Set<Integer> currentSet = queue.poll();
            int currentDfaState = stateMapping.get(currentSet);

            // 对每个输入符号，计算状态转移
            for (char c : getAllPossibleInputs(nfa.transitions)) {
                Set<Integer> nextSet = move(currentSet, c, nfa.transitions);
                nextSet = epsilonClosure(nextSet, nfa.transitions);  // 计算 nextSet 的ε闭包

                if (!nextSet.isEmpty()) {
                    // 如果 new set 没有对应的 DFA 状态，则创建一个新状态
                    if (!stateMapping.containsKey(nextSet)) {
                        stateMapping.put(nextSet, currentStateId++);
                        queue.add(nextSet);
                    }

                    int nextDfaState = stateMapping.get(nextSet);

                    // 添加 DFA 状态转移
                    dfaTransitions.computeIfAbsent(currentDfaState, k -> new HashMap<>())
                            .put(c, nextDfaState);
                }
            }

            // 标记接受状态
            if (hasAcceptState(currentSet, nfa.acceptStates)) {
                acceptStates.add(currentDfaState);
            }
        }

        // 生成最小化后的 DFA
        dfa.startState = stateMapping.get(startSet);
        dfa.acceptStates = acceptStates;
        dfa.transitions = dfaTransitions;

        return dfa;
    }

    // 计算ε闭包
    private static Set<Integer> epsilonClosure(Set<Integer> states, Map<Integer, Map<Character, Set<Integer>>> transitions) {
        Set<Integer> closure = new HashSet<>(states);
        Stack<Integer> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            int state = stack.pop();
            if (transitions.containsKey(state) && transitions.get(state).containsKey('\0')) {
                for (int nextState : transitions.get(state).get('\0')) {
                    if (closure.add(nextState)) {
                        stack.push(nextState);
                    }
                }
            }
        }
        return closure;
    }

    // 获取 NFA 中所有可能的输入字符
    private static Set<Character> getAllPossibleInputs(Map<Integer, Map<Character, Set<Integer>>> transitions) {
        Set<Character> inputs = new HashSet<>();
        for (Map<Character, Set<Integer>> map : transitions.values()) {
            inputs.addAll(map.keySet());
        }
        inputs.remove('\0');  // 去除空转移符号
        return inputs;
    }

    // 计算从当前状态集通过某个输入符号转移后的新状态集合
    private static Set<Integer> move(Set<Integer> states, char input, Map<Integer, Map<Character, Set<Integer>>> transitions) {
        Set<Integer> result = new HashSet<>();
        for (int state : states) {
            if (transitions.containsKey(state) && transitions.get(state).containsKey(input)) {
                result.addAll(transitions.get(state).get(input));
            }
        }
        return result;
    }

    // 检查一个状态集合是否包含 NFA 的接受状态
    private static boolean hasAcceptState(Set<Integer> states, Set<Integer> acceptStates) {
        for (int state : states) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }
}
