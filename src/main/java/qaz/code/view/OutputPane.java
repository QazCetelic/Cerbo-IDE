package qaz.code.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;

public class OutputPane extends HBox {
    public OutputPane() {

    }

    // TODO use byte[] as parameter
    public void setOutput(String output) {
        getChildren().clear();
        char[] chars = output.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            ByteDisplay byteDisplay = new ByteDisplay((byte) chars[i], true);
            getChildren().add(byteDisplay);
            if (i != chars.length - 1) {
                getChildren().add(new Label("|"));
            }
        }
    }

    public void setError(String error) {
        getChildren().clear();
        Label label = new Label(error);
        label.setId("output-error");
        label.setAlignment(Pos.BOTTOM_CENTER);
        getChildren().add(label);
    }
}
