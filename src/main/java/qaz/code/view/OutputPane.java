package qaz.code.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class OutputPane extends ScrollPane {
    private final HBox outputBox = new HBox(2);
    
    // TODO listen to property instead of getter/setter
    public OutputPane() {
        setContent(outputBox);
        outputBox.setPadding(new Insets(5, 5, 0, 5));
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        setOutput(""); // Initialize layout
    }

    // TODO use byte[] as parameter
    public void setOutput(String output) {
        outputBox.getChildren().clear();
        char[] chars = output.toCharArray();
        if (chars.length != 0) {
            for (int i = 0; i < chars.length; i++) {
                ByteView byteView = new ByteView((byte) chars[i], true, i);
                outputBox.getChildren().add(byteView);
                if (i != chars.length - 1) {
                    Label separator = new Label("\u00BB");
                    separator.getStyleClass().add("output-seperator");
                    outputBox.getChildren().add(separator);
                }
            }
        }
        else {
            Label label = new Label("No output");
            label.setId("no-output");
            outputBox.getChildren().add(label);
        }
    }

    public void setError(String error) {
        outputBox.getChildren().clear();
        Label label = new Label(error);
        label.getStyleClass().add("output-error");
        label.setAlignment(Pos.BOTTOM_CENTER);
        outputBox.getChildren().add(label);
    }
}
