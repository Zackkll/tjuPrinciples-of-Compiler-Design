package code_2;

import java.util.HashMap;
import java.util.Map;

public class Token {
    public static final Map<String, String> KEYWORDS;
    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("int", "1");
        KEYWORDS.put("float", "2");
        KEYWORDS.put("char", "3");
        KEYWORDS.put("void", "4");
        KEYWORDS.put("return", "5");
        KEYWORDS.put("const", "6");
        KEYWORDS.put("main", "7");
        KEYWORDS.put("struct", "29");
        KEYWORDS.put("union", "30");
        KEYWORDS.put("switch", "31");
        KEYWORDS.put("case", "32");
        KEYWORDS.put("default", "33");
    }

    // 运算符表
    public static final Map<String, String> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("!", "8");
        OPERATORS.put("+", "9");
        OPERATORS.put("-", "10");
        OPERATORS.put("*", "11");
        OPERATORS.put("/", "12");
        OPERATORS.put("%", "13");
        OPERATORS.put("=", "14");
        OPERATORS.put(">", "15");
        OPERATORS.put("<", "16");
        OPERATORS.put("==", "17");
        OPERATORS.put("<=", "18");
        OPERATORS.put(">=", "19");
        OPERATORS.put("!=", "20");
        OPERATORS.put("&&", "21");
        OPERATORS.put("||", "22");
    }

    // 界符表
    public static final Map<String, String> SEPARATORS;
    static {
        SEPARATORS = new HashMap<>();
        SEPARATORS.put("(", "23");
        SEPARATORS.put(")", "24");
        SEPARATORS.put("{", "25");
        SEPARATORS.put("}", "26");
        SEPARATORS.put(";", "27");
        SEPARATORS.put(",", "28");
        SEPARATORS.put(":", "32");
    }
    public String type;  // 单词符号种别 (KW, OP, SE, IDN, INT, FLOAT, CHAR, STR)
    public String value; // 单词符号内容 (编号或值)

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // 输出格式：[单词符号] <种别,内容>
    @Override
    public String toString() {
        return "<" + type + "," + value + ">";
    }
}