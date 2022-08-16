package qaz.code.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;
import qaz.code.Cerbo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

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
    
    /**
     * Remove empty lines and trailing whitespace.
     * @return Whether something has changed.
     */
    public boolean reduceSpacing() {
        String newCode = codeProperty.get()
                                     .replaceAll("\\n{2}", "\n")
                                     .replaceAll("\\s+$", "");
        boolean changed = !newCode.equals(codeProperty.get());
        codeProperty.set(newCode);
        return changed;
    }
    
    /**
     * Save sheet to file
     */
    public void save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Brainfuck Code", "*.bf"));
        File chosenFile = fileChooser.showSaveDialog(Cerbo.getMainStage());
        if (chosenFile == null) return; // Cancel if no file was chosen
        try {
            // Ensure file has .bf extension
            if (!chosenFile.getName().endsWith(".bf")) {
                chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + ".bf");
            }
            FileWriter writer = new FileWriter(chosenFile);
            writer.write(codeProperty.get());
            writer.close(); // TODO close when exception occurs
        }
        catch (IOException e) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Failed to save file");
            dialog.setContentText(e.getLocalizedMessage());
            dialog.show();
        }
    }
    
    /**
     * Load a sheet from file
     */
    public static @Nullable Sheet load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Brainfuck Code", "*.bf"));
        File chosenFile = fileChooser.showOpenDialog(Cerbo.getMainStage());
        try {
            StringBuilder sb = new StringBuilder();
            Scanner reader = new Scanner(chosenFile);
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
                sb.append('\n');
            }
            reader.close(); // TODO close when exception occurs
            Sheet sheet = new Sheet(chosenFile.getName());
            sheet.codeProperty.set(sb.toString());
            return sheet;
        }
        catch (IOException e) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Failed to load file");
            dialog.setContentText(e.getLocalizedMessage());
            dialog.show();
            return null;
        }
    }
}
