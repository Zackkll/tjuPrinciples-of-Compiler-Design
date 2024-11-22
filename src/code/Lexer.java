package code;
import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final Map<String, String> KEYWORDS = Map.of(
            "int", "1", "float", "2", "char", "3", "void", "4",
            "return", "5", "const", "6", "main", "7"
    );
    private static final Map<String, String> OPERATORS = Map.of(
            "+", "9", "-", "10", "*", "11", "/", "12","=","14",
            ">", "15", "<", "16", "==", "17", "!=", "20"
    );
    private static final Map<String, String> SEPARATORS = Map.of(
            "(", "23", ")", "24", "{", "25", "}", "26", ";", "27", ",", "28"
    );

    private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern INTEGER = Pattern.compile("\\d+");
    private static final Pattern FLOAT = Pattern.compile("\\d+\\.\\d+");
    private static final Pattern CHAR = Pattern.compile("'(.)'");
    private static final Pattern STRING = Pattern.compile("\"(.*?)\"");

    public List<Token> tokenize(String sourceCode) {
        List<Token> tokens = new ArrayList<>();

        // 预处理：分离标点符号
        sourceCode = sourceCode.replaceAll("([{}(),;=<>!+*/%-])", " $1 ");

        String[] lines = sourceCode.split("\\n");
        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                if (word.isBlank()) continue; // 跳过空字符串
                if (KEYWORDS.containsKey(word)) {
                    tokens.add(new Token("KW", KEYWORDS.get(word)));
                } else if (OPERATORS.containsKey(word)) {
                    tokens.add(new Token("OP", OPERATORS.get(word)));
                } else if (SEPARATORS.containsKey(word)) {
                    tokens.add(new Token("SE", SEPARATORS.get(word)));
                } else if (IDENTIFIER.matcher(word).matches()) {
                    tokens.add(new Token("IDN", word));
                } else if (INTEGER.matcher(word).matches()) {
                    tokens.add(new Token("INT", word));
                } else if (FLOAT.matcher(word).matches()) {
                    tokens.add(new Token("FLOAT", word));
                } else if (CHAR.matcher(word).matches()) {
                    tokens.add(new Token("CHAR", word));
                } else if (STRING.matcher(word).matches()) {
                    tokens.add(new Token("STR", word));
                } else {
                    throw new RuntimeException("Unrecognized token: " + word);
                }
            }
        }
        return tokens;
    }

}
