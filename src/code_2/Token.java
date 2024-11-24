package code_2;
public class Token {
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