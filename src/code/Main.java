package code;

import java.util.*;

public class Main {
    private static Lexer lexer;
    private static List<String> tokens;
    

    public static void main(String[] args) {
        System.out.println("请输入源代码，输入 'END' 表示结束：");

        Scanner scanner = new Scanner(System.in);
        StringBuilder sourceCodeBuilder = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            sourceCodeBuilder.append(line).append("\n");
        }

        String sourceCode = sourceCodeBuilder.toString();

        // 调用 Lexer 对输入的代码进行词法分析
        lexer = new Lexer();
        tokens = lexer.tokenize(sourceCode);

        // 输出分析结果
        System.out.println("词法分析结果：");
        for (String token : tokens) {
            System.out.println(token);
        }

        // 开始语法分析
        ProgramParser parser = new ProgramParser(lexer, tokens);
        boolean result = parser.parse();
        System.out.println(result);}

    
    
}