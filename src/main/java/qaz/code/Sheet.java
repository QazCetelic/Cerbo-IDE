package qaz.code;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.ArrayList;

public class Sheet extends BorderPane {
    // Execution of code
    private final ExecutionMananger executionMananger = new ExecutionMananger();
    private Execution lastExecution;

    public Execution getLastExecution() {
        return lastExecution;
    }

    // UI components
    private final CodePane codePane;
    private final MemoryPane memoryPane;
    private final TextField inputField;
    private final OutputPane outputPane;

    public Sheet(String text) {
        codePane = new CodePane(text);
        memoryPane = new MemoryPane();
        inputField = new TextField();
        outputPane = new OutputPane();
        inputField.setPromptText("Input");
        setCenter(codePane);
        setRight(memoryPane);
        setBottom(inputField);
        setTop(outputPane);
        codePane.setOnKeyReleased(e -> execute());
        inputField.setOnKeyReleased(e -> execute());
    }

    public void setResult(Execution.Result result, Execution execution) {
        lastExecution = execution;
        // Ensures JavaFX thread is used for modifying UI
        Platform.runLater(() -> {
            if (result instanceof Execution.Succes) {
                outputPane.setOutput(result.toString());
                codePane.showResults(((Execution.Succes) result).output);
            }
            if (result instanceof Execution.Failure) {
                outputPane.setError(((Execution.Failure) result).error);
            }
            memoryPane.displayMemory(execution);
        });
    }

    public void execute() {
        String code = codePane.codeArea.getText();
        if (Analyzer.isBalanced(code)) {
            // Fills ArrayList with characters from input field
            ArrayList<Character> input = new ArrayList<>();
            for (char c : inputField.getText().toCharArray()) input.add(c);

            executionMananger.execute(code, input, this);
        }
    }
}
