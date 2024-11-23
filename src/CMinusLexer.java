import java.util.*;

public class CMinusLexer {

    private DFA dfa;
    private Map<Integer, String> tokenTypes;

    public CMinusLexer(DFA dfa, Map<Integer, String> tokenTypes) {
        this.dfa = dfa;
        this.tokenTypes = tokenTypes;
    }

    public List<String> tokenize(String sourceCode) {
        List<String> tokens = new ArrayList<>();
        int currentState = dfa.startState;
        StringBuilder buffer = new StringBuilder();

        for (char c : sourceCode.toCharArray()) {
            // 检查 currentState 是否在 dfa.transitions 中有转移
            if (dfa.transitions.containsKey(currentState) && dfa.transitions.get(currentState).containsKey(c)) {
                currentState = dfa.transitions.get(currentState).get(c);
                buffer.append(c);
            } else {
                // 当前状态是接受状态，则保存Token
                if (dfa.acceptStates.contains(currentState)) {
                    // 确保 tokenTypes 中有对应的类型
                    String tokenType = tokenTypes.get(currentState);
                    if (tokenType != null) {
                        tokens.add(buffer.toString() + "\t" + tokenType);
                    } else {
                        // 如果没有找到对应的类型，记录一个错误或跳过
                        System.err.println("Unknown token type for state " + currentState);
                    }
                }
                buffer.setLength(0);
                currentState = dfa.startState;  // 重置到起始状态
            }
        }

        // 最后一个Token
        if (dfa.acceptStates.contains(currentState)) {
            String tokenType = tokenTypes.get(currentState);
            if (tokenType != null) {
                tokens.add(buffer.toString() + "\t" + tokenType);
            }
        }

        return tokens;
    }


    public static void main(String[] args) {
        NFA keywordNFA = NFAConstructor.buildKeywordNFA("int");
        NFA identifierNFA = NFAConstructor.buildIdentifierNFA();
        NFA integerNFA = NFAConstructor.buildIntegerNFA();
        NFA floatNFA = NFAConstructor.buildFloatNFA();
        NFA operatorNFA = NFAConstructor.buildOperatorNFA("+");

        List<NFA> nfas = List.of(keywordNFA, identifierNFA, integerNFA, floatNFA, operatorNFA);
        DFA dfa = NFAtoDFAConverter.convertNfaToDfa(NFAConstructor.combineNFAs(nfas));

        Map<Integer, String> tokenTypes = Map.of(
                //0, "START",
                1, "KW,1",   // int
                2, "IDN",     // identifier
                3, "INT",     // integer
                4, "FLOAT",   // float
                5, "OP,9",    // operator
                6, "SE,27"    // separator
        );

        CMinusLexer lexer = new CMinusLexer(dfa, tokenTypes);
        String code = """
                int a = 10;
                float b = 3.14;
                char c = 'x';
                string str = "hello";
                a = b + 2;
                """;

        List<String> tokens = lexer.tokenize(code);
        tokens.forEach(System.out::println);
    }
}
