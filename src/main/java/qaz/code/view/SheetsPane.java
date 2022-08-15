package qaz.code.view;

import javafx.scene.control.TabPane;
import qaz.code.Cerbo;
import qaz.code.model.Sheet;

import java.util.concurrent.atomic.AtomicInteger;

public class SheetsPane extends TabPane {
    public SheetsPane() {
        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SheetTab tab = getSelectedSheetTab();
            Cerbo.selectedSheetProperty().set(tab.getSheet());
        });
    }
    public void addSheet(SheetTab sheetTab) {
        getTabs().add(sheetTab);
    }

    public void addEmptySheet() {
        Sheet sheet = new Sheet(getUsableName());
        addSheet(new SheetTab(sheet));
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
