package qaz.code.view;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ByteView extends Label {
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("000");
    
    /**
     * Creates a new ByteDisplay.
     * @param b The byte to display.
     * @param showChar Whether to show byte as a character if it's a character.
     */
    public ByteView(byte b, boolean showChar, int index) {
        super(NUMBER_FORMAT.format(b));
        getStyleClass().add("memory-field");
        Tooltip tooltip;
        if (b == 0) {
            tooltip = new Tooltip(index + ": Empty field");
        }
        else {
            getStyleClass().add("memory-field-filled");
            boolean isChar = (b >= 32 && b <= 126);
            if (isChar) {
                getStyleClass().add("memory-field-char");
                char c = (char) b;
                tooltip = new Tooltip(index + ": Character '" + c + "'");
                if (showChar) {
                    setText(String.valueOf(c));
                }
            }
            else {
                tooltip = new Tooltip(index + ": Number " + b);
            }
        }
        setTooltip(tooltip);
    }
    
}
