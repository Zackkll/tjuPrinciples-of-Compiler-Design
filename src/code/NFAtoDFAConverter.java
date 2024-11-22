package code;

import java.util.*;

public class NFAtoDFAConverter {
    private int stateIdCounter = 0;

    // 用于将 NFA 的多个状态集合转换为单一的 DFA 状态
    private DFAState getDFAState(Set<NFAState> nfaStates, Map<Set<NFAState>, DFAState> dfaStateMap) {
        if (dfaStateMap.containsKey(nfaStates)) {
            return dfaStateMap.get(nfaStates);
        }
        DFAState dfaState = new DFAState(stateIdCounter++);
        dfaState.isFinal = nfaStates.stream().anyMatch(state -> state.isFinal);
        dfaStateMap.put(nfaStates, dfaState);
        return dfaState;
    }

    // 子集构造算法
    public DFAState convertToDFA(NFAState startState, Set<Character> alphabet) {
        Map<Set<NFAState>, DFAState> dfaStateMap = new HashMap<>();
        Set<NFAState> startSet = new HashSet<>();
        startSet.add(startState);

        Queue<Set<NFAState>> queue = new LinkedList<>();
        queue.add(startSet);

        DFAState dfaStartState = getDFAState(startSet, dfaStateMap);

        while (!queue.isEmpty()) {
            Set<NFAState> currentSet = queue.poll();
            DFAState currentDFAState = getDFAState(currentSet, dfaStateMap);

            for (char symbol : alphabet) {
                Set<NFAState> nextStateSet = new HashSet<>();

                // 收集当前集合的转换
                for (NFAState state : currentSet) {
                    if (state.transitions.containsKey(symbol)) {
                        nextStateSet.addAll(state.transitions.get(symbol));
                    }
                }

                if (!nextStateSet.isEmpty()) {
                    DFAState nextDFAState = getDFAState(nextStateSet, dfaStateMap);
                    currentDFAState.addTransition(symbol, nextDFAState);

                    if (!dfaStateMap.containsKey(nextStateSet)) {
                        queue.add(nextStateSet);
                    }
                }
            }
        }

        return dfaStartState;
    }
}
