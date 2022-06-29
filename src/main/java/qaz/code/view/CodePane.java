package qaz.code.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class CodePane extends BorderPane {
    public final CodeArea codeArea;
    public ScrollPane resultsPane;
    public CodePane(String code) {
        codeArea = new CodeArea(code);
        resultsPane = new ScrollPane();
        resultsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        resultsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(codeArea);
        IntFunction<Node> lineNumber = LineNumberFactory.get(codeArea);
        codeArea.setParagraphGraphicFactory(lineNumber);
    
        DoubleBinding resultWidthRestriction = widthProperty().multiply(0.20);
        resultsPane.maxWidthProperty().bind(resultWidthRestriction);
        resultsPane.prefWidthProperty().bind(resultWidthRestriction);
        showResults(new ArrayList<>()); // Trigger initial layout
    }
    
    // TODO use byte[] as parameter
    public void showResults(List<List<Character>> results) {
        System.out.println("Showing results " + results);
        VBox rows = new VBox();
        setRight(resultsPane);
        for (List<Character> result : results) {
            HBox row = new HBox(3);
            if (!result.isEmpty()) {
                for (Character c : result) {
                    row.getChildren().add(new ByteDisplay((byte) (char) c, true));
                }
                rows.getChildren().add(row);
            }
            // When a line has no output, add a blank row
            else {
                rows.getChildren().add(new Label(""));
            }
        }
        resultsPane.setContent(rows);
    }
}
