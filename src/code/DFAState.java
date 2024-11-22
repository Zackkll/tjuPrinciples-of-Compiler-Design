package code;

import java.util.HashMap;
import java.util.Map;

// DFA的状态，只允许单一的转换
public class DFAState {
    int id;
    public boolean isFinal;
    public Map<Character, DFAState> transitions;

    DFAState(int id) {
        this.id = id;
        this.isFinal = false;
        this.transitions = new HashMap<>();
    }

    void addTransition(char symbol, DFAState nextState) {
        transitions.put(symbol, nextState);
    }
}
