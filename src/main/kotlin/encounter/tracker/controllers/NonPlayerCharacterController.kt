package encounter.tracker.controllers

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import encounter.tracker.models.CharacterModel
import encounter.tracker.models.CharacterTemplateComboModel
import encountertrackerdb.Character
import encountertrackerdb.CharacterTemplate
import encountertrackerdb.SelectAllCharactersWithTemplate
import encountertrackerdb.SelectCharactersWithTemplate
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.lang.Exception
import javax.xml.crypto.Data

class NonPlayerCharacterController: Controller() {

    fun getTemplates(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<CharacterTemplate> {
        return Database.query(driver).selectAllTemplates().executeAsList().asObservable()
    }

    fun getCharacterList(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<CharacterModel> {
        val list = Database.query(driver).selectAllCharactersWithTemplate().executeAsList().asObservable()
        val characterList = ArrayList<CharacterModel>()
        list.forEach {
            characterList.add(CharacterModel(
                    it.id,
                    it.name,
                    it.armor_class,
                    it.initiative_modifier,
                    it.max_health,
                    it.current_health,
                    it.character_template_id
            ))
        }
        return characterList.asObservable()
    }

    fun getTemplateByID(id: Long, driver: JdbcSqliteDriver = Database.getConnection()): CharacterTemplate {
        return Database.query(driver).selectTemplateByID(id).executeAsOne()
    }

    fun getFilteredCharacterList(character: CharacterModel, driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<CharacterModel> {
        val name = if (character.name == null || character.name.isEmpty()) "" else character.name
        val armorClass = if (character.armorClass == null) "" else character.armorClass.toString()
        val initiative = if (character.initiative == null) "" else character.initiative.toString()
        val maxHealth = if (character.maxHealth == null) "" else character.maxHealth.toString()
        val currentHealth = if (character.currentHealth == null) "" else character.currentHealth.toString()
        val template = Database.query(driver).selectTemplateByID(character.characterTemplateID ?: 0).executeAsOne()

        val list =  Database.query(driver).selectCharactersWithTemplate(
                "%$name%",
                "%$armorClass%",
                "%$initiative%",
                "%$maxHealth%",
                "%$currentHealth%",
                "%${template.type}%",
                "%${template.description}%"
        ).executeAsList().asObservable()

        val characterList = ArrayList<CharacterModel>()

        list.forEach {
            characterList.add(CharacterModel(
                    it.id,
                    it.name,
                    it.armor_class,
                    it.initiative_modifier,
                    it.max_health,
                    it.current_health,
                    it.character_template_id
            ))
        }
        return characterList.asObservable()
    }

    fun deleteCharacter(id: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        println("Deleting character $id")
        Database.query(driver).deleteCharacterByID(id)
    }

    @Throws(Exception::class)
    fun addCharacter(character: CharacterModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (character.characterTemplateID == null) {
            throw Exception("Template field cannot be empty.")
        }
        var name = character.name ?: ""
        // Set name as template name if empty.
        // We might not care about what name a throwaway character has.
        if (name.isEmpty()) {
            val template = Database.query(driver).selectTemplateByID(character.characterTemplateID).executeAsOne()
            name = template.type ?: ""
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
            name,
            character.armorClass,
            character.initiative,
            character.maxHealth,
            character.currentHealth,
            character.characterTemplateID,
            null
        )
    }

    @Throws(Exception::class)
    fun updateCharacter(character: CharacterModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (character.characterTemplateID == null) {
            throw Exception("Template field cannot be empty.")
        }
        var name = character.name ?: ""
        // Set name as template name if empty.
        // We might not care about what name a throwaway character has.
        if (name.isEmpty()) {
            val template = Database.query(driver).selectTemplateByID(character.characterTemplateID).executeAsOne()
            name = template.type ?: ""
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
            name,
            character.armorClass,
            character.initiative,
            character.maxHealth,
            character.currentHealth,
            character.characterTemplateID,
            character.id
        )
    }
}