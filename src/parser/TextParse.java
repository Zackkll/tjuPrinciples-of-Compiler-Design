package parser;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TextParse {
	static String firstTablePath = "src/Tests/firstTable.tsv";
    static String followTablePath = "src/Tests/followTable.tsv";
    static String predictMapPath = "src/Tests/predictMap.tsv";
	
    static ArrayList<Formula> formulas;	// 产生式
    static ArrayList<String> terminals;	// 终结符
    static ArrayList<String> nonTerminals;	// 非终结符
    static HashMap<String, ArrayList<String>> firsts;
    static HashMap<String, ArrayList<String>> follows;
    static HashMap<String, Formula> predictions;

    static void writeAllIntoFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(firstTablePath));
            for (String s : firsts.keySet()) {
                out.write(s + "\t" + firsts.get(s) + "\n");
            }
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(followTablePath));
            for (String s : follows.keySet()) {
                out.write(s + "\t" + follows.get(s) + "\n");
            }
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(predictMapPath));
            for (String s : predictions.keySet()) {
                out.write(s + "\t" + "文法: " + predictions.get(s).left + "->" +
                        Arrays.toString(predictions.get(s).right) + "\n");
            }
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 入口
    static void Do() {
        formulas = new ArrayList<>();
        terminals = new ArrayList<>();
        nonTerminals = new ArrayList<>();
        firsts = new HashMap<>();
        follows = new HashMap<>();
        predictions = new HashMap<>();
        setFormulas();
        setNonTerminals();
        setTerminals();
        setFirsts();
        setFollows();
        setPrediction();
    }

    // 生成文法规则
    public static void setFormulas() {
        try {
            File file = new File("src/parser/grammar.txt");
            RandomAccessFile randomfile = new RandomAccessFile(file, "r");
            String line;
            String left;
            String right;
            Formula formula;
            while ((line=randomfile.readLine())!=null) {
                left = line.split("->")[0].trim();
                right = line.split("->")[1].trim();	// 将右侧所有的值都算进去
                formula = new Formula(left, right.split(" ")); // 根据空格分离右侧的值
                formulas.add(formula);
            }
            randomfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setNonTerminals() {
        for (Formula formula : formulas) {
            if (nonTerminals.contains(formula.left)) {
                continue;
            } else {
                nonTerminals.add(formula.left);
            }
        }
    }

    static void setTerminals() {
        for (Formula formula : formulas) {
            String[] rights = formula.returnRights();
            for (String s : rights) {
                if (nonTerminals.contains(s) || s.equals("$")) {
                    continue;
                } else { 
                    terminals.add(s);
                }
            }
        }
    }

    // 生成 First 集合
    static void setFirsts() {
        FirstTable.setFirst(formulas,terminals,nonTerminals,firsts);
    }

    // 生成 Follow 集合
    static void setFollows() {
        FollowTable.setFollow(formulas,terminals,nonTerminals,firsts,follows);
    }

    static void setPrediction() {
        PredictMap.setPrediction(formulas,terminals,nonTerminals,firsts,follows,predictions);
    }
}

// 产生式类(在这里解析文法)
class Formula {
    String left;
    String[] right;
    public Formula(String left, String[] right){
        this.left = left;
        this.right = right;
    }

    public String[] returnRights(){
        return right;
    }

    public String returnLeft(){
        return left;
    }
}
