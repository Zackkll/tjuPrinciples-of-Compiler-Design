package code;

import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (current < tokens.size()) {
            if (!declaration() && !assignment()) {
                throw new RuntimeException("Syntax error at: " + tokens.get(current).value);
            }
        }
    }

    private boolean declaration() {
        if (match("KEYWORD") && (peek().value.equals("int") || peek().value.equals("float"))) {
            consume("IDN"); // 消费变量名
            if (match("OPERATOR", "=")) { // 如果存在赋值
                expression(); // 解析表达式
            }
            consume("SEPARATOR", ";"); // 确保以分号结束
            return true;
        }
        return false;
    }

    private boolean assignment() {
        if (match("IDN")) {
            consume("OPERATOR", "=");
            expression();
            consume("SEPARATOR", ";");
            return true;
        }
        return false;
    }

    private void expression() {
        term();
        while (match("OPERATOR", "+") || match("OPERATOR", "-") ||
                match("OPERATOR", "*") || match("OPERATOR", "/")) { // 支持四则运算
            term();
        }
    }

    private void term() {
        if (match("IDN") || match("INT") || match("FLOAT")) {
            return;
        }
        if (match("CHAR") || match("STRING")) { // 增加对字符和字符串的支持
            return;
        }
        throw new RuntimeException("Unexpected token: " + peek().value);
    }

    private boolean match(String type) {
        if (current < tokens.size() && peek().type.equals(type)) {
            current++;
            return true;
        }
        return false;
    }

    private boolean match(String type, String value) {
        if (current < tokens.size() && peek().type.equals(type) && peek().value.equals(value)) {
            current++;
            return true;
        }
        return false;
    }

    private void consume(String type) {
        if (!match(type)) {
            throw new RuntimeException("Expected " + type + " but found " + peek().value);
        }
    }

    private void consume(String type, String value) {
        if (!match(type, value)) {
            throw new RuntimeException("Expected " + value + " but found " + peek().value);
        }
    }

    private Token peek() {
        return tokens.get(current);
    }
}