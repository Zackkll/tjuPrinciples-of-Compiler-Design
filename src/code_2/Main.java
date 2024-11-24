package code_2;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 指定要读取的文件路径
        String filePath = "src/Tests/test1.c";

        // 读取文件内容
        StringBuilder sourceCodeBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCodeBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("无法读取文件: " + e.getMessage());
            return;
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
