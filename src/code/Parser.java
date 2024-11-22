package code;

import java.util.List;

public abstract class Parser {
    protected Lexer lexer;
    protected List<String> tokens;
    protected int tokenIndex;

    public Parser(Lexer lexer, List<String> tokens) {
        this.lexer = lexer;
        this.tokens = tokens;
        this.tokenIndex = 0;
    }

    public abstract boolean parse();

    protected boolean match(String token) {
        if (tokenIndex < tokens.size() && tokens.get(tokenIndex).equals(token)) {
            tokenIndex++;
            return true;
        }
        return false;
    }
}