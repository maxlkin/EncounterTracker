package encounter.tracker.controllers

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import encounter.tracker.models.CharacterModel
import encountertrackerdb.Character
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.lang.Exception

class CharacterController: Controller() {

    fun getCharacterList(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<Character> {
        return Database.query(driver).selectAllCharacters().executeAsList().asObservable()
    }

    fun getFilteredCharacterList(character: CharacterModel, driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<Character> {
        val name = if (character.name == null || character.name.isEmpty()) "" else character.name
        val armorClass = if (character.armorClass == null) "" else character.armorClass.toString()
        val initiative = if (character.initiative == null) "" else character.initiative.toString()
        val maxHealth = if (character.maxHealth == null) "" else character.maxHealth.toString()
        val currentHealth = if (character.currentHealth == null) "" else character.currentHealth.toString()

        return Database.query(driver).selectCharacters(
                "%$name%",
                "%$armorClass%",
                "%$initiative%",
                "%$maxHealth%",
                "%$currentHealth%"
        ).executeAsList().asObservable()
    }

    fun deleteCharacter(id: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        println("Deleting character $id")
        Database.query(driver).deleteCharacterByID(id)
    }

    @Throws(Exception::class)
    fun addCharacter(character: CharacterModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (character.name == null || character.name.isEmpty()) {
            throw Exception("Name field cannot be empty.")
        }
        if (character.armorClass == null) {
            throw Exception("Armor class field cannot be empty or contain letters.")
        }
        if (character.initiative == null) {
            throw Exception("Initiative field cannot be empty or contain letters.")
        }
        if (character.maxHealth == null) {
            throw Exception("Max health field cannot be empty or contain letters.")
        }
        if (character.currentHealth == null) {
            throw Exception("Current health field cannot be empty or contain letters.")
        }
        println("Adding character ${character.name}")
        Database.query(driver).insertCharacter(
            character.name,
            character.armorClass,
            character.initiative,
            character.maxHealth,
            character.currentHealth,
            null,
            null
        )
    }

    @Throws(Exception::class)
    fun updateCharacter(character: CharacterModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (character.name == null || character.name.isEmpty()) {
            throw Exception("Name field cannot be empty.")
        }
        if (character.armorClass == null) {
            throw Exception("Armor class field cannot be empty or contain letters.")
        }
        if (character.initiative == null) {
            throw Exception("Initiative field cannot be empty or contain letters.")
        }
        if (character.maxHealth == null) {
            throw Exception("Max health field cannot be empty or contain letters.")
        }
        if (character.currentHealth == null) {
            throw Exception("Current health field cannot be empty or contain letters.")
        }
        if (character.id == null) {
            throw Exception("ID cannot be empty or contain letters when updating a character.")
        }
        println("Updating character ${character.id}")
        Database.query(driver).updateCharacterByID(
            character.name,
            character.armorClass,
            character.initiative,
            character.maxHealth,
            character.currentHealth,
            null,
            character.id
        )
    }

}