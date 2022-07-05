package qaz.code.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import qaz.code.model.Analyzer;
import qaz.code.model.Sheet;

import java.util.ArrayList;
import java.util.List;

public class CodePane extends BorderPane {
    public final CodeView codeView;
    public ScrollPane resultsPane;
    private final Sheet sheet;
    
    public CodePane(Sheet sheet) {
        this.sheet = sheet;
        codeView = new CodeView(sheet);
        resultsPane = new ScrollPane();
        setRight(resultsPane);
        resultsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        resultsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        setCenter(codeView);
        
        // Sync scrolling from code area to results pane, not bidirectional
        codeView.estimatedScrollYProperty().addListener((observable, oldValue, newValue) -> {
            // *3 is necessary for some unknown reason
            resultsPane.setVvalue(newValue / codeView.getTotalHeightEstimate() * 3);
        });
        
        StringBinding lineNumberBinding = Bindings.createStringBinding(() -> {
            int line = codeView.currentParagraphProperty().getValue() + 1;
            int column = codeView.caretColumnProperty().getValue();
            return line + ":" + column;
        }, codeView.currentParagraphProperty(), codeView.caretColumnProperty());
        Label lineNumberLabel = new Label();
        lineNumberLabel.textProperty().bind(lineNumberBinding);
        
        StringBinding selectionBinding = Bindings.createStringBinding(() -> {
            int chars = codeView.getSelection().getLength();
            int newLines = 0;
            int operators = 0;
            for (char c : codeView.getSelectedText().toCharArray()) {
                if (c == '\n') {
                    newLines++;
                }
                else if (Analyzer.OPERATORS.contains(c)) {
                    operators++;
                }
            }
            if (chars == 0) return "";
            else return "Selected " + chars + " characters (" + newLines + " newlines, " + operators + " operators)";
        }, codeView.selectionProperty());
        Label selectionLabel = new Label();
        selectionLabel.textProperty().bind(selectionBinding);
        
        HBox infoBox = new HBox(25, lineNumberLabel, selectionLabel);
        setBottom(infoBox);
    
        DoubleBinding resultWidthRestriction = widthProperty().multiply(0.20);
        resultsPane.maxWidthProperty().bind(resultWidthRestriction);
        resultsPane.prefWidthProperty().bind(resultWidthRestriction);
        showResults(new ArrayList<>()); // Trigger initial layout
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
