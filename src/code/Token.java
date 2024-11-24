package code;

import java.util.HashMap;
import java.util.Map;

/*
 * @Description:
 */
public class Token {
    public static final Map<String, Integer> KW = new HashMap<String, Integer>() {
        {
            put("int", 1);
            put("float", 2);
            put("char", 3);
            put("void", 4);
            put("return", 5);
            put("const", 6);
            put("main", 7);
            put("struct", 29);
            put("union", 30);
            put("switch", 31);
            put("case", 32);
            put("default", 33);
            put("break", 34);
        }
    };
    public static final Map<String, Integer> OP = new HashMap<String, Integer>() {
        {
            put("!",8);
            put("+", 9);
            put("-", 10);
            put("*", 11);
            put("/", 12);
            put("%", 13);
            put("=", 14);
            put(">", 15);
            put("<", 16);
            put("==", 17);
            put("<=", 18);
            put(">=", 19);
            put("!=", 20);
            put("&&", 21);
            put("||", 22);
        }
    };
    public static final Map<String, Integer> SE = new HashMap<String, Integer>() {{
        put("(", 23);
        put(")", 24);
        put("{", 25);
        put("}", 26);
        put(";", 27);
        put(",", 28);
    }};
    String lexeme;
    String tokenType;
    String tokenNum;

    public String getLexeme() {
        return lexeme;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getTokenNum() {
        return tokenNum;
    }


    public Token(String lexeme, String tokenType, String tokenNum) {
        this.lexeme = lexeme;
        this.tokenType = tokenType;
        this.tokenNum = tokenNum;
    }
}
