package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import qaz.code.model.Sheet;
import qaz.code.model.Result;

public class SheetView extends BorderPane {
    private final Sheet sheet;
    private final CodePane codePane;
    private final MemoryPane memoryPane;
    private final TextField inputField;
    private final OutputPane outputPane;
    
    public SheetView(Sheet sheet) {
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
        sheet.inputProperty().bindBidirectional(inputField.textProperty());
        
        sheet.lastResultProperty().addListener((observable, oldValue, newValue) -> showResult(newValue));
    }
    
    private void showResult(Result result) {
        // Ensures JavaFX thread is used for modifying UI
        Platform.runLater(() -> {
            if (result instanceof Result.Succes) {
                outputPane.setOutput(result.toString());
                codePane.showResults(((Result.Succes) result).output);
                // Only display memory if execution succeeded
                memoryPane.displayMemory(result.execution);
            }
            if (result instanceof Result.Failure) {
                outputPane.setError(((Result.Failure) result).error);
                // Display failure in memory pane
                memoryPane.invalidate();
            }
        });
    }
}
