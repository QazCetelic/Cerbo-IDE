package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import qaz.code.Cerbo;
import qaz.code.model.Result;
import qaz.code.model.Sheet;

public class SheetPane extends BorderPane {
    private final Sheet sheet;
    private final CodePane codePane;
    private final MemoryPane memoryPane;
    private final TextField inputField;
    private final OutputPane outputPane;
    private final OperationsView operationsView;
    
    public SheetPane(Sheet sheet, Cerbo cerbo) {
        this.sheet = sheet;
        codePane = new CodePane(sheet);
        memoryPane = new MemoryPane();
        memoryPane.visibleProperty().bind(cerbo.views.showMemoryPane);
        inputField = new TextField();
        outputPane = new OutputPane();
        outputPane.visibleProperty().bind(cerbo.views.showOutputView);
        operationsView = new OperationsView(sheet);
        operationsView.visibleProperty().bind(cerbo.views.showOperationsView);
        
        setCenter(codePane);
        
        BorderPane left = new BorderPane();
        DoubleBinding leftWidth = widthProperty().multiply(0.25);
        left.maxWidthProperty().bind(leftWidth);
        left.prefWidthProperty().bind(leftWidth);
        left.setCenter(memoryPane);
        operationsView.maxHeightProperty().bind(maxHeightProperty().multiply(0.45));
        operationsView.prefWidthProperty().bind(leftWidth);
        memoryPane.prefWidthProperty().bind(leftWidth);
        left.setBottom(operationsView);
        setLeft(left);
        setBottom(inputField);
        setTop(outputPane);
        
        outputPane.maxWidthProperty().bind(maxWidthProperty());
        
        inputField.setPromptText("Input");
        sheet.inputProperty().bindBidirectional(inputField.textProperty());
        
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
