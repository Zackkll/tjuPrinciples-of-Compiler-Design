package code;

import java.util.List;
import java.util.Stack;

public class ProgramParser extends Parser {
    private Stack<String> stack = new Stack<>();
    private int tokenIndex = 0;
    private List<String> tokens;
    private static int stepNumber = 1;

    public ProgramParser(Lexer lexer, List<String> tokens) {
        super(lexer, tokens);
        this.tokens = tokens;
    }

    @Override
    public boolean parse() {
        stack.push("$end");
        stack.push("program");
        boolean result = parseCompUnit() && match("$end");
        logStep(stepNumber++, stack, "$end", result ? "accept" : "error");
        return result;
    }

    private boolean parseCompUnit() {
        boolean result = parseDecl() || parseFuncDef();
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseDecl() {
        if (match("int") || match("float") || match("char")) {
            return parseVarDecl();
        }
        if (match("const")) {
            return parseConstDecl();
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), "error");
        return false;
    }

    private boolean parseVarDecl() {
        boolean result = match("Ident");
        while (result && match(",")) {
            result = match("Ident");
        }
        if (result) {
            result = match(";");
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseConstDecl() {
        boolean result = match("int") || match("float") || match("char");
        while (result) {
            result = match("Ident") && match("=");
            result = result && parseConstInitVal();
            while (result && match(",")) {
                result = match("Ident") && match("=");
                result = result && parseConstInitVal();
            }
        }
        if (result) {
            result = match(";");
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseConstInitVal() {
        if (match("INT") || match("FLOAT") || match("CHAR") || match("STR")) {
            return true;
        } else if (match("(")) {
            return parseExp() && match(")");
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), "error");
        return false;
    }

    private boolean parseFuncDef() {
        if (match("int") || match("float") || match("char") || match("void")) {
            if (match("Ident")) {
                if (match("(")) {
                    return parseFuncFParams() && match(")") && parseBlock();
                }
            }
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), "error");
        return false;
    }

    private boolean parseFuncFParams() {
        boolean result = parseFuncFParam();
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseFuncFParam() {
        return match("int") || match("float") || match("char");
    }

    private boolean parseBlock() {
        if (match("{")) {
            while (!match("}")) {
                if (!parseBlockItem()) {
                    return false;
                }
            }
            return true;
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), "error");
        return false;
    }

    private boolean parseBlockItem() {
        return parseDecl() || parseStmt();
    }

    private boolean parseStmt() {
        return match(";") || parseExp() && match(";");
    }

    private boolean parseExp() {
        return parseAssignExp();
    }

    private boolean parseAssignExp() {
        boolean result = parseRelExp();
        if (match("=")) {
            result = result && parseAssignExp();
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseRelExp() {
        boolean result = parseAddExp();
        while (true) {
            if (match("<") || match(">") || match("<=") || match(">=")) {
                result = result && parseAddExp();
            } else {
                break;
            }
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseAddExp() {
        boolean result = parseMulExp();
        while (true) {
            if (match("+") || match("-")) {
                result = result && parseMulExp();
            } else {
                break;
            }
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseMulExp() {
        boolean result = parseUnaryExp();
        while (true) {
            if (match("*") || match("/") || match("%")) {
                result = result && parseUnaryExp();
            } else {
                break;
            }
        }
        logStep(stepNumber++, stack, tokens.get(tokenIndex), result ? "reduction" : "error");
        return result;
    }

    private boolean parseUnaryExp() {
        if (match("+") || match("-") || match("!")) {
            return parseUnaryExp();
        }
        return parsePrimaryExp();
    }

    private boolean parsePrimaryExp() {
        return match("Ident") || match("INT") || match("FLOAT");
    }

    public boolean match(String tokenType) {
        if (tokenIndex < tokens.size() && tokens.get(tokenIndex).equals(tokenType)) {
            tokenIndex++; // 消耗掉当前的 token 并移动到下一个 token
            return true;
        }
        return false;
    }



    private void logStep(int step, Stack<String> stack, String inputSymbol, String action) {
        String stackTop = stack.isEmpty() ? "" : stack.peek();
        System.out.println(String.format("%d\t%s#%s\t%s", step, stackTop, inputSymbol, action));
    }
}