package qaz.code;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MemoryPane extends BorderPane {
    private Label filled;
    private Label lastIndex;
    private ScrollPane scrollPane;
    private GridPane gridPane;
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

        gridPane = new GridPane();
        gridPane.setId("memory-grid");
        gridPane.setHgap(5);

        byte[] memory = execution.getMemory();

        // Look for the last index of the memory array that is not 0
        for (int i = 0; i <= execution.getLastFilledIndex(); i++) {
            Label label = new Label(String.format("%03X", memory[i]));
            if (memory[i] == 0) {
                label.setId("memory-data-empty");
            }
            else {
                label.setId("memory-data-filled");
                label.setTooltip(new Tooltip(String.valueOf((char) memory[i])));
            }
            gridPane.add(label, i % 4, i / 4);
        }
        scrollPane.setContent(gridPane);
    }
}
