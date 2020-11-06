package encounter.tracker.views
import encounter.tracker.controllers.NonPlayerCharacterController
import encounter.tracker.models.CharacterModel
import encounter.tracker.models.CharacterTemplateComboModel
import encountertrackerdb.CharacterTemplate
import javafx.scene.control.Alert
import javafx.scene.control.ComboBox
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import tornadofx.*

class NPCharacterView: View() {
    private val controller: NonPlayerCharacterController by inject()

    var idField : TextField by singleAssign()
    var nameField : TextField by singleAssign()
    var armorClassField : TextField by singleAssign()
    var initiativeField : TextField by singleAssign()
    var maxHealthField : TextField by singleAssign()
    var currentHealthField : TextField by singleAssign()
    var typeField : ComboBox<CharacterTemplate> by singleAssign()
    var descriptionField : TextField by singleAssign()
    val tableData = controller.getCharacterList()
    var selectedTemplate : CharacterTemplate by singleAssign()

    override val root = vbox(10) {

        addClass(Style.view)
        // Heading
        label("Characters") {
            style {
                fontSize = 18.px
            }
        }
        // Details Form
        form {
            fieldset("Character Details") {
                vbox {
                    hbox(20) {
                        field("ID") { textfield() { idField = this } }
                        field("Name") { textfield() { nameField = this } }
                        field("Armor Class") { textfield() { armorClassField = this } }
                    }
                    hbox(20) {
                        field("Initiative") { textfield() { initiativeField = this } }
                        field("Max Health") { textfield() { maxHealthField = this } }
                        field("Current Health") { textfield() { currentHealthField = this } }
                    }
                    hbox(20) {
                        field("Type") {
                            combobox<CharacterTemplate> {
                                items.setAll(controller.getTemplates())
                                typeField = this
                                cellFormat {
                                    text = it.type
                                }
                                setOnAction {
                                    descriptionField.text = this.selectedItem?.description
                                }
                            }
                        }
                        field("Description") { textfield() { descriptionField = this } }
                        descriptionField.text = ""
                        descriptionField.isEditable = false
                    }
                }
            }
        }
        // Control Buttons
        hbox(20) {
            button("Filter") {
                action {
                    tableData.setAll(controller.getFilteredCharacterList(getSelectedCharacter()))
                }
            }
            button("Add Character") {
                action {
                    try {
                        controller.addCharacter(getSelectedCharacter())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem creating the character", e.message, owner = this@NPCharacterView.currentStage)
                    }
                    idField.text = ""
                    tableData.setAll(controller.getCharacterList())
                }
            }
            button("Update Character"){
                action {
                    try {
                        controller.updateCharacter(getSelectedCharacter())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem updating the character", e.message, owner = this@NPCharacterView.currentStage)
                    }
                    tableData.setAll(controller.getCharacterList())
                }
            }
            button("Clear Selection") {
                action {
                    clearSelection()
                    idField.isEditable = true
                    idField.isDisable = false
                    tableData.setAll(controller.getCharacterList())
                }
            }
        }
        // Data Table
        tableview(tableData) {
            selectionModel.selectionMode = SelectionMode.SINGLE
            readonlyColumn("ID", CharacterModel::id)
            readonlyColumn("Name", CharacterModel::name)
            readonlyColumn("Armor Class", CharacterModel::armorClass)
            readonlyColumn("Initiative Modifier", CharacterModel::initiative)
            readonlyColumn("Max Health", CharacterModel::maxHealth)
            readonlyColumn("Current Health", CharacterModel::currentHealth)
            readonlyColumn("Template", CharacterModel::characterTemplateID).cellFormat {
                val template = it?.let { it1 -> controller.getTemplateByID(it1) }
                if (template != null) {
                    graphic = label(template.type ?: "")
                }
            }
            readonlyColumn("Action", CharacterModel::id).cellFormat {
                graphic = hbox(spacing = 5) {
                    button("Delete") {
                        action {
                            // Delete character
                            if (it != null) {
                                controller.deleteCharacter(it)
                            }
                            // Reload table
                            tableData.setAll(controller.getCharacterList())
                        }
                    }
                }
            }

            onLeftClick {
                if (this.selectedItem != null) {
                    val character = this.selectedItem!!
                    idField.text = character.id.toString()
                    idField.isEditable = false
                    idField.isDisable = true
                    nameField.text = character.name
                    armorClassField.text = character.armorClass.toString()
                    initiativeField.text = character.initiative.toString()
                    maxHealthField.text = character.maxHealth.toString()
                    currentHealthField.text = character.currentHealth.toString()
                }
            }
        }
    }

   private fun getSelectedCharacter() : CharacterModel {
        val id : Long? = try {
            idField.text.toLong()
        } catch (e : NumberFormatException ) {
            null
        }
        val armorClass : Long? = try {
            armorClassField.text.toLong()
        } catch (e : NumberFormatException ) {
            null
        }
        val initiative : Long? = try {
            initiativeField.text.toLong()
        } catch (e : NumberFormatException ) {
            null
        }
        val maxHealth : Long? = try {
            maxHealthField.text.toLong()
        } catch (e : NumberFormatException ) {
            null
        }
        val currentHealth : Long? = try {
            currentHealthField.text.toLong()
        } catch (e : NumberFormatException ) {
            null
        }
        val characterTemplateID : Long? = try {
            typeField.selectedItem?.id
        } catch (e : NumberFormatException ) {
           null
        }

        return CharacterModel(
                id,
                nameField.text,
                armorClass,
                initiative,
                maxHealth,
                currentHealth,
                characterTemplateID
        )
    }
    private fun clearSelection() {
        idField.text = ""
        nameField.text = ""
        armorClassField.text = ""
        initiativeField.text = ""
        maxHealthField.text = ""
        currentHealthField.text = ""
    }
}