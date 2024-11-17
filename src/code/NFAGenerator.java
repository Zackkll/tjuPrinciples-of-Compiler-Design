package code;

import java.util.HashSet;
import java.util.Set;

// NFA 生成器：将正则表达式生成一个简单的 NFA
public class NFAGenerator {
    private int stateIdCounter = 0;

    // 创建一个新的状态
    private NFAState newState() {
        return new NFAState(stateIdCounter++);
    }

    // 生成一个只接受单个字符的 NFA
    public NFA generateSingleCharNFA(char symbol) {
        NFAState start = newState();
        NFAState end = newState();
        start.addTransition(symbol, end);
        Set<NFAState> finalStates = new HashSet<>();
        finalStates.add(end);
        return new NFA(start, finalStates);
    }

    // 将多个单字符的 NFA 合并成一个多字符的 NFA
    public NFA generateFromRegex(String regex) {
        // 这里只处理简单的情况，如直接串联
        NFA combinedNFA = null;
        for (char ch : regex.toCharArray()) {
            NFA singleNFA = generateSingleCharNFA(ch);
            if (combinedNFA == null) {
                combinedNFA = singleNFA;
            } else {
                combinedNFA = concatenate(combinedNFA, singleNFA);
            }
        }
        return combinedNFA;
    }

    // 简单的连接两个 NFA
    private NFA concatenate(NFA first, NFA second) {
        for (NFAState finalState : first.finalStates) {
            finalState.addTransition('\0', second.startState); // 空转换
        }
        first.finalStates = second.finalStates;
        return new NFA(first.startState, first.finalStates);
    }
}
