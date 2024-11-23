package parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstTable {
	static ArrayList<Formula> formulas;
	static ArrayList<String> terminals;
	static ArrayList<String> nonTerminals;
	static HashMap<String, ArrayList<String>> firsts;

	static void setFirst(ArrayList<Formula> _formulas, ArrayList<String> _terminals, ArrayList<String> _nonTerminals,
			HashMap<String, ArrayList<String>> _firsts) {
		formulas = _formulas;
		terminals = _terminals;
		nonTerminals = _nonTerminals;
		firsts = _firsts;

		// 终结符全部求出first集
		ArrayList<String> first;
		for (String terminal : terminals) {
			first = new ArrayList<String>();
			first.add(terminal);
			firsts.put(terminal, first);
		}
		// 给所有非终结符注册一下
		for (String nonterminal : nonTerminals) {
			first = new ArrayList<String>();
			firsts.put(nonterminal, first);
		}

		boolean flag;
		while (true) {
			flag = true;
			String left;
			String right;
			String[] rights;
			for (Formula formula : formulas) {
				left = formula.returnLeft();
				rights = formula.returnRights();
				for (String s : rights) {
					right = s;
					if (!right.equals("$")) {
						for (int l = 0; l < firsts.get(right).size(); l++) {
							if (firsts.get(left).contains(firsts.get(right).get(l))) {
								continue;
							} else {
								firsts.get(left).add(firsts.get(right).get(l));
								flag = false;
							}
						}
					}
					if (isCanBeNull(formulas, right)) {
						continue;
					} else {
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
	}

	// 判断是否产生$
	static boolean isCanBeNull(ArrayList<Formula> formulas, String symbol) {
		String[] rights;
		for (Formula formula : formulas) {
			if (formula.returnLeft().equals(symbol)) {
				rights = formula.returnRights();
				if (rights[0].equals("$")) {
					return true;
				}
			}
		}
		return false;
	}
}
