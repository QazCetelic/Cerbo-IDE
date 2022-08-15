package qaz.code.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;

public class Sheet extends BorderPane {
    // TODO don't have these classes for individual sheets but for the whole application
    private final ObjectProperty<Result> lastResultProperty = new SimpleObjectProperty<>();
    
    /**
     * Change this, and it will be executed by the execution manager.
     * The result will be saved in lastResultProperty.
     */
    private final StringProperty codeProperty = new SimpleStringProperty("");
    private final StringProperty inputProperty = new SimpleStringProperty("");
    private final StringProperty nameProperty = new SimpleStringProperty(null);
    
    public Sheet(String name) {
        this.nameProperty.set(name);
        codeProperty.addListener((observable, oldValue, newValue) -> ExecutionManager.INSTANCE.process(this));
    }
    
    public StringProperty codeProperty() {
        return codeProperty;
    }
    
    public StringProperty nameProperty() {
        return nameProperty;
    }
    
    public StringProperty inputProperty() {
        return inputProperty;
    }
    
    public ObjectProperty<Result> lastResultProperty() {
        return lastResultProperty;
    }
    
    /**
     * @param maxLineLength The maximum length of a line in the code, use null to disable.
     * @return Whether something has changed.
     */
    public boolean minify(Integer maxLineLength) {
        System.out.println("Minifying " + nameProperty.get());
        System.out.println("Old code:\n" + codeProperty.get());
        char[] code = codeProperty.get().toCharArray();
        int startingHash = Arrays.hashCode(code);
        int newLineCounter = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : code) {
            if (Analyzer.OPERATORS.contains(c)) {
                sb.append(c);
                // Don't do anything with line length if it's disabled
                if (maxLineLength != null) {
                    newLineCounter++;
                    if (newLineCounter == 50) {
                        sb.append('\n');
                        newLineCounter = 0;
                    }
                }
            }
        }
        String newCode = sb.toString();
        System.out.println("New code:\n" + newCode);
        boolean changed = Arrays.hashCode(newCode.toCharArray()) != startingHash;
        codeProperty.set(newCode);
        return changed;
    }
}
