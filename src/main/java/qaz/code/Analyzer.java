package qaz.code;

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
}
