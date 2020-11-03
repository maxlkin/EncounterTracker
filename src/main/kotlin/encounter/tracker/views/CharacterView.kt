package encounter.tracker.views
import encounter.tracker.controllers.CharacterController
import encounter.tracker.models.CharacterModel
import encountertrackerdb.Character
import javafx.scene.control.Alert
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import tornadofx.*

class CharacterView: View() {
    private val controller: CharacterController by inject()

    var idField : TextField by singleAssign()
    var nameField : TextField by singleAssign()
    var armorClassField : TextField by singleAssign()
    var initiativeField : TextField by singleAssign()
    var maxHealthField : TextField by singleAssign()
    var currentHealthField : TextField by singleAssign()
    //var selectedCharacter: Character by singleAssign()
    val tableData = controller.getCharacterList()

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
                        alert(Alert.AlertType.ERROR, "There was a problem creating the character", e.message)
                    }
                    idField.text = "-"
                    tableData.setAll(controller.getCharacterList())
                }
            }
            button("Update Character"){
                action {
                    try {
                        controller.updateCharacter(getSelectedCharacter())
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "There was a problem updating the character", e.message)
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
            readonlyColumn("ID", Character::id)
            readonlyColumn("Name", Character::name)
            readonlyColumn("Armor Class", Character::armor_class)
            readonlyColumn("Initiative Modifier", Character::initiative_modifier)
            readonlyColumn("Max Health", Character::max_health)
            readonlyColumn("Current Health", Character::current_health)
            readonlyColumn("Action", Character::id).cellFormat {
                graphic = hbox(spacing = 5) {
                    button("Delete") {
                        action {
                            controller.deleteCharacter(it)
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
                    armorClassField.text = character.armor_class.toString()
                    initiativeField.text = character.initiative_modifier.toString()
                    maxHealthField.text = character.max_health.toString()
                    currentHealthField.text = character.current_health.toString()
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

        return CharacterModel(
                id,
                nameField.text,
                armorClass,
                initiative,
                maxHealth,
                currentHealth,
        )
    }

    private fun clearSelection() {
        idField.text = "-"
        nameField.text = ""
        armorClassField.text = ""
        initiativeField.text = ""
        maxHealthField.text = ""
        currentHealthField.text = ""
    }
}