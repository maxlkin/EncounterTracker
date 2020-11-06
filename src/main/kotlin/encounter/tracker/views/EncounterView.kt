package encounter.tracker.views

import encounter.tracker.controllers.EncounterController
import encounter.tracker.models.EncounterModel
import encountertrackerdb.Encounter
import javafx.scene.control.Alert
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import tornadofx.*

class EncounterView :  View() {
    private val controller: EncounterController by inject()

    var idField : TextField by singleAssign()
    var nameField : TextField by singleAssign()
    var encounterName = "TEST"

    var tableData = controller.getEncounterList()

    override val root = vbox(10) {
        addClass(Style.view)
        // Heading
        label("Encounter view") {
            style {
                fontSize = 18.px
            }
        }
        // Details Form
        form {
            fieldset("Encounter Details") {
                vbox {
                    hbox(20) {
                        field("ID") { textfield() { idField = this } }
                        field("Name") { textfield() { nameField = this } }
                    }
                }
            }
        }
        // Control Buttons
        hbox(20) {
            button("Add Encounter") {
                action {
                    try {
                        controller.addEncounter(getSelectedEncounter())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem creating the character", e.message)
                    }
                    idField.text = ""
                    tableData.setAll(controller.getEncounterList())
                }
            }
            button("Update Encounter") {
                action {
                    try {
                        //controller.updateCharacter(getSelectedEncounter())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem updating the character", e.message)
                    }
                    tableData.setAll(controller.getEncounterList())
                }
            }
            button("Clear Selection") {
                action {
                    clearSelection()
                    idField.isEditable = true
                    idField.isDisable = false
                    tableData.setAll(controller.getEncounterList())
                }
            }
        }
        tableview(tableData) {
            selectionModel.selectionMode = SelectionMode.SINGLE
            readonlyColumn("ID", Encounter::id)
            readonlyColumn("Name", Encounter::name)
            readonlyColumn("Action", Encounter::id).cellFormat {
                graphic = hbox(spacing = 5) {
                    button("Select") {
                        action {
                            find<SingleEncounterView>().setEncounter(it)
                            replaceWith(SingleEncounterView::class)
                        }
                    }
                }
            }
            onLeftClick {
                nameField.text = this.selectedItem?.name
                encounterName = this.selectedItem?.name ?: "Unknown"
                idField.text = this.selectedItem?.id.toString()
                idField.isEditable = false
                idField.isDisable = true
            }
        }
    }

    private fun getSelectedEncounter(): EncounterModel {
        return EncounterModel(null, nameField.text)
    }

    private fun clearSelection() {
        idField.text = ""
        nameField.text = ""
    }
}