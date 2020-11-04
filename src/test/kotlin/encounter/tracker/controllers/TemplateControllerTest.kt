package encounter.tracker.controllers

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import encounter.tracker.database.Database
import encounter.tracker.controllers.CharacterController
import encounter.tracker.models.CharacterModel
import encounter.tracker.models.CharacterTemplateModel
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TemplateControllerTest {
    /**
     * Single database in memory connection for testing.
     */
    private lateinit var driver: JdbcSqliteDriver

    private lateinit var controller: TemplateController

    /**
     * Setting up connection and inserting test data.
     */
    @Before
    fun setup() {
        driver = Database.getTestConnection()
        controller = TemplateController()
        Database.query(driver).insertTemplate("Goblin", "A test goblin.")
        Database.query(driver).insertTemplate("Fish", "A test fish.")
        Database.query(driver).insertTemplate("Bear", "A test bear.")
    }

    @Test
    fun testGetTemplateList() {
        val characters = controller.getTemplateList(driver)
        assertEquals("Goblin", characters[0].type)
        assertEquals("Fish", characters[1].type)
        assertEquals("Bear", characters[2].type)
    }

    @Test
    fun testGetFilteredTemplateList() {
        val templateModel = CharacterTemplateModel(2, "", "")
        val templates = controller.getFilteredTemplateList(templateModel, driver)
        assertEquals("Fish", templates[0].type)
    }

    @Test
    fun testDeleteTemplate() {
        controller.deleteTemplate(1, driver)
        val templates = controller.getTemplateList(driver)
        assertEquals(2, templates.size)
    }

    @Test
    fun testAddTemplate() {
        val templateModel = CharacterTemplateModel(0, "Devil", "Test devil.")
        controller.addTemplate(templateModel, driver)
        val characters = controller.getTemplateList(driver)
        assertEquals("Devil", characters[3].type)
    }

    @Test
    fun testUpdateTemplate() {
        val templateModel = CharacterTemplateModel(2, "Fisherman", "A fisherman")
        controller.updateTemplate(templateModel, driver)
        val templates = controller.getTemplateList(driver)
        assertEquals("Fisherman", templates[1].type)
    }
}