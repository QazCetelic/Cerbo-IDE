package qaz.code.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import qaz.code.model.Execution;
import qaz.code.view.ByteDisplay;

public class MemoryPane extends BorderPane {
    private Label filled;
    private Label lastIndex;
    private ScrollPane scrollPane;
    public MemoryPane() {
        scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(scrollPane);
    }

    public void displayMemory(Execution execution) {
        filled = new Label(execution.getAmountNotEmpty() + " filled");
        setTop(filled);
        lastIndex = new Label("last index: " + execution.getLastFilledIndex());
        setBottom(lastIndex);

        FlowPane flowPane = new FlowPane(0, 0);
        flowPane.setHgap(5);
        flowPane.maxWidthProperty().bind(scrollPane.widthProperty().subtract(new ScrollBar().getWidth()));

        byte[] memory = execution.getMemory();
        DoubleBinding rowsBinding = widthProperty().divide(new ByteDisplay((byte) 0, false).getWidth());
        // Look for the last index of the memory array that is not 0
        for (int i = 0; i <= execution.getLastFilledIndex(); i++) {
            ByteDisplay byteDisplay = new ByteDisplay(memory[i], false);
            byteDisplay.getStyleClass().add("memory-grid");
            flowPane.getChildren().add(byteDisplay);
        }
        scrollPane.setContent(flowPane);
    }
}
