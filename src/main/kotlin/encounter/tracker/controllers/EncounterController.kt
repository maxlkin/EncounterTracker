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

class EncounterController: Controller() {
    fun getEncounterList(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<Encounter> {
        return Database.query(driver).selectAllEncounters().executeAsList().asObservable()
    }

    fun getEncounterByID(id: Long, driver: JdbcSqliteDriver = Database.getConnection()): SelectEncounterByID {
        return Database.query(driver).selectEncounterByID(id).executeAsOne()
    }

    fun getNpcList(id: Long , driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<SelectNPCNotIn> {
        return Database.query(driver).selectNPCNotIn(id).executeAsList().asObservable()
    }

    fun addCharacterToEncounter(characterID: Long, encounterID: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        Database.query(driver).addCharacterToEncounter(characterID, encounterID)
    }

    fun getCharacterList(id: Long , driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<SelectCharactersNotIn> {
        return Database.query(driver).selectCharactersNotIn(id).executeAsList().asObservable()
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