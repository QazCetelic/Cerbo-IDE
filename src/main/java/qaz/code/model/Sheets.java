package qaz.code.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Sheets {
    private final SetProperty<Sheet> sheets = new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
    public SetProperty<Sheet> sheetsProperty() {
        return sheets;
    }
    
    private final ObjectProperty<Sheet> selectedSheetProperty = new SimpleObjectProperty<>();
    public ObjectProperty<Sheet> selectedSheetProperty() {
        return selectedSheetProperty;
    }
    
    public void select(@NotNull Sheet sheet) {
        Objects.requireNonNull(sheet);
        // Ensure that the sheet is in the set of sheets.
        sheets.add(sheet);
        selectedSheetProperty.set(sheet);
    }
}
