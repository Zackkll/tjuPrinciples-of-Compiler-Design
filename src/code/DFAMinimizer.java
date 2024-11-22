package code;

import java.util.*;

public class DFAMinimizer {
    // Hopcroft's Algorithm for DFA Minimization
    public DFAState minimizeDFA(DFAState startState, Set<Character> alphabet) {
        Map<DFAState, Integer> stateGroup = new HashMap<>();
        Map<Integer, Set<DFAState>> partition = new HashMap<>();

        Set<DFAState> accepting = new HashSet<>();
        Set<DFAState> nonAccepting = new HashSet<>();

        // 初始划分为接受状态和非接受状态
        Queue<Set<DFAState>> queue = new LinkedList<>();
        for (DFAState state : collectStates(startState)) {
            if (state.isFinal) {
                accepting.add(state);
            } else {
                nonAccepting.add(state);
            }
        }

        partition.put(0, accepting);
        partition.put(1, nonAccepting);

        queue.add(accepting);
        queue.add(nonAccepting);

        while (!queue.isEmpty()) {
            Set<DFAState> currentGroup = queue.poll();

            for (char symbol : alphabet) {
                // 按 symbol 重新划分状态
                Map<Integer, Set<DFAState>> newPartition = new HashMap<>();

                for (DFAState state : currentGroup) {
                    DFAState target = state.transitions.get(symbol);
                    int groupId = stateGroup.getOrDefault(target, -1);

                    newPartition.putIfAbsent(groupId, new HashSet<>());
                    newPartition.get(groupId).add(state);
                }

                for (Set<DFAState> group : newPartition.values()) {
                    if (!partition.containsValue(group)) {
                        partition.put(partition.size(), group);
                        queue.add(group);
                    }
                }
            }
        }

        // 重新生成最小化的 DFA
        Map<Integer, DFAState> minimizedDFAStates = new HashMap<>();
        for (Map.Entry<Integer, Set<DFAState>> entry : partition.entrySet()) {
            DFAState newState = new DFAState(entry.getKey());
            newState.isFinal = entry.getValue().stream().anyMatch(state -> state.isFinal);
            minimizedDFAStates.put(entry.getKey(), newState);
        }

        // 重新链接转换
        for (Map.Entry<Integer, Set<DFAState>> entry : partition.entrySet()) {
            DFAState newState = minimizedDFAStates.get(entry.getKey());

            for (DFAState state : entry.getValue()) {
                for (Map.Entry<Character, DFAState> trans : state.transitions.entrySet()) {
                    char symbol = trans.getKey();
                    int targetGroupId = stateGroup.get(trans.getValue());
                    newState.addTransition(symbol, minimizedDFAStates.get(targetGroupId));
                }
            }
        }

        return minimizedDFAStates.get(0); // 返回最小化后的起始状态
    }

    // 收集DFA中的所有状态
    private Set<DFAState> collectStates(DFAState startState) {
        Set<DFAState> states = new HashSet<>();
        Queue<DFAState> queue = new LinkedList<>();
        queue.add(startState);

        while (!queue.isEmpty()) {
            DFAState state = queue.poll();
            if (!states.contains(state)) {
                states.add(state);
                queue.addAll(state.transitions.values());
            }
        }

        return states;
    }
}
