package qaz.code;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.List;

public class CodePane extends BorderPane {
    public final CodeArea codeArea;
    public VBox resultsPane;
    public CodePane(String code) {
        codeArea = new CodeArea(code);
        setCenter(codeArea);
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    }

    public void showResults(List<List<Character>> results) {
        resultsPane = new VBox(5);
        setRight(resultsPane);
        for (List<Character> result : results) {
            StringBuilder lineOutput = new StringBuilder();
            for (Character character : result) {
                lineOutput.append(character);
            }
            Label lineOutputLabel = new Label(lineOutput.toString());
            resultsPane.getChildren().add(lineOutputLabel);
        }
    }
}
