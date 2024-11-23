import java.util.*;

public class DFAMinimizer {

    // 最小化 DFA
    public static DFA minimizeDfa(DFA dfa) {
        // 步骤 1: 初始化划分
        Map<Integer, Integer> stateToGroup = new HashMap<>();
        Set<Integer> acceptStates = dfa.acceptStates;
        Set<Integer> nonAcceptStates = new HashSet<>(dfa.transitions.keySet());
        nonAcceptStates.removeAll(acceptStates);

        // 步骤 2: 初始划分：接受状态和非接受状态
        Set<Integer> acceptGroup = new HashSet<>(acceptStates);
        Set<Integer> nonAcceptGroup = new HashSet<>(nonAcceptStates);

        Map<Integer, Set<Integer>> partition = new HashMap<>();
        partition.put(0, acceptGroup);
        partition.put(1, nonAcceptGroup);

        // 步骤 3: 逐步细分状态
        boolean changed = true;
        while (changed) {
            changed = false;
            Map<Integer, Set<Integer>> newPartition = new HashMap<>();

            // 遍历当前划分中的每个组，按转移细分
            for (Map.Entry<Integer, Set<Integer>> entry : partition.entrySet()) {
                Set<Integer> group = entry.getValue();

                // 统计状态对于每个输入符号的转移结果
                Map<Map<Integer, Integer>, Set<Integer>> newGroups = new HashMap<>();

                for (int state : group) {
                    Map<Integer, Integer> transitionGroup = new HashMap<>();

                    // 查找状态的转移
                    for (Map.Entry<Character, Integer> transition : dfa.transitions.get(state).entrySet()) {
                        Integer targetState = transition.getValue();
                        int groupId = stateToGroup.getOrDefault(targetState, -1);
                        transitionGroup.put(groupId, groupId);
                    }

                    // 将相同转移的状态归到同一组
                    newGroups.computeIfAbsent(transitionGroup, k -> new HashSet<>()).add(state);
                }

                // 如果状态组发生细分，则更新
                if (newGroups.size() > 1) {
                    changed = true;
                    for (Map.Entry<Map<Integer, Integer>, Set<Integer>> groupEntry : newGroups.entrySet()) {
                        int newGroupId = newPartition.size();
                        newPartition.put(newGroupId, groupEntry.getValue());
                        for (int state : groupEntry.getValue()) {
                            stateToGroup.put(state, newGroupId);
                        }
                    }
                } else {
                    newPartition.put(entry.getKey(), group);
                }
            }

            partition = newPartition;
        }

        // 步骤 4: 合并等价状态
        DFA minimizedDfa = new DFA();
        minimizedDfa.startState = 0;

        for (int groupId : partition.keySet()) {
            Set<Integer> group = partition.get(groupId);

            // 使用组的标识符作为新状态
            int newState = group.iterator().next();
            minimizedDfa.transitions.put(newState, new HashMap<>());

            if (acceptStates.contains(newState)) {
                minimizedDfa.acceptStates.add(newState);
            }
        }

        return minimizedDfa;
    }
}
