package encounter.tracker.views
import encounter.tracker.controllers.CharacterController
import encountertrackerdb.Character
import javafx.geometry.Pos
import javafx.scene.control.SelectionMode
import tornadofx.*
import tornadofx.WizardStyles.Companion.graphic

class CharacterView: View() {
    private val controller: CharacterController by inject()
    override val root = vbox(10) {
        addClass(Style.view)
        label("Characters") {
            style {
                fontSize = 18.px
            }
        }
        form {
            fieldset("Character Details") {
                vbox {
                    hbox(20) {
                        field("ID") { textfield() }
                        field("Name") { textfield() }
                        field("Armor Class") { textfield() }
                    }
                    hbox(20) {
                        field("Initiative") { textfield() }
                        field("Max Health") { textfield() }
                        field("Current Health") { textfield() }
                    }
                }
            }
        }

        hbox(20) {
            button("Filter")
            button("Add Character")
            button("Update Character")
        }

        val tableData = controller.getCharacterList()

        tableview(tableData) {
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
                            println(controller.getCharacterList().asObservable())
                            tableData.setAll(controller.getCharacterList())
                        }
                    }
                }
            }
        }
    }
}