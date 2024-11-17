package code;

import java.util.*;

public class NFA {
    public NFAState startState;
    public Set<NFAState> finalStates;

    NFA(NFAState startState, Set<NFAState> finalStates) {
        this.startState = startState;
        this.finalStates = finalStates;
    }
}

