package code;
import java.util.*;

public class SymbolTable {
    private Map<String, String> table = new HashMap<>();

    public void add(String identifier, String type) {
        table.put(identifier, type);
    }

    public String get(String identifier) {
        return table.get(identifier);
    }
}
