package encounter.tracker.views
import encounter.tracker.controllers.CharacterController
import encountertrackerdb.Character
import tornadofx.*

class CharacterView: View() {
    private val controller: CharacterController by inject()
    override val root = vbox {
        tableview(controller.getCharacterList()) {
            readonlyColumn("ID",Character::id)
            readonlyColumn("Name", Character::name)
            readonlyColumn("Armor Class", Character::armor_class)
            readonlyColumn("Initiative Modifier",Character::initiative_modifier)
            readonlyColumn("Max Health",Character::max_health)
            readonlyColumn("Current Health",Character::current_health)
        }
    }
}