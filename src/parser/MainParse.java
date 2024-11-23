package parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainParse {
	static HashMap<String, Formula> predictMap; // 预测表
	static ArrayList<String> input_str; // 输入串, 词法分析的结果
	static ArrayList<String> symbol_stack; // 符号栈
	// static ArrayList<String> parse_error_stack; // 语法分析输出可能的错误结果
	static ArrayList<String> parse_result_stack; // 语法分析输出展示的结果
	static int parse_result_counter; // 语法分析输出结果的计数器

	static void DoParse() {
		input_str = TextParseInput.getLex_result_stack();
		symbol_stack = new ArrayList<>();
		parse_result_stack = new ArrayList<>();
		parse_result_counter = 0;

		TextParse.Do();
		predictMap = TextParse.predictions;

		TextParse.writeAllIntoFile();

		parse();

		printParseResult();
	}

	static void parse() {
		symbol_stack.add("#");
		input_str.add("#");
		symbol_stack.add("program");

		String predictMapKey;
		while (true) {
			parse_result_counter++;
			if (symbol_stack.get(symbol_stack.size() - 1).equals("#") && input_str.get(0).equals("#")) {
				parse_result_stack.add(parse_result_counter + "\t" + "EOF" + "#" + "EOF" + "\t" + "accept");
				break;
			}
			try {
				if (input_str.get(0).equals(symbol_stack.get(symbol_stack.size() - 1))) {
					parse_result_stack.add(parse_result_counter + "\t" + symbol_stack.get(symbol_stack.size() - 1) + "#"
							+ input_str.get(0) + "\t" + "move");
					input_str.remove(0);
					symbol_stack.remove(symbol_stack.size() - 1);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 匹配字符
			predictMapKey = PredictMap.getMapKey(input_str.get(0), symbol_stack.get(symbol_stack.size() - 1));

			// 能够找到匹配的
			Formula formula = predictMap.get(predictMapKey);
			if (formula != null) {
				parse_result_stack.add(parse_result_counter + "\t" + symbol_stack.get(symbol_stack.size() - 1) + "#"
						+ input_str.get(0) + "\t" + "reduction");
				if (symbol_stack.get(symbol_stack.size() - 1).equals("#")) {
				} else {
					symbol_stack.remove(symbol_stack.size() - 1);
				}
				String[] rights = formula.returnRights();
				if (rights[0].equals("$")) {
					continue;
				}
				for (int i = rights.length - 1; i >= 0; i--) {
					symbol_stack.add(rights[i]);
				}
			}

			else {
				parse_result_stack.add(parse_result_counter + "\t" + symbol_stack.get(symbol_stack.size() - 1) + "#"
						+ input_str.get(0) + "\t" + "error");
				return;
			}
		}
	}

	// 输出语法分析结果
	static void printParseResult() {
		System.out.println("开始输出语法分析结果: --------------------");
		for (String s : parse_result_stack) {
			System.out.println(s);
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("src/Tests/grammar.tsv"));
			for (String s : parse_result_stack) {
				out.write(s + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
