package qaz.code.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;

public class OutputPane extends HBox {
    // TODO listen to property instead of getter/setter
    public OutputPane() {
        setOutput(""); // Initialize layout
    }

    // TODO use byte[] as parameter
    public void setOutput(String output) {
        getChildren().clear();
        char[] chars = output.toCharArray();
        if (chars.length != 0) {
            for (int i = 0; i < chars.length; i++) {
                ByteDisplay byteDisplay = new ByteDisplay((byte) chars[i], true, i);
                getChildren().add(byteDisplay);
                if (i != chars.length - 1) {
                    Label separator = new Label("\u00BB");
                    separator.getStyleClass().add("output-seperator");
                    getChildren().add(separator);
                }
            }
        }
        else {
            Label label = new Label("No output");
            label.setId("no-output");
            getChildren().add(label);
        }
    }

    public void setError(String error) {
        getChildren().clear();
        Label label = new Label(error);
        label.getStyleClass().add("output-error");
        label.setAlignment(Pos.BOTTOM_CENTER);
        getChildren().add(label);
    }
}
