package code;

import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final String KEYWORDS = "while|for|continue|break|if|else|float|int|char|void|return|const|main|struct|union|switch|case|default";
    private static final String OPERATORS = "\\+\\+|--|\\+=|-=|\\*=|/=|%=|==|<=|>=|!=|&&|\\|\\||[+\\-*/%<>=]";
    private static final String SEPARATORS = "[(){};,\\[\\]:]";
    private static final String IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String FLOAT = "\\d+\\.\\d+"; // 将FLOAT提前到INT之前
    private static final String INT = "\\d+";
    private static final String CHAR = "'[^']'";


    //定义TOKEN序列，就是将要
    private List<Token> tokens = new ArrayList<>();

    public List<Token> analyze(String code) {
        String regex = String.format("(%s)|(%s)|(%s)|(%s)|(%s)|(%s)|(%s)",
                KEYWORDS, OPERATORS, SEPARATORS, IDENTIFIER, FLOAT, INT, CHAR);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches("(?i)" + KEYWORDS)) {
                tokens.add(new Token("KW", token));
            } else if (token.matches("(?i)" + OPERATORS)) {
                tokens.add(new Token("OP", token));
            } else if (token.matches(SEPARATORS)) {
                tokens.add(new Token("SE", token));
            } else if (token.matches(IDENTIFIER)) {
                tokens.add(new Token("IDN", token));
            } else if (token.matches(FLOAT)) {
                tokens.add(new Token("FLOAT", token)); // 识别浮点数
            } else if (token.matches(INT)) {
                tokens.add(new Token("INT", token)); // 识别整数
            } else if (token.matches(CHAR)) {
                tokens.add(new Token("CHAR", token));
            }
        }
        return tokens;
    }
}
