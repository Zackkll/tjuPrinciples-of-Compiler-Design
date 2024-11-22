package code;
public class Token {
    public String type; // 类型 (KW, OP, SE, IDN, INT, FLOAT, CHAR, STR)
    public String value; // 值或编号

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + type + "," + value + ">";
    }
}
