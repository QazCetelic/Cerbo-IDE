package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import qaz.code.model.Sheet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeView extends CodeArea {
    private static final Pattern WHITE_SPACE = Pattern.compile("^\\s+");
    public CodeView(Sheet sheet) {
        super(sheet.codeProperty().get());
        // Ensure CSS stylesheet is compatible with this class
        getStyleClass().add("text-area");
        // Add line numbers at the left of the code area
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        // Highlight current line
        setLineHighlighterFill(Color.hsb(0, 0, 1, 0.1));
        setLineHighlighterOn(true);
        // Syntax highlighting for visible lines of code
        getVisibleParagraphs().addModificationObserver(new VisibleParagraphStyler<>(this, Highlighter::computeHighlighting));
        // Sync code with model
        ObservableValue<String> textProperty = textProperty();
        sheet.codeProperty().bind(textProperty);
        sheet.codeProperty().addListener((observable, oldValue, newValue) -> {
            // Only set when it's not the same already to prevent infinite loops
            if (!newValue.equals(getText())) replaceText(newValue);
            triggerHighlight();
        });
        triggerHighlight();
    
        // Auto-indent: insert previous line's indents on enter TODO: check if this works
        addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int caretPosition = getCaretPosition();
                int currentParagraph = getCurrentParagraph();
                Matcher whitespaceMatcher = WHITE_SPACE.matcher(getParagraph(currentParagraph - 1).getSegments().get(0));
                if (whitespaceMatcher.find()) Platform.runLater(() -> insertText(caretPosition, whitespaceMatcher.group()));
            }
        });
    }
    
    public void triggerHighlight() {
        setStyleSpans(0, Highlighter.computeHighlighting(getText()));
    }
}
