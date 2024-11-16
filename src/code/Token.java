package code;
public class Token {
    String type;  // Token 类型，例如 "KEYWORD", "IDN", "INT"
    String value; // Token 值，例如 "if", "x", "123"
    
    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
}

