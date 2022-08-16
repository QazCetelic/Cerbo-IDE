package qaz.code.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer {
    public static boolean isBalanced(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '[') {
                count++;
            }
            else if (str.charAt(i) == ']') {
                count--;
            }
            if (count < 0) {
                return false;
            }
        }
        return count == 0;
    }
    
    public static final Set<Character> OPERATORS = Set.of('+', '-', '<', '>', '[', ']', '.', ',');
    private static final Pattern EMPTY_LOOP_PATTERN = Pattern.compile("\\[[^]\\[.,><+-]*]");
    
    /**
     * Finds any loop that doesn't contain any operators.
     * @param code The code that potentially contains empty loops.
     * @return All the starting indexes of the empty loops.
     */
    public static List<Integer> findEmptyLoops(String code) {
        Matcher matcher = EMPTY_LOOP_PATTERN.matcher(code);
        List<Integer> indexes = new ArrayList<>();
        while (matcher.find()) {
            indexes.add(matcher.start());
        }
        return indexes;
    }
}
