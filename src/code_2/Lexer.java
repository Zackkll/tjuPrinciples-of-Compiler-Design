package code_2;
import java.util.*;
import java.util.regex.*;

public class Lexer {
    // 关键字表
    private static final Map<String, String> KEYWORDS;
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
    private static final Map<String, String> OPERATORS;
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
    private static final Map<String, String> SEPARATORS;
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

    // 正则表达式定义
    private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern INTEGER = Pattern.compile("\\d+");
    private static final Pattern FLOAT = Pattern.compile("\\d+\\.\\d+");
    private static final Pattern CHAR = Pattern.compile("'(.)'");
    private static final Pattern STRING = Pattern.compile("\"(.*?)\"");

    public List<String> tokenize(String sourceCode) {
        List<String> output = new ArrayList<>();

        // 1. 删除单行注释（以 // 开头）
        sourceCode = sourceCode.replaceAll("//.*", "");

        // 2. 删除多行注释（以 /* 开始，以 */ 结束）
        sourceCode = sourceCode.replaceAll("/\\*.*?\\*/", "");

        // 预处理：分隔标点符号，并确保数字和冒号之间有空格
        sourceCode = sourceCode.replaceAll("([{}(),;=<>!+*/%-])", " $1 ");
        sourceCode = sourceCode.replaceAll("(\\d+):", "$1 : ");  // 增加对数字和冒号的处理
        sourceCode = sourceCode.replaceAll("(default:)", "default : ");  // 特别处理 default:

        String[] lines = sourceCode.split("\\n");
        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                if (word.isBlank()) continue; // 跳过空字符串

                // 识别关键字
                if (KEYWORDS.containsKey(word)) {
                    output.add(word + "\t" + new Token("KW", KEYWORDS.get(word)));
                }
                // 识别运算符
                else if (OPERATORS.containsKey(word)) {
                    output.add(word + "\t" + new Token("OP", OPERATORS.get(word)));
                }
                // 识别分隔符
                else if (SEPARATORS.containsKey(word)) {
                    output.add(word + "\t" + new Token("SE", SEPARATORS.get(word)));
                }
                // 识别标识符
                else if (IDENTIFIER.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("IDN", word));
                }
                // 识别整数
                else if (INTEGER.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("INT", word));
                }
                // 识别浮点数
                else if (FLOAT.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("FLOAT", word));
                }
                // 识别字符常量
                else if (CHAR.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("CHAR", word));
                }
                // 识别字符串常量
                else if (STRING.matcher(word).matches()) {
                    output.add(word + "\t" + new Token("STR", word));
                }
                // 未识别的词法单元
                else {
                    throw new RuntimeException("Unrecognized token: " + word);
                }
            }
        }
        return output;
    }
}
