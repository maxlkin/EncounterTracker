package encounter.tracker.views

import encounter.tracker.controllers.TemplateController
import encounter.tracker.models.CharacterTemplateModel
import encountertrackerdb.CharacterTemplate
import javafx.scene.control.Alert
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import tornadofx.*

class TemplateView : View() {
    private val controller: TemplateController by inject()

    var idField : TextField by singleAssign()
    var typeField : TextField by singleAssign()
    var descriptionField : TextField by singleAssign()

    val tableData = controller.getTemplateList()

    override val root = vbox(10) {
        addClass(Style.view)
        // Heading
        label("Templates") {
            style {
                fontSize = 18.px
            }
        }
        // Details Form
        form {
            fieldset("Template Details") {
                vbox {
                    hbox(20) {
                        field("ID") { textfield() { idField = this } }
                        field("Type") { textfield() { typeField = this } }
                        field("Description") { textfield() { descriptionField = this } }
                    }
                }
            }
        }
        // Control Buttons
        hbox(20) {
            button("Filter") {
                action {
                    tableData.setAll(controller.getFilteredTemplateList(getSelectedTemplate()))
                }
            }
            button("Add Character") {
                action {
                    try {
                        controller.addTemplate(getSelectedTemplate())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem creating the template", e.message)
                    }
                    idField.text = "-"
                    tableData.setAll(controller.getTemplateList())
                }
            }
            button("Update Character"){
                action {
                    try {
                        controller.updateTemplate(getSelectedTemplate())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem updating the character", e.message)
                    }
                    tableData.setAll(controller.getTemplateList())
                }
            }
            button("Clear Selection") {
                action {
                    clearSelection()
                    idField.isEditable = true
                    idField.isDisable = false
                    tableData.setAll(controller.getTemplateList())
                }
            }
        }
        // Data Table
        tableview(tableData) {
            selectionModel.selectionMode = SelectionMode.SINGLE
            readonlyColumn("ID", CharacterTemplate::id)
            readonlyColumn("Type", CharacterTemplate::type)
            readonlyColumn("Description", CharacterTemplate::description)
            readonlyColumn("Action", CharacterTemplate::id).cellFormat {
                graphic = hbox(spacing = 5) {
                    button("Delete") {
                        action {
                            // Delete character
                            controller.deleteTemplate(it)
                            // Reload table
                            tableData.setAll(controller.getTemplateList())
                        }
                    }
                }
            }

            onLeftClick {
                if (this.selectedItem != null) {
                    val template = this.selectedItem!!
                    idField.text = template.id.toString()
                    idField.isEditable = false
                    idField.isDisable = true
                    typeField.text = template.type
                    descriptionField.text = template.description
                }
            }
        }
    }

    private fun getSelectedTemplate() : CharacterTemplateModel {
        val id : Long? = try {
            idField.text.toLong()
        } catch (e : NumberFormatException ) {
            null
        }

        return CharacterTemplateModel(
                id,
                typeField.text,
                descriptionField.text
        )
    }

    private fun clearSelection() {
        idField.text = "-"
        typeField.text = ""
        descriptionField.text = ""
    }
}