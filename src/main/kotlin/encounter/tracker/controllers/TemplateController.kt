package encounter.tracker.controllers

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import encounter.tracker.models.CharacterTemplateModel
import encountertrackerdb.CharacterTemplate
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.lang.Exception

class TemplateController: Controller() {
    fun getTemplateList(driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<CharacterTemplate> {
        return Database.query(driver).selectAllTemplates().executeAsList().asObservable()
    }

    fun getFilteredTemplateList(template: CharacterTemplateModel, driver: JdbcSqliteDriver = Database.getConnection()): ObservableList<CharacterTemplate> {
        val id = if (template.id == null) "" else template.id
        val type = if (template.type == null || template.type.isEmpty()) "" else template.type.toString()
        val description = if (template.description == null || template.description.isEmpty()) "" else template.description.toString()

        return Database.query(driver).selectTemplates(
                "%$id%",
                "%$type%",
                "%$description%"
        ).executeAsList().asObservable()
    }

    fun deleteTemplate(id: Long, driver: JdbcSqliteDriver = Database.getConnection()) {
        println("Deleting template $id")
        Database.query(driver).deleteTemplate(id)
    }

    @Throws(Exception::class)
    fun addTemplate(template: CharacterTemplateModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (template.type == null || template.type.isEmpty()) {
            throw Exception("Type field cannot be empty.")
        }
        if (template.description == null || template.description.isEmpty()) {
            throw Exception("Description cannot be empty.")
        }

        println("Adding template ${template.type}")
        Database.query(driver).insertTemplate(
                template.type,
                template.description
        )
    }

    @Throws(Exception::class)
    fun updateTemplate(template: CharacterTemplateModel, driver: JdbcSqliteDriver = Database.getConnection()) {
        if (template.type == null || template.type.isEmpty()) {
            throw Exception("Type field cannot be empty.")
        }
        if (template.description == null || template.description.isEmpty()) {
            throw Exception("Description field cannot be empty or contain letters.")
        }
        if (template.id == null) {
            throw Exception("ID cannot be empty or contain letters when updating a character.")
        }
        println("Updating template ${template.id}")
        Database.query(driver).updateTemplateByID(
                template.type,
                template.description,
                template.id
        )
    }
}