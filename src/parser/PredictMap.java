package parser;

import java.util.ArrayList;
import java.util.HashMap;

public class PredictMap {
    static void setPrediction(ArrayList<Formula> formulas, ArrayList<String> terminals,
                              ArrayList<String> nonTerminals, HashMap<String, ArrayList<String>> firsts,
                              HashMap<String, ArrayList<String>> follows,
                              HashMap<String, Formula> predictions) {
        for (Formula formula : formulas) {
            // First(formula.right[0])
            try {
                if (formula.right[0].equals("$")) {
                    continue;
                }
                for (String terminalInFirsts : firsts.get(formula.right[0])) {
                    if (terminalInFirsts.equals("$")) {
                        for (String terminalInFollows : follows.get(formula.left)) {
                            predictions.put(getMapKey(terminalInFollows, formula.left),
                                    new Formula(formula.left, new String[]{"$"}));
                        }
                    }
                    predictions.put(getMapKey(terminalInFirsts, formula.left), formula);
                }
            } catch (Exception e) {
                System.out.println("first结合中没有 key: " + formula.right[0]);
                e.printStackTrace();
            }
        }

        for (Formula formula : formulas) {
            if (formula.returnRights()[0].equals("$")) {    // E -> $
                for (String followElement : follows.get(formula.returnLeft())) { // Follow(E)
                    // [FollowElement(E), E] : E - > $
                    predictions.put(getMapKey(followElement, formula.returnLeft()), formula);
                }
            }
        }
    }

    // 以固定的格式产生分析表的 Key
    static String getMapKey(String terminal, String nonTerminal) {
        return  "{横坐标: " + terminal + "  " + "纵坐标: " + nonTerminal + "}";
    }

}
