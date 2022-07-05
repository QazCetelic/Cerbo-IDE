package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
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
import qaz.code.model.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodePane extends BorderPane {
    public final CodeArea codeArea;
    public ScrollPane resultsPane;
    private final Sheet sheet;
    
    public CodePane(Sheet sheet) {
        this.sheet = sheet;
        codeArea = new CodeArea(sheet.codeProperty().get());
        codeArea.getStyleClass().add("text-area");
        resultsPane = new ScrollPane();
        setRight(resultsPane);
        resultsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        resultsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        setCenter(codeArea);
        IntFunction<Node> lineNumber = LineNumberFactory.get(codeArea);
        codeArea.setParagraphGraphicFactory(lineNumber);
        codeArea.setLineHighlighterFill(Color.hsb(0, 0, 1, 0.1));
        codeArea.setLineHighlighterOn(true);
        codeArea.getVisibleParagraphs().addModificationObserver(new VisibleParagraphStyler<>(codeArea, Highlighter::computeHighlighting));
        codeArea.textProperty().addListener((observable, oldValue, newValue) -> {
            sheet.codeProperty().set(newValue);
        });
        
        // Sync scrolling from code area to results pane, not bidirectional
        codeArea.estimatedScrollYProperty().addListener((observable, oldValue, newValue) -> {
            // *3 is necessary for some unknown reason
            resultsPane.setVvalue(newValue / codeArea.getTotalHeightEstimate() * 3);
        });
        
        StringBinding lineNumberBinding = Bindings.createStringBinding(() -> {
            int line = codeArea.currentParagraphProperty().getValue() + 1;
            int column = codeArea.caretColumnProperty().getValue();
            
            return line + ":" + column;
        }, codeArea.currentParagraphProperty(), codeArea.caretColumnProperty());
        
        Label lineNumberLabel = new Label();
        lineNumberLabel.textProperty().bind(lineNumberBinding);
        setBottom(lineNumberLabel);
    
        DoubleBinding resultWidthRestriction = widthProperty().multiply(0.20);
        resultsPane.maxWidthProperty().bind(resultWidthRestriction);
        resultsPane.prefWidthProperty().bind(resultWidthRestriction);
        showResults(new ArrayList<>()); // Trigger initial layout
        
        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
            }
        });
    }
    
    // TODO use byte[] as parameter
    public void showResults(List<List<Character>> results) {
        VBox rows = new VBox(0);
        int index = -1;
        for (List<Character> result : results) {
            // Add output for line...
            if (!result.isEmpty()) {
                HBox row = new HBox(3);
                for (Character c : result) {
                    row.getChildren().add(new ByteView((byte) (char) c, true, index++));
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
