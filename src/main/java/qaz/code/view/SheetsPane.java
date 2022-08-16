package qaz.code.view;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import qaz.code.model.Sheet;
import qaz.code.model.Sheets;

import java.util.concurrent.atomic.AtomicInteger;

public class SheetsPane extends TabPane {
    private final Sheets sheets;
    public SheetsPane(Sheets sheets) {
        this.sheets = sheets;
        
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                SheetTab tab = (SheetTab) newValue;
                if (tab != null) sheets.select(tab.getSheet());
            });
        });
        sheets.sheetsProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                getTabs().clear();
                getTabs().addAll(newValue.stream().map(sheet -> new SheetTab(sheet, sheets)).toList());
            });
        });
    }
    
    public void addEmptySheet() {
        Sheet sheet = new Sheet(getUsableName());
        sheets.sheetsProperty().add(sheet);
    }

    private String getUsableName() {
        AtomicInteger num = new AtomicInteger(0);
        String name;
        do {
            num.incrementAndGet();
            name = "Untitled " + num;
        }
        while (getTabs().stream().anyMatch(tab -> tab.getText().equals("Untitled " + num)));
        return name;
    }

    public SheetTab getSelectedSheetTab() {
        return (SheetTab) getSelectionModel().getSelectedItem();
    }
}
