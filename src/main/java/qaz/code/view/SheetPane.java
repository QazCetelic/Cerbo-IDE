package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import qaz.code.model.Result;
import qaz.code.model.Sheet;

public class SheetPane extends BorderPane {
    private final Sheet sheet;
    private final CodePane codePane;
    private final MemoryPane memoryPane;
    private final TextField inputField;
    private final OutputPane outputPane;
    
    public SheetPane(Sheet sheet) {
        this.sheet = sheet;
        codePane = new CodePane(sheet);
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
        sheet.inputProperty().bind(inputField.textProperty());
        
        sheet.lastResultProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> showResult(newValue)));
    }
    
    private void showResult(Result result) {
        if (result instanceof Result.Succes) {
            outputPane.setOutput(result.toString());
            codePane.showResults(((Result.Succes) result).output);
            // Only update memory view if execution succeeded
            memoryPane.displayMemory(result.execution);
        }
        if (result instanceof Result.Failure) {
            outputPane.setError(((Result.Failure) result).error);
            // Display failure in memory pane
            memoryPane.invalidate();
        }
    }
}
