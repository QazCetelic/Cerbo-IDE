package qaz.code;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class OutputPane extends HBox {
    public OutputPane() {

    }

    public void setOutput(String output) {
        getChildren().clear();
        for (char c : output.toCharArray()) {
            Label label;
            String charString = String.valueOf(c);
            if (c < 32 || c > 126) {
                label = new Label(Integer.toOctalString(c));
                label.setId("output-octal");
            }
            else {
                label = new Label(charString);
                label.setId("output-normal");
            }
            label.setAlignment(Pos.BOTTOM_CENTER);
            getChildren().add(label);
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
