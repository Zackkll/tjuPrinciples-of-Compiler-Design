package code;
/*
 * @Description: 词法分析器的程序入口
 */
public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //默认是当前工程文件夹
        String dir = "src/Tests/";
        String in = "test2.c";
        String out = "5lex.tsv";

        ReadTxt r = new ReadTxt();
        NFA nfa = new NFA();
        DFA dfa = new DFA(nfa);
        dfa.determine();
        dfa.minimize();

        TokenTable tokenTable = new TokenTable();
        Lexer lexer = new Lexer(r.readTxt(dir + in), tokenTable, dfa);
        lexer.run();
        tokenTable.printTokenTable();
        tokenTable.saveTokenTable(dir + out);
    }
}