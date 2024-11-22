package code;


import java.util.*;

public class Lexer {
    private static final String KEYWORDS = "while|for|continue|break|if|else|float|int|char|void|return|const|main|struct|union|switch|case|default";
    private static final String OPERATORS = "\\+\\+|--|\\+=|-=|\\*=|/=|%=|==|<=|>=|!=|&&|\\|\\||[+\\-*/%<>=]";
    private static final String SEPARATORS = "[(){};,\\[\\]:]";
    private static final String IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String FLOAT = "\\d+\\.\\d+";
    private static final String INT = "\\d+";
    private static final String CHAR = "'[^']'";

    // 定义所有可能的正则表达式
    private static final Map<String, String> REGEX_MAP = Map.of(
            "KW", KEYWORDS,
            "OP", OPERATORS,
            "SE", SEPARATORS,
            "IDN", IDENTIFIER,
            "FLOAT", FLOAT,
            "INT", INT,
            "CHAR", CHAR
    );

    // DFA 状态机
    private DFAState startDFAState;

    // 初始化 Lexer
    public Lexer() {
        // 使用 NFA 生成器创建初始 NFA
        NFAGenerator nfaGenerator = new NFAGenerator();
        NFAState combinedStartState = new NFAState(0);
        Set<NFAState> finalStates = new HashSet<>();
        Set<Character> alphabet = new HashSet<>();

        // 构建所有的 NFA 并合并
        for (Map.Entry<String, String> entry : REGEX_MAP.entrySet()) {
            NFA nfa = nfaGenerator.generateFromRegex(entry.getValue());
            combinedStartState.addTransition('\0', nfa.startState);
            finalStates.addAll(nfa.finalStates);

            // 将正则表达式中的字符加入 alphabet
            for (char ch : entry.getValue().toCharArray()) {
                if (ch != '\\' && ch != '|' && ch != '(' && ch != ')' && ch != '[' && ch != ']' && ch != '*') {
                    alphabet.add(ch);
                }
            }
        }

        NFA combinedNFA = new NFA(combinedStartState, finalStates);

        // 确定化 NFA 为 DFA
        NFAtoDFAConverter converter = new NFAtoDFAConverter();
        DFAState dfaStartState = converter.convertToDFA(combinedNFA.startState, alphabet);

        // 最小化 DFA
        DFAMinimizer minimizer = new DFAMinimizer();
        this.startDFAState = minimizer.minimizeDFA(dfaStartState, alphabet);
    }

    // 使用 DFA 进行词法分析
    public List<Token> analyze(String code) {
        List<Token> tokens = new ArrayList<>();
        DFAState currentState = startDFAState;
        StringBuilder buffer = new StringBuilder();

        for (char ch : code.toCharArray()) {
            DFAState nextState = currentState.transitions.get(ch);
            if (nextState != null) {
                buffer.append(ch);
                currentState = nextState;
            } else {
                // 识别到完整的 token
                if (currentState.isFinal) {
                    tokens.add(new Token("UNKNOWN", buffer.toString()));
                    buffer.setLength(0);
                }
                currentState = startDFAState; // 重置到初始状态
            }
        }

        return tokens;
    }
}
