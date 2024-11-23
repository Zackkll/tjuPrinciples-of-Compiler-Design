package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<>();
		try {
			File file = new File("src/Tests/lex.tsv");
			if (file.isFile() && file.exists()) {
				InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()));
				BufferedReader bufferedReader = new BufferedReader(reader);
				try {
					String str;
					while ((str = bufferedReader.readLine()) != null) {
						String[] strs = str.split("[,><]");
						switch (strs[1]) {
						case "KW":
							list.add(strs[0].trim());
							break;
						case "OP":
							list.add(strs[0].trim());
							break;
						case "SE":
							list.add(strs[0].trim());
							break;
						case "IDN":
							list.add("Ident");
							break;
						case "INT":
							list.add("INT");
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				bufferedReader.close();
				reader.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (IOException e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		TextParseInput.setLex_result_stack(list); // 设置语法分析的输入
		MainParse.DoParse(); // 语法分析
	}
}
