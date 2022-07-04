package qaz.code.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;
import qaz.code.model.Execution;
import qaz.code.model.ExecutionManager;
import qaz.code.model.Result;
import qaz.code.view.Highlighter;

import java.util.ArrayList;

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
}
