package code;

import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final String KEYWORDS = "while|for|continue|break|if|else|float|int|char|void|return";
    private static final String OPERATORS = "\\+\\+|--|\\+=|-=|\\*=|/=|%=|==|<=|>=|!=|&&|\\|\\||[+\\-*/%<>=]";
    private static final String SEPARATORS = "[(){};,\\[\\]]";
    private static final String IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String INT = "\\d+";
    private static final String FLOAT = "\\d+\\.\\d+";
    private static final String CHAR = "'[^']'";
    private static final String STRING = "\"[^\"]*\"";

    private List<Token> tokens = new ArrayList<>();
    private SymbolTable symbolTable = new SymbolTable();

    public List<Token> analyze(String code) {
        String regex = String.format("(%s)|(%s)|(%s)|(%s)|(%s)|(%s)|(%s)|(%s)",
            KEYWORDS, OPERATORS, SEPARATORS, IDENTIFIER, INT, FLOAT, CHAR, STRING);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches(KEYWORDS)) {
                tokens.add(new Token("KEYWORD", token));
            } else if (token.matches(OPERATORS)) {
                tokens.add(new Token("OPERATOR", token));
            } else if (token.matches(SEPARATORS)) {
                tokens.add(new Token("SEPARATOR", token));
            } else if (token.matches(IDENTIFIER)) {
                tokens.add(new Token("IDN", token));
                symbolTable.add(token, "unknown");
            } else if (token.matches(INT)) {
                tokens.add(new Token("INT", token));
            } else if (token.matches(FLOAT)) {
                tokens.add(new Token("FLOAT", token));
            } else if (token.matches(CHAR)) {
                tokens.add(new Token("CHAR", token));
            } else if (token.matches(STRING)) {
                tokens.add(new Token("STRING", token));
            }
        }
        return tokens;
    }
}
