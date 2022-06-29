package qaz.code.view;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class ByteDisplay extends Label {
    /**
     * Creates a new ByteDisplay.
     * @param b The byte to display.
     * @param showChar Whether to show byte as a character if it's a character.
     */
    public ByteDisplay(byte b, boolean showChar) {
        super(String.format("%03X", b));
        if (b == 0) {
            setId("memory-data-empty");
        }
        else {
            char c = (char) b;
            if (showChar && c >= 32 && c <= 126) {
                setText(String.valueOf(c));
                setId("memory-data-normal");
            }
            else {
                setId("memory-data-filled");
                setTooltip(new Tooltip(String.valueOf((char) b)));
            }
        }
        
    }
}
