package qaz.code;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import qaz.code.model.Execution;
import qaz.code.model.ExecutionManager;
import qaz.code.view.CodePane;
import qaz.code.view.MemoryPane;
import qaz.code.view.OutputPane;
import qaz.code.view.Highlighter;

import java.util.ArrayList;

public class Sheet extends BorderPane {
    // TODO don't have these classes for individual sheets but for the whole application
    private final ExecutionManager executionManager = new ExecutionManager();
    private final Highlighter highlighter = new Highlighter();
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
        
        setCenter(codePane);
        setLeft(memoryPane);
        setBottom(inputField);
        setTop(outputPane);
        
        outputPane.maxWidthProperty().bind(maxWidthProperty());
        DoubleBinding memoryPaneWidth = widthProperty().multiply(0.25);
        memoryPane.maxWidthProperty().bind(memoryPaneWidth);
        memoryPane.prefWidthProperty().bind(memoryPaneWidth);
    
        inputField.setPromptText("Input");
        codePane.codeArea.textProperty().addListener(e -> execute());
        inputField.textProperty().addListener(e -> execute());
    }

    public void setResult(Execution.Result result, Execution execution) {
        lastExecution = execution;
        // Ensures JavaFX thread is used for modifying UI
        Platform.runLater(() -> {
            if (result instanceof Execution.Succes) {
                outputPane.setOutput(result.toString());
                codePane.showResults(((Execution.Succes) result).output);
                // Only display memory if execution succeeded
                memoryPane.displayMemory(execution);
            }
            if (result instanceof Execution.Failure) {
                outputPane.setError(((Execution.Failure) result).error);
                // Display failure in memory pane
                memoryPane.invalidate();
            }
        });
    }

    public void execute() {
        String code = codePane.codeArea.getText();
        // Fills ArrayList with characters from input field
        ArrayList<Character> input = new ArrayList<>();
        for (char c : inputField.getText().toCharArray()) input.add(c);
    
        executionManager.process(code, input, this);
    }
}
