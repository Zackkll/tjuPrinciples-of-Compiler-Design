package code;
import java.util.*;
public class Mainpaser {
	public static void main(String[] args) {
        String code = "int x = 5; float y = 1;";
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.analyze(code);

        Parser parser = new Parser(tokens);
        parser.parse();

        System.out.println("Parsing completed successfully.");
    }
}
