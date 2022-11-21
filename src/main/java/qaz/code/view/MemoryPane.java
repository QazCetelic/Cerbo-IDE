package qaz.code.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import qaz.code.model.Execution;

public class MemoryPane extends BorderPane {
    private static final double SCROLL_BAR_WIDTH = 5;
    private final ScrollPane scrollPane;
    
    public MemoryPane() {
        scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(scrollPane);
    }
    
    public void displayMemory(Execution execution) {
        FlowPane flowPane = new FlowPane(5, 5);
        flowPane.setPadding(new Insets(5));
        flowPane.maxWidthProperty().bind(scrollPane.widthProperty().subtract(SCROLL_BAR_WIDTH));
        
        setDisabled(false);
        Label filled = new Label(execution.getAmountNotEmpty() + " filled");
        setTop(filled);
        
        Hyperlink lastIndexButton = new Hyperlink("Last index: " + execution.getLastFilledIndex());
        lastIndexButton.setTooltip(new Tooltip("Navigate to last filled index"));
        lastIndexButton.setOnMouseClicked(e -> {
            Node last = flowPane.getChildren().get(flowPane.getChildren().size() - 1);
            ensureVisible(scrollPane, last);
        });
        setBottom(lastIndexButton);
        
        byte[] memory = execution.getMemoryCloned();
        int lastIndex = execution.getLastFilledIndex();
        // This caps the amount of memory displayed to 2500 to prevent lag, TODO find more efficient way
        for (int i = 0; i <= Math.min(lastIndex, 2_500); i++) {
            ByteView byteDisplay = new ByteView(memory[i], false, i);
            byteDisplay.getStyleClass().add("memory-grid");
            flowPane.getChildren().add(byteDisplay);
        }
        flowPane.getChildren().add(new Label("..."));
        
        scrollPane.setContent(flowPane);
    }
    
    public void invalidate() {
        setDisabled(true);
    }
    
    private static void ensureVisible(ScrollPane pane, Node node) {
        double width = pane.getContent().getBoundsInLocal().getWidth();
        double height = pane.getContent().getBoundsInLocal().getHeight();
        
        double x = node.getBoundsInParent().getMaxX();
        double y = node.getBoundsInParent().getMaxY();
        
        // scrolling values range from 0 to 1
        pane.setVvalue(y / height);
        pane.setHvalue(x / width);
        
        // just for usability
        node.requestFocus();
    }
    
}
