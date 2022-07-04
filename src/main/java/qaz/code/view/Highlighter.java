package qaz.code.view;

import javafx.application.Platform;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import qaz.code.model.Analyzer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Highlighter {
    public static final Pattern PATTERN = Pattern.compile("(?<FIELD>[-+])|(?<POINTER>[><])|(?<IO>[,.])|(?<LOOP>[]\\[])|(?<COMMENT>.+?)");
    
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            if (matcher.group("LOOP") != null) {
                // TODO add bracket color depending on the bracket nesting level
                spansBuilder.add(List.of("highlight-loop"), matcher.end() - matcher.start());
            }
            else {
                String styleClass =
                        matcher.group("FIELD") != null ? "highlight-field-arithmatic" :
                                matcher.group("POINTER") != null ? "highlight-pointer-arithmatic" :
                                        matcher.group("IO") != null ? "highlight-io" :
                                                matcher.group("COMMENT") != null ? "highlight-comment" :
                                                        null; /* never happens */
                Objects.requireNonNull(styleClass);
                List<String> styleList = matcher.group("COMMENT") == null ? List.of("highlight-code", styleClass) : List.of(styleClass);
                spansBuilder.add(styleList, matcher.end() - matcher.start());
            }
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
    private static void process(CodeArea codeArea) {
        String text = codeArea.getText();
        boolean bracketsBalanced = Analyzer.isBalanced(text);
        Map<Integer, Boolean> brackets = new HashMap<>();
        Map<Integer, StyleClass> styleClasses = new HashMap<>();
    
        // Scan through all chars
        {
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (c == '[') {
                    brackets.put(i, BRACKET_OPEN);
                }
                else if (c == ']') {
                    brackets.put(i, BRACKET_CLOSE);
                }
                else if (c == '>' || c == '<') {
                    styleClasses.put(i, StyleClass.FIELD_ARITHMETIC);
                }
                else if (c == '+' || c == '-') {
                    styleClasses.put(i, StyleClass.POINTER_ARITHMETIC);
                }
                else {
                    styleClasses.put(i, StyleClass.COMMENT);
                }
            }
        }
    
        // Stays empty if brackets are not balanced
        HashMap<Integer, Integer> bracketNesting = new HashMap<>();
        if (bracketsBalanced) {
            // Calculate the nesting of brackets
            {
                int nesting = 0;
                for (Map.Entry<Integer, Boolean> entry : brackets.entrySet()) {
                    if (entry.getValue()) {
                        nesting++;
                        bracketNesting.put(entry.getKey(), nesting);
                    }
                    else {
                        bracketNesting.put(entry.getKey(), nesting);
                        nesting--;
                    }
                }
            }
        }
        
        // Apply styling
        Platform.runLater(() -> {
            for (int i = 0; i < styleClasses.size(); i++) {
                StyleClass styleClass = styleClasses.get(i);
                if (styleClass != null) {
                    // TODO use ranges
                    codeArea.setStyleClass(i, i, styleClass.cssClass);
                }
            }
            
            if (bracketsBalanced) {
                for (Map.Entry<Integer, Integer> nestingEntry: bracketNesting.entrySet()) {
                    int index = nestingEntry.getKey();
                    int nesting = nestingEntry.getValue();
                    codeArea.setStyle(index, index, List.of("-fx-fill: hsb(" + (nesting * 10) % 255 + ", 50%, 50%);"));
                }
            }
            else {
                // Shows faulty brackets
                for (Integer index: brackets.keySet()) {
                    codeArea.setStyleClass(index, index, StyleClass.BRACKET_UNBALANCED.cssClass);
                }
            }
        });
    }
    
    private static final boolean BRACKET_OPEN = true;
    private static final boolean BRACKET_CLOSE = false;
    
    enum StyleClass {
        FIELD_ARITHMETIC("highlight-field-arithmetic"),
        POINTER_ARITHMETIC("highlight-pointer-arithmetic"),
        INPUT_OUTPUT("highlight-io"),
        BRACKET_UNBALANCED("highlight-bracket-unbalanced"),
        COMMENT("highlight-comment");
        
        public final String cssClass;
        
        StyleClass(String cssClass) {
            this.cssClass = cssClass;
        }
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting2(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        boolean bracketsBalanced = Analyzer.isBalanced(text);
        
        Map<Integer, Boolean> brackets = new HashMap<>();
        Map<Integer, StyleClass> styleClasses = new HashMap<>();
    
        // Scan through all chars
        {
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (c == '[') {
                    brackets.put(i, BRACKET_OPEN);
                }
                else if (c == ']') {
                    brackets.put(i, BRACKET_CLOSE);
                }
                else if (c == '>' || c == '<') {
                    styleClasses.put(i, StyleClass.FIELD_ARITHMETIC);
                }
                else if (c == '+' || c == '-') {
                    styleClasses.put(i, StyleClass.POINTER_ARITHMETIC);
                }
                else {
                    styleClasses.put(i, StyleClass.COMMENT);
                }
            }
        }
    
//        List<Pair<Pair<Integer, Integer>, StyleClass>> styleClassesList = new ArrayList<>();
//        {
//            int startIndex = 0;
//            StyleClass lastStyleClass = null;
//            List<Map.Entry<Integer, StyleClass>> entries = new ArrayList<>(styleClasses.entrySet());
//            int lastIndex = 0;
//            for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
//                Map.Entry<Integer, StyleClass> entry = entries.get(entryIndex);
//                int index = entry.getKey();
//                if (lastIndex != index - 1 || entry.getValue() != lastStyleClass) {
//                    if (lastStyleClass != null) {
//                        styleClassesList.add(new Pair<>(new Pair<>(startIndex, index), lastStyleClass));
//                    }
//                }
//                else {
//                    startIndex = index;
//                }
//                lastStyleClass = entry.getValue();
//                lastIndex = index;
//            }
//        }
    
        // Stays empty if brackets are not balanced
        HashMap<Integer, Integer> bracketNesting = new HashMap<>();
        if (bracketsBalanced) {
            // Calculate the nesting of brackets
            int nesting = 0;
            for (Map.Entry<Integer, Boolean> entry : brackets.entrySet()) {
                if (entry.getValue()) {
                    nesting++;
                    bracketNesting.put(entry.getKey(), nesting);
                }
                else {
                    bracketNesting.put(entry.getKey(), nesting);
                    nesting--;
                }
            }
        }
        
        return spansBuilder.create();
    }
}
