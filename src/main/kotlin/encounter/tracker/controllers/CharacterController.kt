package encounter.tracker.controllers

import encounter.tracker.database.Database
import encountertrackerdb.Character
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable

class CharacterController: Controller() {

    fun getCharacterList(): ObservableList<Character> {
        return Database.query().selectAllCharacters().executeAsList().asObservable()
    }

    fun deleteCharacter(id: Long) {
        println("Deleting character $id")
        Database.query().deleteCharacterByID(id)
    }

    fun addCharacter(character: Character) {
        println("Adding character ${character.name}")
        Database.query().insertCharacter(
            character.name,
            character.armor_class,
            character.initiative_modifier,
            character.max_health,
            character.current_health
        )
    }

    fun updateCharacter(character: Character) {
        println("Updating character ${character.id}")
        Database.query().updateCharacterByID(
            character.name,
            character.armor_class,
            character.initiative_modifier,
            character.max_health,
            character.current_health,
            character.id
        )
    }

}