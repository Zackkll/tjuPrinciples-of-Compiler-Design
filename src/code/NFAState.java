package code;

import java.util.*;

// 状态类
public class NFAState {
    int id;
    boolean isFinal;
    Map<Character, Set<NFAState>> transitions; // NFA的状态会有多个转换

    NFAState(int id) {
        this.id = id;
        this.isFinal = false;
        this.transitions = new HashMap<>();
    }

    void addTransition(char symbol, NFAState nextState) {
        transitions.putIfAbsent(symbol, new HashSet<>());
        transitions.get(symbol).add(nextState);
    }
}

