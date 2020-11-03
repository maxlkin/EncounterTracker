package encounter.tracker.views
import encounter.tracker.controllers.CharacterController
import encountertrackerdb.Character
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
            button("Filter")
            button("Add Character") {
                action {
                    controller.addCharacter(getSelectedCharacter())
                    idField.text = "-"
                    tableData.setAll(controller.getCharacterList())
                }
            }
            button("Update Character"){
                action {
                    controller.updateCharacter(getSelectedCharacter())
                    tableData.setAll(controller.getCharacterList())
                }
            }
            button("Clear Selection") {
                action {
                    clearSelection()
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
                val character = this.selectedItem!!
                idField.text = character.id.toString()
                idField.setEditable(false)
                idField.setDisable(true)
                nameField.text = character.name
                armorClassField.text = character.armor_class.toString()
                initiativeField.text = character.initiative_modifier.toString()
                maxHealthField.text = character.max_health.toString()
                currentHealthField.text = character.current_health.toString()
            }
        }
    }

    private fun getSelectedCharacter() : Character {
        return Character(
                idField.text.toLong(),
                nameField.text,
                armorClassField.text.toLong(),
                initiativeField.text.toLong(),
                maxHealthField.text.toLong(),
                currentHealthField.text.toLong(),
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