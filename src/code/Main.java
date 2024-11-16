package code;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 提示用户输入代码
        System.out.println("请输入代码（以 ';' 结束）：");
        Scanner scanner = new Scanner(System.in);

        // 读取用户输入的完整代码行
        StringBuilder codeBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().endsWith(";")) { // 判断是否以分号结尾
                codeBuilder.append(line).append("\n");
                break;
            }
            codeBuilder.append(line).append("\n");
        }
        String code = codeBuilder.toString();

        // 创建 Lexer 并对用户输入的代码进行词法分析
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.analyze(code);

        // 输出 Token 类型和值
        System.out.println("词法分析结果：");
        tokens.forEach(token -> System.out.println(token.type + ": " + token.value));
    }
}
