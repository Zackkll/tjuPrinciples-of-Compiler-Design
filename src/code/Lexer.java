package code;
import java.util.*;
import java.util.regex.*;

public class Lexer {
    // 关键字表
    private static final Map<String, String> KEYWORDS = Map.of(
            "int", "1", "float", "2", "char", "3", "void", "4",
            "return", "5", "const", "6", "main", "7"
    );
    // 运算符表
    private static final Map<String, String> OPERATORS = Map.of(
            "+", "9", "-", "10", "*", "11", "/", "12",
            ">", "15", "<", "16", "==", "17", "!=", "20",
            "=", "11"
    );
    // 界符表
    private static final Map<String, String> SEPARATORS = Map.of(
            "(", "23", ")", "24", "{", "25", "}", "26", ";", "27", ",", "28"
    );

    // 正则表达式定义
    private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern INTEGER = Pattern.compile("\\d+");
    private static final Pattern FLOAT = Pattern.compile("\\d+\\.\\d+");
    private static final Pattern CHAR = Pattern.compile("'(.)'");
    private static final Pattern STRING = Pattern.compile("\"(.*?)\"");

    public List<String> tokenize(String sourceCode) {
        List<String> output = new ArrayList<>();

        // 预处理：分隔标点符号
        sourceCode = sourceCode.replaceAll("([{}(),;=<>!+*/%-])", " $1 ");

        String[] lines = sourceCode.split("\\n");
        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                if (word.isBlank()) continue; // 跳过空字符串
                if (KEYWORDS.containsKey(word)) {
                    output.add(word + "\t" + new Token("KW", KEYWORDS.get(word)));
                } else if (OPERATORS.containsKey(word)) {
                    output.add(word + "\t" + new Token("OP", OPERATORS.get(word)));
                } else if (SEPARATORS.containsKey(word)) {
                    output.add(word + "\t" + new Token("SE", SEPARATORS.get(word)));
                } else if (IDENTIFIER.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("IDN", word));
                } else if (INTEGER.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("INT", word));
                } else if (FLOAT.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("FLOAT", word));
                } else if (CHAR.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("CHAR", word));
                } else if (STRING.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("STR", word));
                } else {
                    throw new RuntimeException("Unrecognized token: " + word);
                }
            }
        }
        return output;
    }
}
