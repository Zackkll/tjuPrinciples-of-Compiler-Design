package code;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 提示用户输入源代码
        System.out.println("请输入源代码，输入 'END' 表示结束：");

        // 使用 Scanner 接收多行输入
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
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.tokenize(sourceCode);

        // 输出分析结果
        System.out.println("词法分析结果：");
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}
