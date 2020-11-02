package encounter.tracker.views
import encounter.tracker.database.Database
import encountertrackerdb.Character
import encountertrackerdb.MainQueries
import javafx.beans.property.ReadOnlyListProperty
import javafx.scene.Parent
import tornadofx.*

class CharacterView: View() {
    override val root = vbox {
        label("Character view")
        val characterList = Database.query().selectAllCharacters().executeAsList()
        val characters = characterList.asObservable()

        tableview(characters) {
            readonlyColumn("ID",Character::id)
            readonlyColumn("Name", Character::name)
            readonlyColumn("Armor Class", Character::armor_class)
            readonlyColumn("Initiative Modifier",Character::initiative_modifier)
            readonlyColumn("Max Health",Character::max_health)
            readonlyColumn("Current Health",Character::current_health)
        }
    }
}