package code;
/*
 * @Description:
 */
public class Token {
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
