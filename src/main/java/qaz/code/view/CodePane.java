package qaz.code.view;

import com.github.mouse0w0.darculafx.DarculaFX;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.*;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodePane extends BorderPane {
    private final Highlighter highlighter = new Highlighter();
    public final CodeArea codeArea;
    public ScrollPane resultsPane;
    
    public CodePane(String code) {
        codeArea = new CodeArea(code);
        codeArea.getStyleClass().add("text-area");
        resultsPane = new ScrollPane();
        setRight(resultsPane);
        resultsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        resultsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(codeArea);
        IntFunction<Node> lineNumber = LineNumberFactory.get(codeArea);
        codeArea.setParagraphGraphicFactory(lineNumber);
        codeArea.setLineHighlighterFill(Color.hsb(0, 0, 1, 0.1));
        codeArea.setLineHighlighterOn(true);
        codeArea.getVisibleParagraphs().addModificationObserver(new VisibleParagraphStyler<>(codeArea, Highlighter::computeHighlighting));
    
        DoubleBinding resultWidthRestriction = widthProperty().multiply(0.20);
        resultsPane.maxWidthProperty().bind(resultWidthRestriction);
        resultsPane.prefWidthProperty().bind(resultWidthRestriction);
        showResults(new ArrayList<>()); // Trigger initial layout
        
        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
            }
        });
    }
    
    // TODO use byte[] as parameter
    public void showResults(List<List<Character>> results) {
        VBox rows = new VBox();
        int index = -1;
        for (List<Character> result : results) {
            // Add output for line...
            if (!result.isEmpty()) {
                HBox row = new HBox(3);
                for (Character c : result) {
                    row.getChildren().add(new ByteDisplay((byte) (char) c, true, index++));
                }
                rows.getChildren().add(row);
            }
            // ...or when a line has no output, add a blank row
            else {
                rows.getChildren().add(new Label(""));
            }
        }
        resultsPane.setContent(rows);
    }
}
