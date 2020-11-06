package encounter.tracker.controllers

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import encounter.tracker.models.EncounterModel
import encountertrackerdb.*
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.lang.Exception
import javax.xml.crypto.Data
import kotlin.random.Random

class EncounterController: Controller() {
    fun getEncounterList(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<Encounter> {
        return Database.query(driver).selectAllEncounters().executeAsList().asObservable()
    }

    fun getEncounterByID(id: Long, driver: JdbcSqliteDriver = Database.getConnection()): Encounter {
        return Database.query(driver).selectEncounterByID(id).executeAsOne()
    }

    fun getNpcList(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<SelectAllCharactersWithTemplate> {
        return Database.query(driver).selectAllCharactersWithTemplate().executeAsList().asObservable()
    }

    /**
     *
     */
    fun addCharacterToEncounter(characterID: Long, encounterID: Long, copy: Boolean = false,  driver: JdbcSqliteDriver = Database.getConnection()) {
        if (copy) {
            // Add a copy of a character
            val character = Database.query(driver).selectCharacterByID(characterID).executeAsOne()
            // Insert copy
            Database.query(driver).insertCharacter(character.name, character.armor_class, character.initiative_modifier, character.max_health, character.current_health, character.character_template_id, character.id)
            val copy_id = Database.query(driver).lastInsertRowId().executeAsOne()
            // Add copy to encounter
            Database.query(driver).addCharacterToEncounter(copy_id, encounterID)
        } else {
            // Add original character
            Database.query(driver).addCharacterToEncounter(characterID, encounterID)
        }
    }

    fun removeCharacterFromEncounter(listID: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        val listing = Database.query(driver).selectCharacterListing(listID).executeAsOne()
        val character = Database.query(driver).selectCharacterByID(listing.character_id).executeAsOne()

        if (character.copy == null) {
            // Remove normally
            Database.query(driver).removeCharacterFromEncounter(listID)
        } else {
            // Remove copied character as well as listing.
            Database.query(driver).removeCharacterFromEncounter(listID)
            Database.query(driver).removeCharacterFromEncounter(character.id)
        }
        Database.query(driver).removeCharacterFromEncounter(listID)
    }

    fun getEncounterCharacters(encounterID: Long, driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<SelectCharactersIn> {
        return Database.query(driver).selectCharactersIn(encounterID).executeAsList().asObservable()
    }

    fun getCharacterList(id: Long , driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<SelectCharactersNotIn> {
        return Database.query(driver).selectCharactersNotIn(id).executeAsList().asObservable()
    }

    fun rollInitiative(encounterID: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        val characters = getEncounterCharacters(encounterID)
        characters.forEach {
            val initiative = Random.nextInt(1, 20) + it.initiative_modifier
            println("Rolled $initiative initiative.")
            Database.query(driver).setInitiative(initiative, it.id_)
        }
    }

    fun rollNulledInitiative(encounterID: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        val characters = getEncounterCharacters(encounterID)
        characters.forEach {
            if (it.initiative == null) {
                val initiative = Random.nextInt(1, 20) + it.initiative_modifier
                println("Rolled $initiative initiative.")
                Database.query(driver).setInitiative(initiative, it.id_)
            }
        }
    }


    @Throws(Exception::class)
    fun addEncounter(encounter: EncounterModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (encounter.name == null || encounter.name.isEmpty()) {
            throw Exception("Encounter name cannot be empty.")
        }

        println("Adding encounter ${encounter.name}")
        Database.query(driver).insertEncounter(
                encounter.name,
        )
    }
}