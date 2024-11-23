import java.util.*;
public class NFAConstructor {

    // 关键字 NFA（如 "int", "float" 等）
    public static NFA buildKeywordNFA(String keyword) {
        return buildStringNFA(keyword); // 关键字为特定字符串
    }

    // 运算符 NFA（如 "+", "-", "==" 等）
    public static NFA buildOperatorNFA(String operator) {
        return buildStringNFA(operator); // 运算符为单一字符或多个字符的字符串
    }

    // 界符 NFA（如 "(", ")", "{", "}", ";" 等）
    public static NFA buildSeparatorNFA(String separator) {
        return buildStringNFA(separator); // 界符为字符或符号
    }
    public static NFA buildSingleCharacterNFA(char c) {
        NFA nfa = new NFA();
        nfa.startState = 0;  // 设置起始状态为 0
        nfa.acceptStates.add(1);  // 设置接受状态为 1
        nfa.addTransition(0, c, 1);  // 从状态 0 用字符 c 转移到状态 1
        return nfa;
    }
    // 单字符 NFA（如 'c'）
    public static NFA buildCharNFA() {
        return buildSingleCharacterNFA('\''); // 单字符
    }

    // 字符串 NFA（如 "string"）
    public static NFA buildStringNFA(String str) {
        NFA nfa = new NFA();
        int currentState = 0;

        for (char c : str.toCharArray()) {
            nfa.addTransition(currentState, c, currentState + 1);
            currentState++;
        }

        nfa.acceptStates.add(currentState);
        return nfa;
    }

    // 标识符 NFA（如 a, b, my_var 等）
    public static NFA buildIdentifierNFA() {
        NFA nfa = new NFA();
        int start = 0, loop = 1;

        // 首字符转移：字母或下划线
        for (char c = 'a'; c <= 'z'; c++) nfa.addTransition(start, c, loop);
        for (char c = 'A'; c <= 'Z'; c++) nfa.addTransition(start, c, loop);
        nfa.addTransition(start, '_', loop);

        // 后续字符转移：字母、数字或下划线
        for (char c = 'a'; c <= 'z'; c++) nfa.addTransition(loop, c, loop);
        for (char c = 'A'; c <= 'Z'; c++) nfa.addTransition(loop, c, loop);
        for (char c = '0'; c <= '9'; c++) nfa.addTransition(loop, c, loop);
        nfa.addTransition(loop, '_', loop);

        nfa.acceptStates.add(loop);
        return nfa;
    }

    // 整数 NFA（如 123, 456 等）
    public static NFA buildIntegerNFA() {
        NFA nfa = new NFA();
        int start = 0, loop = 1;

        // 数字转移
        for (char c = '0'; c <= '9'; c++) {
            nfa.addTransition(start, c, loop);
            nfa.addTransition(loop, c, loop);
        }

        nfa.acceptStates.add(loop);
        return nfa;
    }

    // 浮点数 NFA（如 3.14, 0.99 等）
    public static NFA buildFloatNFA() {
        NFA nfa = new NFA();
        int start = 0, integerPart = 1, dot = 2, floatPart = 3;

        // 整数部分
        for (char c = '0'; c <= '9'; c++) {
            nfa.addTransition(start, c, integerPart);
            nfa.addTransition(integerPart, c, integerPart);
            nfa.addTransition(dot, c, floatPart);
            nfa.addTransition(floatPart, c, floatPart);
        }

        // 小数点
        nfa.addTransition(integerPart, '.', dot);

        nfa.acceptStates.add(floatPart);
        return nfa;
    }
    // 合并多个 NFA
    public static NFA combineNFAs(List<NFA> nfas) {
        NFA combinedNFA = new NFA();
        int globalState = 1;  // 用于重新分配状态编号

        // 遍历每个NFA，合并它们
        for (NFA nfa : nfas) {
            // 添加从新起始状态到每个NFA的起始状态的空转移
            combinedNFA.addEpsilonTransition(0, globalState + nfa.startState);

            // 复制 NFA 的状态转移
            for (Map.Entry<Integer, Map<Character, Set<Integer>>> entry : nfa.transitions.entrySet()) {
                int fromState = entry.getKey() + globalState;  // 重新分配状态编号
                for (Map.Entry<Character, Set<Integer>> transition : entry.getValue().entrySet()) {
                    for (int toState : transition.getValue()) {
                        combinedNFA.addTransition(fromState, transition.getKey(), toState + globalState);
                    }
                }
            }

            // 复制 NFA 的接受状态
            for (int acceptState : nfa.acceptStates) {
                combinedNFA.acceptStates.add(acceptState + globalState);
            }

            // 更新全局状态编号
            globalState += nfa.transitions.size();
        }

        // 设置合并后的起始状态
        combinedNFA.startState = 0;

        return combinedNFA;
    }
}
