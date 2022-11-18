package qaz.code.view;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import qaz.code.Cerbo;
import qaz.code.model.Result;
import qaz.code.model.Snippet;

public class SheetPane extends BorderPane {
    private final CodePane codePane;
    private final MemoryPane memoryPane;
    private final TextField inputField;
    private final OutputPane outputPane;
    private final OperationsView operationsView;
    
    public SheetPane(Snippet snippet, Cerbo cerbo) {
        codePane = new CodePane(snippet, cerbo);
        memoryPane = new MemoryPane();
        memoryPane.visibleProperty().bind(cerbo.views.showMemoryPane);
        inputField = new TextField();
        outputPane = new OutputPane();
        outputPane.visibleProperty().bind(cerbo.views.showOutputView);
        operationsView = new OperationsView(snippet);
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
        snippet.getInputProperty().bindBidirectional(inputField.textProperty());
        
        snippet.getLastResultProperty().addListener((observable, oldValue, newValue) -> showResult(newValue));
        showResult(snippet.getLastResultProperty().get());
    }
    
    private void showResult(Result result) {
        Platform.runLater(() -> {
            if (result instanceof Result.Succes) {
                outputPane.setOutput(result.toString());
                codePane.showResults(((Result.Succes) result).getOutput());
                // Only update memory view if execution succeeded
                memoryPane.displayMemory(result.getExecution());
            }
            if (result instanceof Result.Failure) {
                outputPane.setError(((Result.Failure) result).getError());
                // Display failure in memory pane
                memoryPane.invalidate();
            }
        });
    }
}
