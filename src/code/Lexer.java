package code;

import java.util.*;

public class Lexer {
    // 状态定义
    private enum State {
        START, IDENTIFIER, INTEGER, FLOAT, CHAR, STRING, OPERATOR, SEPARATOR, ERROR
    }

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

    public List<String> tokenize(String sourceCode) {
        List<String> output = new ArrayList<>();
        State state = State.START;
        StringBuilder tokenBuffer = new StringBuilder();
        char[] chars = sourceCode.toCharArray();

        for (int i = 0; i <= chars.length; i++) {
            char c = i < chars.length ? chars[i] : '\0'; // 用 \0 作为结束标记

            switch (state) {
                case START:
                    if (Character.isWhitespace(c)) {
                        continue;
                    } else if (Character.isLetter(c) || c == '_') {
                        tokenBuffer.append(c);
                        state = State.IDENTIFIER;
                    } else if (Character.isDigit(c)) {
                        tokenBuffer.append(c);
                        state = State.INTEGER;
                    } else if (c == '\'') {
                        tokenBuffer.append(c);
                        state = State.CHAR;
                    } else if (c == '\"') {
                        tokenBuffer.append(c);
                        state = State.STRING;
                    } else if (OPERATORS.containsKey(String.valueOf(c))) {
                        tokenBuffer.append(c);
                        state = State.OPERATOR;
                    } else if (SEPARATORS.containsKey(String.valueOf(c))) {
                        output.add(c + "\t" + new Token("SE", SEPARATORS.get(String.valueOf(c))));
                    } else if (c == '\0') {
                        break;
                    } else {
                        state = State.ERROR;
                        tokenBuffer.append(c);
                    }
                    break;

                case IDENTIFIER:
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        tokenBuffer.append(c);
                    } else {
                        String token = tokenBuffer.toString();
                        if (KEYWORDS.containsKey(token)) {
                            output.add(token + "\t" + new Token("KW", KEYWORDS.get(token)));
                        } else {
                            output.add(token + "\t" + new Token("IDN", token));
                        }
                        tokenBuffer.setLength(0);
                        state = State.START;
                        i--; // 回退字符
                    }
                    break;

                case INTEGER:
                    if (Character.isDigit(c)) {
                        tokenBuffer.append(c);
                    } else if (c == '.') {
                        tokenBuffer.append(c);
                        state = State.FLOAT;
                    } else {
                        output.add(tokenBuffer.toString() + "\t" + new Token("INT", tokenBuffer.toString()));
                        tokenBuffer.setLength(0);
                        state = State.START;
                        i--; // 回退字符
                    }
                    break;

                case FLOAT:
                    if (Character.isDigit(c)) {
                        tokenBuffer.append(c);
                    } else {
                        output.add(tokenBuffer.toString() + "\t" + new Token("FLOAT", tokenBuffer.toString()));
                        tokenBuffer.setLength(0);
                        state = State.START;
                        i--; // 回退字符
                    }
                    break;

                case CHAR:
                    if (c == '\'') {
                        tokenBuffer.append(c);
                        output.add(tokenBuffer.toString() + "\t" + new Token("CHAR", tokenBuffer.toString()));
                        tokenBuffer.setLength(0);
                        state = State.START;
                    } else {
                        tokenBuffer.append(c);
                    }
                    break;

                case STRING:
                    tokenBuffer.append(c);
                    if (c == '\"') {
                        output.add(tokenBuffer.toString() + "\t" + new Token("STR", tokenBuffer.toString()));
                        tokenBuffer.setLength(0);
                        state = State.START;
                    }
                    break;

                case OPERATOR:
                    String currentToken = tokenBuffer.toString();
                    if (OPERATORS.containsKey(currentToken + c)) {
                        tokenBuffer.append(c);
                    } else {
                        output.add(tokenBuffer.toString() + "\t" + new Token("OP", OPERATORS.get(currentToken)));
                        tokenBuffer.setLength(0);
                        state = State.START;
                        i--; // 回退字符
                    }
                    break;

                case ERROR:
                    throw new RuntimeException("Unrecognized token: " + tokenBuffer.toString());

                default:
                    throw new IllegalStateException("Unexpected state: " + state);
            }
        }

        return output;
    }
}