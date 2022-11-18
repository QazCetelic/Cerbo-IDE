package qaz.code.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Dialog
import javafx.stage.FileChooser
import qaz.code.Cerbo
import qaz.code.model.Operator.Companion.OPERATORS
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

class Snippet {
    val lastResultProperty: ObjectProperty<Result> = SimpleObjectProperty()

    /**
     * Change this, and it will be executed by the execution manager.
     * The result will be saved in lastResultProperty.
     */
    val codeProperty: StringProperty = SimpleStringProperty("")
    val inputProperty: StringProperty = SimpleStringProperty("")
    val nameProperty: StringProperty = SimpleStringProperty(null)

    constructor(name: String) {
        nameProperty.set(name)
        codeProperty.addListener { _, _, _ ->
            ExecutionManager.INSTANCE.process(this)
        }
        // Initially process the sheet
        ExecutionManager.INSTANCE.process(this)
    }

    /**
     * @param maxLineLength The maximum length of a line in the code, use null to disable.
     * @return Whether something has changed.
     */
    fun minify(): Boolean {
        val oldCode = codeProperty.get()
        val newCode = oldCode.filter { c -> c == '\n' || c in OPERATORS }
        val changed = oldCode != newCode
        if (changed) codeProperty.set(newCode)
        return changed
    }

    /**
     * Remove empty lines and trailing whitespace.
     * @return Whether something has changed.
     */
    fun reduceSpacing(): Boolean {
        val newCode = codeProperty.get()
            .replace("\\n{2,}".toRegex(), "\n")
            .trim()
        val changed = newCode != codeProperty.get()
        if (changed) codeProperty.set(newCode)
        return changed
    }

    /**
     * Save sheet to file
     */
    fun save() {
        val fileChooser = FileChooser()
        fileChooser.title = "Save"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Brainfuck Code", "*.bf"))
        var chosenFile = fileChooser.showSaveDialog(Cerbo.getMainStage()) ?: return
        // Cancel if no file was chosen
        try {
            // Ensure file has .bf extension
            if (!chosenFile.name.endsWith(".bf")) {
                chosenFile = File(chosenFile.parent, chosenFile.name + ".bf")
            }
            val writer = FileWriter(chosenFile)
            writer.write(codeProperty.get())
            writer.close() // TODO close when exception occurs
        }
        catch (e: IOException) {
            val dialog = Dialog<String>()
            dialog.title = "Failed to save file"
            dialog.contentText = e.localizedMessage
            dialog.show()
        }
    }

    companion object {
        /**
         * Load a sheet from file
         */
        @JvmStatic
        fun load(): Snippet? {
            val fileChooser = FileChooser()
            fileChooser.title = "Load"
            fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Brainfuck Code", "*.bf"))
            val chosenFile = fileChooser.showOpenDialog(Cerbo.getMainStage())
            return try {
                val sb = StringBuilder()
                val reader = Scanner(chosenFile)
                while (reader.hasNextLine()) {
                    sb.append(reader.nextLine())
                    sb.append('\n')
                }
                reader.close() // TODO close when exception occurs
                val snippet = Snippet(chosenFile.name)
                snippet.codeProperty.set(sb.toString())
                snippet
            }
            catch (e: IOException) {
                val dialog = Dialog<String>()
                dialog.title = "Failed to load file"
                dialog.contentText = e.localizedMessage
                dialog.show()
                null
            }
        }
    }
}