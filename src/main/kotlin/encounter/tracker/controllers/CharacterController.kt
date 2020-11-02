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

}